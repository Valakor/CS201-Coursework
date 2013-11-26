package cs201.gui.roles.residence;

import java.awt.Color;
import java.awt.Graphics2D;

import cs201.gui.Gui;
import cs201.roles.housingRoles.ResidentRole;

public class ResidentGui implements Gui {

	private ResidentRole role = null;
	private boolean isPresent;
	
	private final int WIDTH = 20, HEIGHT = 20;
	private final int startX = 0, startY = 250;
	
	private int fridgeX = 0, fridgeY = 0;
	private int exitX = -20, exitY = 0;
	private int bedX = 0, bedY = 0;
	private int tableX = 0, tableY = 0;
	
	private int xPos;
	private int yPos;
	private int xDestination;
	private int yDestination;
	
	boolean animating;
	
	
	public ResidentGui() {
		this(null);
	}
	
	public ResidentGui(ResidentRole newRole) {
		xPos = startX;
		yPos = startY;
		xDestination = xPos;
		yDestination = yPos;
		
		role = newRole;
		isPresent = true;
		animating = false;
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		if (xPos<xDestination) {
			xPos++;
		}
		else if (xPos>xDestination) {
			xPos--;
		}
		
		if (yPos<yDestination) {
			yPos++;
		}
		else if (yPos>yDestination) {
			yPos--;
		}
		
		if (xPos==xDestination && yPos==yDestination && animating == true) {
			role.msgAnimationDone();
			animating = false;
		}
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
	
	public void walkToFridge() {
		//set destination to fridge from residence layout
		xDestination = fridgeX;
		yDestination = fridgeY;
		animating = true;
	}
	
	public void goToBed() {
		//animation to walk to the bed
		xDestination = bedX;
		yDestination = bedY;
		animating = true;
		//animation to get in bed
	}
	
	public void exit() {
		xDestination = exitX;
		yDestination = exitY;
		animating = true;
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return isPresent;
	}
	
	public void setPresent(boolean bool) {
		isPresent = bool;
	}
	
	public void setBed(int x, int y) {
		bedX = x;
		bedY = y;
	}
	
	public void setFridge(int x, int y) {
		fridgeX = x;
		fridgeY = y;
	}
	
	public void setTable(int x, int y) {
		tableX = x;
		tableY = y;
	}
	
	public void setExit(int y) {
		exitY = y;
	}

}
