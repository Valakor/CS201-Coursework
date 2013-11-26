package cs201.structures.restaurant;

import java.util.ArrayList;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.StructurePanel;
import cs201.gui.roles.restaurant.Matt.CashierGuiMatt;
import cs201.gui.roles.restaurant.Matt.CookGuiMatt;
import cs201.gui.roles.restaurant.Matt.CustomerGuiMatt;
import cs201.gui.roles.restaurant.Matt.WaiterGuiMatt;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.helper.CityTime;
import cs201.helper.Matt.RestaurantRotatingStand;
import cs201.roles.Role;
import cs201.roles.restaurantRoles.RestaurantWaiterRole;
import cs201.roles.restaurantRoles.Matt.RestaurantCashierRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantCookRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantCustomerRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantHostRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMattNormal;
import cs201.roles.restaurantRoles.Matt.RestaurantWaiterRoleMattStand;

public class RestaurantMatt extends Restaurant {
	private final int INITIALWAITERS = 2;
	private final int MAXWAITERS = 4;
	private RestaurantRotatingStand stand = new RestaurantRotatingStand();
	
	public RestaurantMatt(int x, int y, int width, int height, int id, StructurePanel p) {
		super(x, y, width, height, id, p);
		
		// Setup all roles that are persistent in this Restaurant
		this.host = new RestaurantHostRoleMatt();
		host.setRestaurant(this);
		
		this.cook = new RestaurantCookRoleMatt();
		CookGuiMatt cookGui = new CookGuiMatt((RestaurantCookRoleMatt) cook);
		cookGui.setPresent(false);
		((RestaurantCookRoleMatt) cook).setGui(cookGui);
		((RestaurantCookRoleMatt) cook).setRotatingStand(stand);
		this.panel.addGui(cookGui);
		cook.setRestaurant(this);
		
		
		this.cashier = new RestaurantCashierRoleMatt();
		CashierGuiMatt cashierGui = new CashierGuiMatt((RestaurantCashierRoleMatt) cashier);
		cashierGui.setPresent(false);
		((RestaurantCashierRoleMatt) cashier).setGui(cashierGui);
		((RestaurantCashierRoleMatt) cashier).setHost((RestaurantHostRoleMatt) host);
		this.panel.addGui(cashierGui);
		cashier.setRestaurant(this);
		
		this.waiters = new ArrayList<RestaurantWaiterRole>();
		for (int i = 0; i < INITIALWAITERS; i++) {
			RestaurantWaiterRoleMatt newWaiter;
			if (i % 2 == 0) {
				newWaiter = new RestaurantWaiterRoleMattNormal();
			} else {
				newWaiter = new RestaurantWaiterRoleMattStand();
			}
			WaiterGuiMatt waiterGui = new WaiterGuiMatt((RestaurantWaiterRoleMatt) newWaiter, null);
			waiterGui.setPresent(false);
			((RestaurantWaiterRoleMatt) newWaiter).setGui(waiterGui);
			((RestaurantWaiterRoleMatt) newWaiter).setRotatingStand(stand);
			waiters.add(newWaiter);
			this.panel.addGui(waiterGui);
			newWaiter.setRestaurant(this);
		}
	}
	
	@Override
	public Role getRole(Intention role) {
		switch (role) {
		case RestaurantCook: {
			if (cook.getPerson() == null) {
				((RestaurantCookRoleMatt) cook).getGui().setPresent(true);
				Do("Cook Check");
				return cook;
			}
			return null;
		}
		case RestaurantHost: {
			if (host.getPerson() == null) {
				Do("Host Check");
				return host;
			}
			return null;
		}
		case RestaurantWaiter: {
			for (RestaurantWaiterRole r : waiters) {
				if (r.getPerson() == null) {
					((RestaurantHostRoleMatt) host).addWaiter((RestaurantWaiterRoleMatt) r);
					UpdateWaiterHomePositions();
					((RestaurantWaiterRoleMatt) r).getGui().setPresent(true);
					Do("Waiter Check 1");
					return r;
				}
			}
			
			if (waiters.size() < MAXWAITERS) {
				RestaurantWaiterRole newWaiter;
				if (waiters.size() % 2 == 0) {
					newWaiter = new RestaurantWaiterRoleMattNormal();
				} else {
					newWaiter = new RestaurantWaiterRoleMattStand();
				}
				WaiterGuiMatt waiterGui = new WaiterGuiMatt((RestaurantWaiterRoleMatt) newWaiter, null);
				((RestaurantWaiterRoleMatt) newWaiter).setGui(waiterGui);
				waiters.add(newWaiter);
				((RestaurantHostRoleMatt) host).addWaiter((RestaurantWaiterRoleMatt) newWaiter);
				UpdateWaiterHomePositions();
				((RestaurantWaiterRoleMatt) newWaiter).setRotatingStand(stand);
				this.panel.addGui(waiterGui);
				newWaiter.setRestaurant(this);
				((RestaurantWaiterRoleMatt) newWaiter).getGui().setPresent(true);
				Do("Waiter Check 2");
				return newWaiter;
			}
			
			return null;
		}
		case RestaurantCashier: {
			if (cashier.getPerson() == null) {
				((RestaurantCashierRoleMatt) cashier).getGui().setPresent(true);
				Do("Cashier Check");
				return cashier;
			}
			return null;
		}
		case RestaurantCustomer: {
			RestaurantCustomerRoleMatt newCustomer = new RestaurantCustomerRoleMatt();
			CustomerGuiMatt customerGui = new CustomerGuiMatt((RestaurantCustomerRoleMatt) newCustomer, null);
			((RestaurantCustomerRoleMatt) newCustomer).setGui(customerGui);
			newCustomer.setCashier((RestaurantCashierRoleMatt) cashier);
			newCustomer.setHost((RestaurantHostRoleMatt) host);
			this.panel.addGui(customerGui);
			newCustomer.setRestaurant(this);
			return newCustomer;
		}
		default: {
			Do("Wrong Intention provided in getRole(Intention)");
			return null;
		}
		}
	}
	
	private void checkIfRestaurantShouldOpen() {
		if (host.getPerson() != null && cashier.getPerson() != null && cook.getPerson() != null) {
			for (RestaurantWaiterRole w : waiters) {
				if (w.getPerson() != null) {
					Do("Open for business!");
					this.isOpen = true;
					return;
				}
			}
		}
	}
	
	private void UpdateWaiterHomePositions() {		
		int initialX = (int)(RestaurantAnimationPanelMatt.WINDOWX * .48f);
    	int initialY = (int)(RestaurantAnimationPanelMatt.WINDOWY * .18f);
    	int mult = (int)(RestaurantAnimationPanelMatt.WINDOWX * .024f);
    	int offset = (int)(RestaurantAnimationPanelMatt.WINDOWX * .048f);
    	
    	synchronized(waiters) {
    		for (RestaurantWaiterRoleMatt waiter : ((RestaurantHostRoleMatt) host).getActiveWaiters()) {
    			int x = initialX - (((RestaurantHostRoleMatt) host).getNumActiveWaiters() - 1) * mult;
    			int y = initialY;
				waiter.getGui().SetHomePosition(x, y);
				waiter.getGui().GoToWaitingPosition();
				initialX += offset;
    		}
    	}
    }

	@Override
	public void updateTime(CityTime time) {
		if (!isOpen) {
			checkIfRestaurantShouldOpen();
		}
		
		if (time.equalsIgnoreDay(this.closingTime)) {
			Do("It's closing time!");
			if (host.getPerson() != null) {
				host.msgClosingTime();
			} else {
				closingTime();
			}
		}
	}
	
	@Override
	public void closingTime() {
		cashier.msgClosingTime();
		cook.msgClosingTime();
		for (RestaurantWaiterRole r : waiters) {
			r.msgClosingTime();
		}
	}

}
