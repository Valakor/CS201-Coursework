package cs201.roles.restaurantRoles.Ben;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelBen;
import cs201.helper.CityDirectory;
import cs201.helper.Ben.RestaurantRotatingStandBen;
import cs201.helper.Ben.RestaurantRotatingStandBen.RotatingStandOrderBen;
import cs201.interfaces.roles.restaurant.Ben.CashierBen;
import cs201.interfaces.roles.restaurant.Ben.CookBen;
import cs201.interfaces.roles.restaurant.Ben.WaiterBen;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.roles.restaurantRoles.RestaurantCookRole;
import cs201.roles.restaurantRoles.Ben.RestaurantCookRoleBen.MarketOrder.Item;
import cs201.structures.market.MarketStructure;

/**
 * Restaurant Cook Agent
 */
public class RestaurantCookRoleBen extends RestaurantCookRole implements CookBen {
	
	private String name;
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	private Timer timer;
	private Map<String, MyFood> inventory = new HashMap<String, MyFood>();
	private CashierBen cashier = null;
	
	private boolean closingTime = false;
	
	private boolean checkInventory = true;
	
	private RestaurantAnimationPanelBen animPanel = null;
	private RestaurantRotatingStandBen stand = null;
	
	public RestaurantCookRoleBen() {
		this("");
	}
	
	public RestaurantCookRoleBen(String name) {
		super();

		this.name = name;
		
		timer = new Timer();
		
		// Set up our map of foods
		/*
		String chickenString = JOptionPane.showInputDialog("Enter the amount of starting Chicken for the restaurant:");
		String steakString = JOptionPane.showInputDialog("Enter the amount of starting Steak for the restaurant");
		int chickenAmount = 0;
		int steakAmount = 0;
		if (!chickenString.equals(""))
			chickenAmount = Integer.parseInt(chickenString);
		if (!steakString.equals(""))
			steakAmount = Integer.parseInt(steakString);
			*/
		int chickenAmount = 0;
		int steakAmount = 0;

		inventory.put("Chicken", new MyFood("Chicken", 10000, chickenAmount, 1, 10));
		inventory.put("Steak", new MyFood("Steak", 12000, steakAmount, 1, 10));
		
	}
	
	/**
	 * Set corresponding instances...
	 */
	
	public void setCashier(CashierBen c) {
		cashier = c;
	}
	
	public void setAnimPanel(RestaurantAnimationPanelBen p) {
		animPanel = p;
	}
	
	public void setRotatingStand(RestaurantRotatingStandBen s) {
		stand = s;
	}

	/**
	 * Messages.
	 */
	
	public void checkInventory() {
		orderFoodIfInventoryLow();
	}
	
	public void msgHereIsOrder(WaiterBen waiter, String choice, int table) {
		orders.add(new Order(waiter, choice, table, OrderState.pending));
		Do("Just got an order of " + choice);
		
		stateChanged();
	}
	
	/**
	 * Sent to me by the Cashier letting me know my food has arrived from the market.
	 */
	public void msgHereIsYourFood(ItemRequest request) {
		
		MyFood f = inventory.get(request.item);
		if (f != null) {
			f.quantity += request.amount;
			f.orderedMore = false;
			Do("Just received my order of " + request.item);
		}
		
		stateChanged();
	}
	
	public void timerDone(Order order) {
		order.state = OrderState.done;
		Do("The " + order.choice + " is done!");
		
		stateChanged();
	}
	
	public void msgClosingTime() {
		closingTime = true;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {

		if (closingTime) {
			LeaveRestaurant();
			return true;
		}
		
		// Check inventory if we're opening up
		if (checkInventory) {
			orderFoodIfInventoryLow();
			checkInventory = false;
			return true;
		}
		
		// If any orders are finished cooking, plate them
		synchronized(orders) {
			for (Order order : orders) {
				if (order.state == OrderState.done) {
					plateIt(order);
					return true;
				}
			}
		}
		
		// If there is a pending order, start cooking it
		synchronized(orders) {
			for (Order order : orders) {
				if (order.state == OrderState.pending) {
					attemptToCookOrder(order);
					return true;
				}
			}
		}
		
		// If there is a pending stand order, take it off the stand
		if (stand.orderCount() > 0) {
			takeOrderOffStand();
		}
		
		/*
		 * We have tried all our rules and found nothing to do.
		 * Return false to main loop of abstract agent and wait.
		 */
		return false;

	}

	/**
	 * Actions
	 */
	private void attemptToCookOrder(final Order order) {
		// First lets see if we have enough food
		MyFood f = inventory.get(order.choice);
		if (f.quantity == 0) {
			// We're out of food, so let the waiter know
			order.waiter.msgOutOf(order.choice, order.table);
			Do("Out of " + f.type);
			
			// Try to order more food
			orderFoodIfInventoryLow();
			
			// Discard the order, since we can't fulfill it
			orders.remove(order);
			return;
		}
		
		// Animate the cook!
		doCooking(order);
		
		// We're now cooking the order
		order.state = OrderState.cooking;
		
		// We now have one less food item
		f.quantity--;
		
		// Look at our cooking times
		MyFood theFood = inventory.get(order.choice);
		int cookingTime = theFood.cookingTime;
				
		// Set a timer so we don't burn the food
		timer.schedule(new TimerTask() {
			public void run() {
				timerDone(order);
			}
		}, cookingTime);
	}
	
	private void orderFoodIfInventoryLow() {
		
		if (CityDirectory.getInstance().getMarkets().size() == 0) {
			// There aren't any Markets in this city, so we're s#!& out of luck
			return;
		}
		
		// Choose a Market to order from (we'll just choose a random one for now)
		// TODO make this not random
		MarketStructure market = CityDirectory.getInstance().getRandomMarket();
		
		// Go through each item in the inventory and make a call to the market if we're low
		Iterator it = inventory.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			MyFood thisFood = (MyFood)pairs.getValue();
			
			// If our food inventory is less than the low threshold, order more food
			if (thisFood.quantity <= thisFood.lowThreshold && thisFood.orderedMore == false) {
				// Calculate how much we need
				int amountNeeded = thisFood.normalAmount - thisFood.quantity;
				
				// Give the manager our order
				market.getManager().msgHereIsMyOrderForDelivery(restaurant, new ItemRequest(thisFood.type, amountNeeded));
				
				// Mark down that we've ordered
				thisFood.orderedMore = true;
			}
		}
	}
	
	private void takeOrderOffStand() {
		// Grab the order from the stand
		RotatingStandOrderBen standOrder = stand.removeOrder();
		
		// Convert it to our own Order
		Order newOrder = new Order(standOrder.waiter, standOrder.choice, standOrder.table, OrderState.pending);
		
		// Add the order to our list
		orders.add(newOrder);
		
		// Let's go ahead and start cooking it
		attemptToCookOrder(newOrder);
	}
	
	private void plateIt(Order order) {
		// Animate the plating of the food
		doPlating(order);
		
		// Let the waiter know that the food is done
		order.waiter.msgOrderReady(order.choice, order.table);
		
		// Mark the order as plated
		order.state = OrderState.plated;
	}
	
	private void doCooking(Order order) {
		animPanel.cookingArea.addItem(order.choice == "Chicken" ? "C" : "ST");
	}
	
	private void doPlating(Order order) {
		animPanel.cookingArea.removeItem(order.choice == "Chicken" ? "C" : "ST");
		animPanel.platingArea.addItem(order.choice == "Chicken" ? "C" : "ST");
	}
	
	private void LeaveRestaurant() {
		this.isActive = false;
		this.myPerson.goOffWork();
		this.myPerson.removeRole(this);
		this.myPerson = null;
	}

	/**
	 * Utilities
	 */
	
	public String getName() {
		return name;
	}
	
	// A class to keep track of the orders we receive.
	private enum OrderState {pending, cooking, done, plated};
	private class Order {
		WaiterBen waiter;
		String choice;
		int table;
		OrderState state;
		
		public Order(WaiterBen w, String c, int t, OrderState s) {
			waiter = w;
			choice = c;
			table = t;
			state = s;
		}
	}
	
	private class MyFood {
		String type;
		int cookingTime;
		int quantity;
		int lowThreshold;
		int normalAmount;
		Boolean orderedMore = false;
		
		public MyFood(String t, int cT, int q, int lT, int nA) {
			type = t;
			cookingTime = cT;
			quantity = q;
			lowThreshold = lT;
			normalAmount = nA;
		}
	}
	
	public class MarketOrder {
		public class Item {
			String type;
			int quantity;
			public Item(String t, int q) {
				type = t;
				quantity = q;
			}
		}
		public List<Item> items = new ArrayList<Item>();
		public CookBen cook;
		public CashierBen cashier;
		public void addItem(Item theItem) {
			items.add(theItem);
		}
	}

	@Override
	public void addMarket(MarketStructure m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startInteraction(Intention intent) {
		closingTime = false;
	}
	
}

