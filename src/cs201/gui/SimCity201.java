package cs201.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cs201.gui.structures.restaurant.RestaurantGuiMatt;
import cs201.structures.restaurant.RestaurantMatt;

public class SimCity201 extends JFrame {
	CityPanel cityPanel;
	JPanel buildingPanels;
	CardLayout cardLayout;
	
	public SimCity201() {
		setVisible(true);
		setSize(1000, 500);
		
		setLayout(new BorderLayout());
		
		cityPanel = new CityPanel();
		cityPanel.setPreferredSize(new Dimension(1000, 250));
		cityPanel.setMaximumSize(new Dimension(1000, 250));
		cityPanel.setMinimumSize(new Dimension(1000, 250));
		
		cardLayout = new CardLayout();
		
		buildingPanels = new JPanel();
		buildingPanels.setLayout(cardLayout);
		buildingPanels.setMinimumSize(new Dimension(1000, 250));
		buildingPanels.setMaximumSize(new Dimension(1000, 250));
		buildingPanels.setPreferredSize(new Dimension(1000, 250));
		buildingPanels.setBackground(Color.YELLOW);
		
		// Create initial buildings here and add them to cityPanel and buildingPanels
		RestaurantMatt r = new RestaurantMatt(40, 40, 40, 40, 0);
		RestaurantGuiMatt g = new RestaurantGuiMatt(r, 0, this);
		r.setStructurePanel(g);
		buildingPanels.add(g, "" + 0);
		cityPanel.addStructure(r);
		
		RestaurantMatt r2 = new RestaurantMatt(40, 100, 40, 40, 1);
		RestaurantGuiMatt g2 = new RestaurantGuiMatt(r, 1, this);
		r2.setStructurePanel(g2);
		buildingPanels.add(g2, "" + 1);
		cityPanel.addStructure(r2);
		
		add(BorderLayout.NORTH, cityPanel);
		add(BorderLayout.SOUTH, buildingPanels);
	}
	
	public void displayStructurePanel(StructurePanel bp) {
		cardLayout.show(buildingPanels, bp.getName());
	}
}
