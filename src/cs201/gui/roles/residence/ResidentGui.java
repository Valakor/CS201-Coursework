package cs201.gui.roles.residence;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.Gui;
import cs201.roles.housingRoles.ResidentRole;

public class ResidentGui implements Gui {

	private ResidentRole role = null;
	private boolean isPresent;
	
	private final int WIDTH = 20, HEIGHT = 20;
	private final int startX = 100, startY = 100;
	
	private int xPos;
	private int yPos;
	
	public ResidentGui() {
		this(null);
	}
	
	public ResidentGui(ResidentRole newRole) {
		xPos = startX;
		yPos = startY;
		role = newRole;
		isPresent = true;
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.ORANGE);
		g.fillRect(xPos, yPos, WIDTH, HEIGHT);
		
		g.setColor(Color.WHITE);
		g.drawString("Resident", WIDTH, HEIGHT);
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return isPresent;
	}

}
