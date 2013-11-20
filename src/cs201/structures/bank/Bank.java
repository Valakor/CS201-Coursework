package cs201.structures.bank;

import java.util.List;

import cs201.roles.bankRoles.BankGuardRole;
import cs201.roles.bankRoles.BankTellerRole;
import cs201.structures.Structure;


public class Bank extends Structure {
	
	//================================================================================
    // Member Variables
    //================================================================================
	
	List<BankTellerRole> bankTellers;
	BankGuardRole bankGuard;
	boolean isOpen;
	double bankBalance;
	
	public enum AccountTypes { BUSINESS, PERSONAL }
	HashMap<int actNumber, double balance> personalAccounts;
	HashMap<int actNumber, double balance> businessAccounts;
	
	
	//================================================================================
    // Constructors
    //================================================================================
	
	public Bank(int x, int y, int width, int height, int id) {
	    super(x, y, width, height, id);
	}
	
	//================================================================================
    // Methods
    //================================================================================
	
	// Returns a list of this Bank's tellers
	public List<BankTellerRole> getTellers() {
	    return bankTellers;
	}
	// Returns the bank's guard
	public static BankGuardRole getGuard() {
	    return bankGuard;
	}
	// Returns SimCity201's personal accounts
	public HashMap<int actNumber, double balance> getPersonalAccounts() {
	    return personalAccounts;
	}
	// Returns SimCity201's business accounts
	public HashMap<int actNumber, double balance> getBusinessAccounts() {
	    return businessAccounts;
	}
	// Returns the total money held inside of the Bank (Useful for granting loans)
	public double getBankBalance() {
	    for(int actNum : personalAccounts.keySet()) {
	       bankBalance += personalAccounts.get(actNum);
	    }
	    for(int actNum : businessAccounts.keySet()) {
	       bankBalance += businessAccounts.get(actNum);
	    }
	    return bankBalance;
	}
	// Sets whether this Bank is open or closed
	public void setOpen(boolean open) {
	    isOpen = open;
	}
	// Returns whether or not this Bank is open
	public static boolean getOpen() {
	    return isOpen;
	}
	
}