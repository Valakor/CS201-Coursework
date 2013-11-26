package cs201.roles.housingRoles;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.roles.housing.Landlord;
import cs201.interfaces.roles.housing.Renter;
import cs201.roles.housingRoles.ResidentRole.ResidentState;
import cs201.structures.residence.Residence;

public class RenterRole extends ResidentRole implements Renter {
	LandlordRole myLandlord;
	double amtRentOwed = 0;
	
	public RenterRole() {
		super();
	}
	
	public RenterRole(Residence r) {
		super(r);
	}
	
	// Messages
	
	public void msgRentDueYouOwe (Landlord l, double amt) {
		amtRentOwed += amt;
		stateChanged();
	}

	public void msgRentLateYouOweAdditional (Landlord l, double amt) {
		amtRentOwed += amt;
		stateChanged();
	}
	
	// Scheduler
	
	public boolean pickAndExecuteAnAction() {
		if(state==ResidentState.payingRent) {
			if (amtRentOwed>0) {
				if (getPerson().getMoney()>=amtRentOwed) {
					Do("I have "+getPerson().getMoney()+" on hand and I owe "+amtRentOwed+" for rent.");
					PayRent(myLandlord, amtRentOwed);
					return true;
				}
				else
				{
					//WithdrawMoneyPerson(amtRentOwed-(getPerson().getMoney()));
					Do("I don't have enough money on hand to pay my rent due.");
					return false; //temporary
				}
			}
		}
		return super.pickAndExecuteAnAction();
	}

	//Actions
	
	private void PayRent (Landlord l, double amt) {
		// Any necessary rent pay animation
		l.msgHereIsRentPayment(this, amt);
		amtRentOwed -= amt;
		getPerson().removeMoney(amt);
		Do("I paid my rent of "+amt+".");
	}
	
	@Override
	public void startInteraction(Intention intent) {
		if (intent == Intention.ResidencePayRent){
			state = ResidentState.payingRent;
			stateChanged();
		}
		super.startInteraction(intent);
	}

	
	public void setLandlord(LandlordRole l) {
		myLandlord = l;
	}
	
	public LandlordRole getLandlord() {
		return myLandlord;
	}

}
