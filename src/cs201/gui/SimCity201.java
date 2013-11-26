package cs201.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import cs201.agents.PersonAgent;
import cs201.agents.PersonAgent.Intention;
import cs201.gui.structures.market.MarketAnimationPanel;
import cs201.gui.structures.restaurant.RestaurantAnimationPanelMatt;
import cs201.helper.CityDirectory;
import cs201.helper.CityTime;
import cs201.roles.marketRoles.MarketEmployeeRole;
import cs201.roles.marketRoles.MarketManagerRole.ItemRequest;
import cs201.structures.market.MarketStructure;
import cs201.structures.restaurant.RestaurantMatt;

public class SimCity201 extends JFrame {
	private final int SIZEX = 1200;
	private final int SIZEY	= 800;
	
	CityPanel cityPanel;
	JPanel buildingPanels;
	CardLayout cardLayout;
	
	SettingsPanel settingsPanel;
	
	public SimCity201() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setSize(SIZEX, SIZEY);
		
		JPanel guiPanel = new JPanel();
		
		setLayout(new BorderLayout());
		
		guiPanel.setLayout(new BorderLayout());
		
		cityPanel = new CityPanel();
		cityPanel.setPreferredSize(new Dimension(SIZEX * 3/5, SIZEY * 3 / 5));
		cityPanel.setMaximumSize(new Dimension(SIZEX * 3/5, SIZEY * 3 / 5));
		cityPanel.setMinimumSize(new Dimension(SIZEX * 3/5, SIZEY * 3 / 5));
		
		cardLayout = new CardLayout();
		
		buildingPanels = new JPanel();
		buildingPanels.setLayout(cardLayout);
		buildingPanels.setMinimumSize(new Dimension(SIZEX * 2/5, SIZEY * 3 / 5));
		buildingPanels.setMaximumSize(new Dimension(SIZEX * 2/5, SIZEY * 3 / 5));
		buildingPanels.setPreferredSize(new Dimension(SIZEX * 2/5, SIZEY * 3 / 5));
		buildingPanels.setBackground(Color.YELLOW);

		// Create initial buildings here and add them to cityPanel and buildingPanels
		
		JScrollPane cityScrollPane = new JScrollPane(cityPanel);
		
		cityScrollPane.setMinimumSize(new Dimension(SIZEX * 3/5, SIZEY * 3 / 5));
		cityScrollPane.setMaximumSize(new Dimension(SIZEX * 3/5, SIZEY * 3 / 5));
		cityScrollPane.setPreferredSize(new Dimension(SIZEX * 3/5, SIZEY * 3 / 5));
		
		cityScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		cityScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		guiPanel.add(BorderLayout.WEST, cityScrollPane);
		guiPanel.add(BorderLayout.EAST, buildingPanels);
		
		settingsPanel = new SettingsPanel();
		
		settingsPanel.setMinimumSize(new Dimension(SIZEX, SIZEY * 2/5));
		settingsPanel.setMaximumSize(new Dimension(SIZEX, SIZEY * 2/5));
		settingsPanel.setPreferredSize(new Dimension(SIZEX, SIZEY * 2/5));
		
		add(BorderLayout.SOUTH, settingsPanel);
		add(BorderLayout.NORTH, guiPanel);
		
		settingsPanel.addPanel("Restaurants",new ConfigPanel());
		settingsPanel.addPanel("Transit",new TransitConfigPanel());
		settingsPanel.addPanel("Transit",new TransitConfigPanel());
		settingsPanel.addPanel("Banks",new ConfigPanel());
		settingsPanel.addPanel("Markets",new ConfigPanel());
		settingsPanel.addPanel("Housing",new ConfigPanel());
		settingsPanel.addPanel("Housing",new ConfigPanel());
		settingsPanel.addPanel("Housing",new ConfigPanel());
		settingsPanel.addPanel("Restaurants",new ConfigPanel());
		settingsPanel.addPanel("Restaurants",new ConfigPanel());
		
		RestaurantAnimationPanelMatt g = new RestaurantAnimationPanelMatt(0,this);
		RestaurantMatt r = new RestaurantMatt(100,100,50,50,0,g);
		r.setStructurePanel(g);
		r.setClosingTime(new CityTime(10, 30));
		buildingPanels.add(g,""+0);
		cityPanel.addStructure(r);
		CityDirectory.getInstance().addRestaurant(r);
		
		MarketAnimationPanel mG = new MarketAnimationPanel(1,this, 50, 50);
		MarketStructure m = new MarketStructure(225,100,50,50,1,mG);
		m.setStructurePanel(mG);
		buildingPanels.add(mG,""+1);
		cityPanel.addStructure(m);
		CityDirectory.getInstance().addMarket(m);
		
		MarketAnimationPanel mG2 = new MarketAnimationPanel(1,this, 50, 50);
		MarketStructure m2 = new MarketStructure(19*25,9*25,50,50,1,mG2);
		m.setStructurePanel(mG2);
		buildingPanels.add(mG2,""+2);
		cityPanel.addStructure(m2);
		CityDirectory.getInstance().addMarket(m2);
		
		pack();
		CityDirectory.getInstance().startTime();
		
		/*
		 * Delivery Truck testing
		m.addInventory("Pizza", 20, 20);
		m.getManager().msgHereIsMyOrderForDelivery(r, new ItemRequest("Pizza",1));
		m.getManager().pickAndExecuteAnAction();
		((MarketEmployeeRole)m.getEmployees().get(0)).pickAndExecuteAnAction();
		m.getManager().pickAndExecuteAnAction();
		*/
		
		PersonAgent p = new PersonAgent("Cashier");
		p.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCashier, r, null);
		CityDirectory.getInstance().addPerson(p);
		p.startThread();
		
		PersonAgent p2 = new PersonAgent("Cook");
		p2.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantCook, r, null);
		CityDirectory.getInstance().addPerson(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Waiter 1");
		p3.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		CityDirectory.getInstance().addPerson(p3);
		p3.startThread();
		
		PersonAgent p4 = new PersonAgent("Host");
		p4.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantHost, r, null);
		CityDirectory.getInstance().addPerson(p4);
		p4.startThread();
		
		PersonAgent p5 = new PersonAgent("Customer 1");
		p5.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p5);
		p5.startThread();
		
		PersonAgent p6 = new PersonAgent("Waiter 2");
		p6.setupPerson(CityDirectory.getInstance().getTime(), null, r, Intention.RestaurantWaiter, r, null);
		CityDirectory.getInstance().addPerson(p6);
		p6.startThread();
		
		PersonAgent p7 = new PersonAgent("Customer 2");
		p7.setupPerson(CityDirectory.getInstance().getTime(), null, null, null, r, null);
		CityDirectory.getInstance().addPerson(p7);
		p7.startThread();
	}
	
	public void displayStructurePanel(StructurePanel bp) {
		cardLayout.show(buildingPanels, bp.getName());
	}
}
