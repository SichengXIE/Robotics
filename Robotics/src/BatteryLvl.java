import lejos.robotics.subsumption.Behavior;
import lejos.hardware.Battery;

public class BatteryLvl implements Behavior {
	
	Battery battery = new Battery();

	@Override
	public boolean takeControl() {

		if (battery.getBatteryCurrent() < 0.5f) {
			return true;
		}
		return false;
	}

	@Override
	public void action() {
		System.out.println("Sorry, battery low") ;
		if (battery.getBatteryCurrent() < 0.5f) {
			System.exit(0);
		}
		
	}

	@Override
	public void suppress() {}


}
