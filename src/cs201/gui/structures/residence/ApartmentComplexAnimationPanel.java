package cs201.gui.structures.residence;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import cs201.gui.Gui;
import cs201.gui.SimCity201;
import cs201.gui.StructurePanel;
import cs201.gui.roles.residence.LandlordGui;

public class ApartmentComplexAnimationPanel extends StructurePanel {

	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	
	private final int deskWidth = 70;
	private final int deskHeight = 40;
	private final int deskX = 250;
	private final int deskY = 100;
	
	private final int chairWidth = 20;
	private final int chairHeight = 20;
	private final int chairX = deskX+(deskWidth/2)-(chairWidth/2);
	private final int chairY = deskX+(deskHeight)+5;
	
	private List<Gui> guis = new ArrayList<Gui>();
	
	public ApartmentComplexAnimationPanel(int i, SimCity201 sc) {
		super(i, sc);
		// TODO Auto-generated constructor stub
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(Color.darkGray);
		g2.fillRect(deskX, deskY, deskWidth, deskHeight);
		
		g2.setColor(Color.WHITE);
		g2.drawString("Desk", deskX, deskY+deskHeight);
		
	    for(Gui gui : guis) {
	        if (gui.isPresent()) {
	            gui.updatePosition();
	            gui.draw(g2);
	        }
	    }

	}

	public void addGui(Gui gui) {
	    guis.add(gui);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
	
	public void informLandlord(LandlordGui lGui) {
		lGui.setChair(chairX, chairY);
	}

}
