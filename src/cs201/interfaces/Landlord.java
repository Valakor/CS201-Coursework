package cs201.interfaces;

import cs201.structures.Residence;

public interface Landlord {

	//messages
	
	public abstract void msgHereIsRentPayment(Renter r, double amt);
	public abstract void msgPropertyNeedsMaintenance(Renter r, Residence res);
	
}