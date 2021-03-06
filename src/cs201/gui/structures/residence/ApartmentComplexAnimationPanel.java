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
import javax.swing.Timer;

import cs201.gui.ArtManager;
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
	private final int chairY = deskY+(deskHeight)-1;
	
	public ApartmentComplexAnimationPanel(int i, SimCity201 sc) {
		super(i, sc);
		// TODO Auto-generated constructor stub
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		Timer timer = new Timer(15, this );
    	timer.start();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY); //clear screen
		
		for (int i=0; i<WINDOWX; i=i+32) {
        	for (int j=0; j<WINDOWY; j=j+32) {
        		g.drawImage(ArtManager.getImage("Apartment_Complex_Floor2"), i, j, 32, 32, null);
        	}
        }
		
		/*g2.setColor(Color.darkGray);
		g2.fillRect(deskX, deskY, deskWidth, deskHeight);*/
		
		g.drawImage(ArtManager.getImage("Apartment_Complex_Desk"), deskX, deskY, 86, 43, null);
		
		/*g2.setColor(Color.GRAY);
		g2.fillRect(chairX, chairY, chairWidth, chairHeight);*/
		
		
		
		g2.setColor(Color.WHITE);
		g2.drawString("Desk", deskX, deskY+13);
		
	    super.paintComponent(g);
	    
	    g.drawImage(ArtManager.getImage("Apartment_Complex_Chair"), chairX-3, chairY+10, 30, 25, null);
	}
	
	public void informLandlord(LandlordGui lGui) {
		lGui.setChair(chairX, chairY);
	}

}
