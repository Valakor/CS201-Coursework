package cs201.roles.restaurantRoles.Matt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.roles.restaurant.Matt.CustomerGuiMatt;
import cs201.helper.Constants;
import cs201.helper.Matt.MenuMatt;
import cs201.interfaces.roles.restaurant.Matt.CashierMatt;
import cs201.interfaces.roles.restaurant.Matt.CustomerMatt;
import cs201.interfaces.roles.restaurant.Matt.HostMatt;
import cs201.interfaces.roles.restaurant.Matt.WaiterMatt;
import cs201.roles.restaurantRoles.RestaurantCustomerRole;
import cs201.trace.AlertLog;
import cs201.trace.AlertTag;

/**
 * Restaurant customer agent.
 */
public class RestaurantCustomerRoleMatt extends RestaurantCustomerRole implements CustomerMatt {
	private final int EATINGDURATION = 1600;
	private final int CHOOSINGDURATION = 625;
	private final int WAITINGDURATION = 1000;
	private Timer customerTimer;
	private CustomerGuiMatt customerGui;

	// Agent correspondents
	private HostMatt host;
	private WaiterMatt waiter;
	private CashierMatt cashier;
	private MenuMatt menu;
	private String choice;
	private String forceChoice;
	
	private int attemptedOrders;
	private double checkAmount;
	
	private Semaphore atTargetPosition = new Semaphore(0, false); // used for animation

	private enum AgentState
	{ DoingNothing, WaitingInRestaurant, AboutToBeSeated, BeingSeated, readingMenu, Hailed, Ordering, Eating, WaitingForCheck, PayingCheck, Leaving };
	private AgentState state = AgentState.DoingNothing;

	private enum AgentEvent 
	{ none, gotHungry, waitedTooLong, followWaiter, seated, readyToOrder, cannotAffordAnything, tellWaiterOrder, foodArrived, doneEating, gotCheck, checkPaid };
	private AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 */
	public RestaurantCustomerRoleMatt(RestaurantCashierRoleMatt cashier) {
		super();
		this.cashier = cashier;
		this.checkAmount = 0;
		this.attemptedOrders = 0;
		this.forceChoice = "";
	}
	
	public RestaurantCustomerRoleMatt() {
		super();
		this.cashier = null;
		this.checkAmount = 0;
		this.attemptedOrders = 0;
		this.forceChoice = "";
	}

	public void setCashier(CashierMatt cashier) {
		this.cashier = cashier;
	}
	
	public void setHost(HostMatt host) {
		this.host = host;
	}
	
	// Messages -------------------------------------------------------------
	@Override
	public void msgIsHungry() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "I'm hungry.");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	@Override
	public void msgAboutToBeSeated() {
		state = AgentState.AboutToBeSeated;
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "About to be seated.");
		stateChanged();
	}

	@Override
	public void msgFollowMeToTable(WaiterMatt waiter, MenuMatt menu) {
		this.waiter = waiter;
		event = AgentEvent.followWaiter;
		this.menu = menu;
		stateChanged();
	}

	@Override
	public void msgWhatWouldYouLike() {
		event = AgentEvent.tellWaiterOrder;
		stateChanged();
	}
	
	@Override
	public void msgHereIsYourFood(String choice) {
		event = AgentEvent.foodArrived;
		stateChanged();
	}
	
	@Override
	public void msgOutOfYourChoice(MenuMatt m) {
		this.menu = m;	
		event = AgentEvent.seated;
		state = AgentState.BeingSeated;
		stateChanged();
	}
	
	@Override
	public void msgHereIsYourCheck(double check) {
		checkAmount = check;
		event = AgentEvent.gotCheck;
		stateChanged();
	}
	
	@Override
	public void msgHereIsYourChange(double change) {
		myPerson.setMoney(change);
		event = AgentEvent.checkPaid;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry){
			state = AgentState.WaitingInRestaurant;
			GoToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.waitedTooLong) {
			state = AgentState.DoingNothing;
			WaitTimeTooLong();
			return true;
		}
		if (state == AgentState.AboutToBeSeated && event == AgentEvent.followWaiter){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.readingMenu;
			ChooseFood();
			return true;
		}
		if (state == AgentState.readingMenu && event == AgentEvent.readyToOrder){
			state = AgentState.Hailed;
			HailWaiter();
			return true;
		}
		if (state == AgentState.readingMenu && event == AgentEvent.cannotAffordAnything){
			state = AgentState.Leaving;
			LeaveRestaurant();
			return true;
		}
		if (state == AgentState.Hailed && event == AgentEvent.tellWaiterOrder){
			state = AgentState.Ordering;
			OrderFood();
			return true;
		}
		if (state == AgentState.Ordering && event == AgentEvent.foodArrived){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.WaitingForCheck;
			AskForCheck();
			return true;
		}
		if (state == AgentState.WaitingForCheck && event == AgentEvent.gotCheck){
			state = AgentState.PayingCheck;
			PayCheck();
			return true;
		}
		if (state == AgentState.PayingCheck && event == AgentEvent.checkPaid){
			state = AgentState.Leaving;
			LeaveRestaurant();
			return true;
		}

		return false;
	}

	// Actions -------------------------------------------------------------
	private void GoToRestaurant() {
		DoGoToRestaurant();
		host.msgIWantToEat(this);
		DoGoToWaitingArea();
		DoWaitTimer();
	}
	
	private void WaitTimeTooLong() {
		host.msgWaitTimeTooLong(this);
		DoWaitTimeTooLong();
		event = AgentEvent.none;
		attemptedOrders = 0;
		DeactivateRole();
	}
	
	private void SitDown() {
		DoSitDown(); // contains animation
		event = AgentEvent.seated;
	}

	private void ChooseFood() {
		if (attemptedOrders >= 2) {
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Tried to order twice and didn't receive anything, so I'm leaving the restaurant.");
			LeaveRestaurant();
			return;
		}
		DoChooseFood();
		ChooseFoodTimer();
	}

	private void HailWaiter() {
		DoHailWaiter();
		waiter.msgReadyToOrder(this);
	}

	private void OrderFood() {
		DoOrderFood();
		attemptedOrders++;
		waiter.msgHereIsMyChoice(this, choice);
	}
	
	private void EatFood() {
		DoEatFood();
		EatFoodTimer();
	}
	
	private void AskForCheck() {
		DoAskForCheck();
		waiter.msgDoneEatingAndNeedCheck(this);
	}
	
	private void PayCheck() {
		DoPayCheck(); // contains animation
		cashier.msgPayCheck(this, myPerson.getMoney(), checkAmount);
		myPerson.setMoney(0);
	}
	
	private void LeaveRestaurant() {
		waiter.msgPayingAndLeaving(this);
		DoLeaveRestaurant(); // contains animation
		state = AgentState.DoingNothing;
		event = AgentEvent.none;
		attemptedOrders = 0;
		DeactivateRole();
	}

	// Utilities -------------------------------------------------------------
	private void DoGoToRestaurant() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Going to restaurant.");
		customerGui.DoGoToRestaurant();
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoGoToWaitingArea() {
		customerGui.DoGoToWaitingArea();
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoWaitTimer() {
		customerTimer = new Timer(WAITINGDURATION, 
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				event = AgentEvent.waitedTooLong;
				stateChanged();
			}
		});
		customerTimer.setRepeats(false);
		customerTimer.start();
	}
	
	private void DoWaitTimeTooLong() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Wait time too long. I'm leaving the restaurant.");
		
		customerGui.DoExitRestaurant();
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Has left the restaurant.");
	}
	
	private void DoSitDown() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Sitting down.");
		customerGui.Animate();
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoChooseFood() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Choosing food.");
		customerGui.setMessage("");
	}
	
	private void ChooseFoodTimer() {
		customerTimer = new Timer((int) (CHOOSINGDURATION * Constants.ANIMATION_SPEED_FACTOR), 
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MenuMatt tempMenu = new MenuMatt();
				if (tempMenu.getPrice(forceChoice) != null) {
					choice = forceChoice;
				} else {
					choice = null;
					do {
						choice = menu.randomItem();
						if (menu.getPrice(choice) <= myPerson.getMoney()) {
							break;
						}
						menu.removeItem(choice);
						choice = null;
					} while (menu.size() > 0);
					if (choice == null) {
						AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Can't afford anything! I'm is leaving the restaurant.");
						event = AgentEvent.cannotAffordAnything;
						stateChanged();
						return;
					}
				}
				
				event = AgentEvent.readyToOrder;
				stateChanged();
			}
		});
		customerTimer.setRepeats(false);
		customerTimer.start();
	}
	
	private void DoHailWaiter() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Hailing his waiter, " + waiter.toString() + ".");
	}
	
	private void DoOrderFood() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Ordering.");
		customerGui.setMessage(choice + "?");
	}
	
	private void DoEatFood() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Eating.");
		customerGui.setMessage(choice);
	}
	
	private void EatFoodTimer() {
		customerTimer = new Timer((int) (EATINGDURATION * Constants.ANIMATION_SPEED_FACTOR), 
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				event = AgentEvent.doneEating;
				myPerson.justAte();
				stateChanged();
			}
		});
		customerTimer.setRepeats(false);
		customerTimer.start();
	}
	
	private void DoAskForCheck() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Asking " + waiter.toString() + " for the bill.");
		customerGui.setMessage("$$?");
	}
	
	private void DoPayCheck() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Going to " + cashier.toString() + " to pay bill.");
		customerGui.DoGoToCashier();
		customerGui.setMessage(String.format("$%.2f", this.checkAmount));
		try {
			atTargetPosition.acquire();
			Thread.sleep((int) (500 * Constants.ANIMATION_SPEED_FACTOR));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoLeaveRestaurant() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Leaving the restaurant.");
		customerGui.DoExitRestaurant();
		try {
			atTargetPosition.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT, getName(), "Has left the restaurant.");
	}
	
	private void DeactivateRole() {
		this.isActive = false;
		this.customerGui.setPresent(false);
	}
	
	/**
	 * The CustomerGui informs the Customer that it is done animating to its destination, freeing up the CustomerAgent
	 */
	public void DoneAnimating() {
		atTargetPosition.release();
	}
	
	// Utility -------------------------------------------------------------
	public void setForceChoice(String choice) {
		forceChoice = choice;
	}
	
	public void setGui(CustomerGuiMatt g) {
		customerGui = g;
	}

	@Override
	public CustomerGuiMatt getGui() {
		return customerGui;
	}

	@Override
	public void startInteraction(Intention intent) {
		if (intent == Intention.RestaurantCustomer) {
			this.customerGui.setPresent(true);
			this.msgIsHungry();
		}
	}

	@Override
	public void msgClosingTime() {
		// Nothing to do here
	}
}

