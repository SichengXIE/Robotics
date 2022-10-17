
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

public class Reset extends Mapping implements Behavior {
	Navigator navigator;
	
	public Reset() {
		this.navigator = Find.navigator;
	}
	private boolean Resetcomplete = false;
    
    @Override
    public boolean takeControl() {
    	if (Main.pushed == true) {
    		System.out.println("Resetting ...");
    		return true;
		}
    	return Resetcomplete;
    }
    
    @Override
	public void action() {         
    	//start going
		navigator.goTo(0, 0, 0);
    	
		// Degree
		while(super.firstLevel[0] != super.secondLevel[0]) { // When the color cannot find the line
			super.curpilot.rotate(3); // machine keep rotate
		}
		
		System.exit(0);
	}
    
    @Override
    public void suppress() {
    }
}

 