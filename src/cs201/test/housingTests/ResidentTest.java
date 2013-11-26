package cs201.test.housingTests;

import org.junit.Before;
import org.junit.Test;

import cs201.agents.PersonAgent;
import cs201.agents.PersonAgent.Intention;
import cs201.gui.structures.residence.ResidenceAnimationPanel;
import cs201.roles.housingRoles.ResidentRole;
import cs201.roles.housingRoles.ResidentRole.ResidentState;
import cs201.structures.residence.Residence;
import junit.framework.TestCase;

public class ResidentTest extends TestCase {

	ResidentRole resident;
	ResidenceAnimationPanel animationPanel;
	Residence residence;
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		animationPanel = new ResidenceAnimationPanel(0, null);
		residence = new Residence(0, 0, 0, 0, 0, animationPanel, false);
		resident = new ResidentRole();
		resident.setPerson(new PersonAgent("Resident", null));
		resident.setTest(true);
		resident.setResidence(residence);
		residence.setResident(resident);
	}

	@Test
	public void test() {
		assertFalse("Resident scheduler should return false. Nothing to do.", resident.pickAndExecuteAnAction());
		assertEquals("Resident state should be doingNothing.",resident.getState(), ResidentState.doingNothing);
		resident.startInteraction(Intention.ResidenceEat);
		assertEquals("Resident state should be hungry.",resident.getState(), ResidentState.hungry);
		assertTrue("Resident scheduler should return true. Now hungry.", resident.pickAndExecuteAnAction());
	}

}
