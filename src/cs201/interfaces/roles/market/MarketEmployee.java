package cs201.interfaces.roles.market;

import java.util.List;

import cs201.roles.marketRoles.MarketManagerRole.Item;

public interface MarketEmployee {

	public void msgRetrieveItems(MarketManager manager, List<Item> items, int id);
	
}