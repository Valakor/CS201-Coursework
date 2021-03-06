package cs201.gui.structures.restaurant;

import javax.swing.*;

import cs201.helper.Constants;
import cs201.gui.ArtManager;
import cs201.gui.Gui;
import cs201.gui.SimCity201;
import cs201.gui.StructurePanel;
import cs201.gui.roles.restaurant.Brandon.CustomerGuiBrandon;
import cs201.gui.roles.restaurant.Brandon.WaiterGuiBrandon;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class RestaurantAnimationPanelBrandon extends StructurePanel implements ActionListener {

    public static final int TABLE_DIMEN_Y = 50;
    public static final int TABLE_DIMEN_X = 50;
	private static final int FRAME_RATE = 100;
	private static final int REFRESH_RATE = Constants.ANIMATION_SPEED;
	public static final int CASHIER_X = 150;
	public static final int CASHIER_Y = 50;

	public static final int DA_CHILL_ZONE_X = 150;
	public static final int DA_CHILL_ZONE_Y = 150;

	public static final int CUSTOMER_WAITING_AREA_X = 15;
	public static final int CUSTOMER_WAITING_AREA_Y = 15;
	
	public static final int WAITING_AREA_WIDTH = 50;
	public static final int WAITING_AREA_HEIGHT = 50;
	
	public static final int WAITER_REST_X = 15;
	public static final int WAITER_REST_Y = 75;
	
	public static final int WAITER_REST_WIDTH = 50;
	public static final int WAITER_REST_HEIGHT = 75;
	
	public static final int KITCHEN_X = 200;
	public static final int KITCHEN_Y = -20;
	
	public static final int WINDOWX = 500;
	public static final int WINDOWY = 500;
	public static final int HOST_X = 75;
	public static final int HOST_Y = 15;
	
    private Dimension bufferSize;
        
    private HashMap<Integer,Dimension> tables = new HashMap<Integer,Dimension>();

    private boolean paused = false;
    
    public RestaurantAnimationPanelBrandon(int instance, SimCity201 city)
    {
    	super(instance,city);
    	bufferSize = new Dimension(WINDOWX,WINDOWY);
    	tables.put(1,new Dimension(200,200));
    	tables.put(2,new Dimension(100,200));
    	tables.put(3,new Dimension(100,100));
    	tables.put(4,new Dimension(200,100));
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        if(Constants.DEBUG_MODE)
        {
	        //Clear the screen by painting a rectangle the size of the frame
	        g2.setColor(getBackground());
	        g2.fillRect(0,0,bufferSize.width,bufferSize.height);
	        	        
	        for(Integer i : tables.keySet())
	        {	        	
	        	g2.setColor(Color.ORANGE);
	        	
	        	g2.fillRect(tables.get(i).width, tables.get(i).height, TABLE_DIMEN_X, TABLE_DIMEN_Y);
	        	
	        	g2.setColor(Color.BLACK);
	        	
	        	g2.drawString(""+i,tables.get(i).width,tables.get(i).height+TABLE_DIMEN_Y);
	        }
	        
	        g2.setColor(Color.GREEN);
	        g2.fillRect(CASHIER_X,CASHIER_Y,TABLE_DIMEN_X,TABLE_DIMEN_Y);
	        g2.setColor(Color.BLACK);
	        g2.drawString("CASHIER",CASHIER_X,CASHIER_Y);
	        
	        g2.setColor(Color.RED);
	        g2.fillRect(CUSTOMER_WAITING_AREA_X, CUSTOMER_WAITING_AREA_Y, WAITING_AREA_WIDTH, WAITING_AREA_HEIGHT);
	        g2.fillRect(WAITER_REST_X,WAITER_REST_Y,WAITER_REST_WIDTH,WAITER_REST_HEIGHT);
	    }
        else
        {
        	g2.drawImage(ArtManager.getImage("Restaurant_Brandon_Floor"),0,0,this);
	        
	        for(Integer i : tables.keySet())
	        {
	        	g2.drawImage(ArtManager.getImage("Restaurant_Brandon_Table"), tables.get(i).width, tables.get(i).height, TABLE_DIMEN_X, TABLE_DIMEN_Y,this);
	        }
	        
	        g2.drawImage(ArtManager.getImage("Restaurant_Brandon_Cash_Register"), CASHIER_X,CASHIER_Y+20,TABLE_DIMEN_X,(int)((1.0*TABLE_DIMEN_X/56)*16), this);
	        g2.drawImage(ArtManager.getImage("Restaurant_Brandon_Rug"), CUSTOMER_WAITING_AREA_X, CUSTOMER_WAITING_AREA_Y, WAITING_AREA_WIDTH, WAITING_AREA_HEIGHT,this);
	        g2.drawImage(ArtManager.getImage("Restaurant_Brandon_Rug"), WAITER_REST_X,WAITER_REST_Y,WAITER_REST_WIDTH,WAITER_REST_HEIGHT,this);
        }
        
        super.paintComponent(g2);
    }
    
    public void addTable(int x, int y)
    {
    	tables.put(tables.size()+1,new Dimension(x,y));
    }
    
    public HashMap<Integer,Dimension> getTables()
    {
    	return tables;
    }
}
