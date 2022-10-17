
import javax.sound.sampled.Line;
import lejos.robotics.SampleProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Pose;
import lejos.robotics.subsumption.Behavior;

/**
 * Mapping class can make robot measure the map automatically follow the costumed color user set.
 * "IMPROTANT" The map should be a square because of the hardware of lejos does not support rotated smoothly.
 * 
 * @author sicheng
 * @since 2022.01
 *
 */

public class Mapping implements Behavior {
  public MovePilot curpilot;   // Start pilot 
  PoseProvider poseProvider ;   // Coordinate records

  SampleProvider lt;

  /**
   * Initialize the mapping program and set variables properly
   */

  public Mapping() {

    this.poseProvider = Find.poseProvider;
    this.curpilot = Find.pilot;
    this.lt = Find.xx.getColorIDMode();

  }

  final static float ANGULAR_SPEED = 20; // How fast around corners (degrees/sec)
  final static float LINEAR_SPEED = 50; // How fast in a straight line (mm/sec)
  private boolean mappingComplete = false;



  // Share the lines
  public Line[] lines;



  // Start the EV3colorSensor

  float[] firstLevel = new float[1]; // In order to share
  float[] secondLevel = new float[1];
  float[]thirdLevel = new float[1];
  float[] fourthLevel = new float[1];

  // Pose provider

  /**
   * A getter for main method to get where the lines are so that we can have a basic map
   * 
   * @return an arrarylist
   */

  public Line[] getLines() { // Getter for the lines
    return lines;
  }

  /**
   * From this method, main will know if the mapping is completed
   * 
   * @return return boolean to check if the map is completed
   */

  @Override
  public boolean takeControl() {
    if (Main.nextClap == false) { // Check
      return !(mappingComplete);
    }
    return false;
  }

  /**
   * This method is actual running method.
   * The max and min color depth wil be set inside. The judgment is if the depth of color is inside our range.
   * Robot will follow it. Unless it's going to turn to another direction
   */
  @Override
  public void action() {

    curpilot.setAngularSpeed(ANGULAR_SPEED); // Set turnning speed
    curpilot.setLinearSpeed(LINEAR_SPEED); // Set running speed
    Pose coords = new Pose();
    lt.fetchSample(firstLevel, 0); // The color it detect first

    for (int i = 0; i < 4; i++) {

      lt.fetchSample(secondLevel, 0); // The currently color it scanned right now
      curpilot.forward();
      fourthLevel[0] = firstLevel[0] + 1; // The Max Color
      thirdLevel[0] = firstLevel[0] - 1; // The min Color
      while (secondLevel[0] <= fourthLevel[0] && secondLevel[0] >= thirdLevel[0]) {
        lt.fetchSample(secondLevel, 0);
      }

      if (i == 1) {
        coords = poseProvider.getPose(); //if at top right corner gets pose
      }
      curpilot.rotate(80f);
    }

    Main.setCornerCoord(coords.getLocation());
    mappingComplete = true; // Set the variable to done
    Main.setNextClap(true);
    Main.mapped = true;
  } 


  @Override
  public void suppress() {
  }
}

