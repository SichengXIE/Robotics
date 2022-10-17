
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

/**
 * This class is using to find object in the map and locate where is it. We will use ultrasonic sensor provided by lego.
 * We will set a max detect range at first which means everything outside will not be count as an "Object".
 * Then report it to push interface
 * 
 * @author sichengg
 * @since 2022.01
 *
 */
public class Find implements Behavior {

  static MovePilot pilot;
  static SampleProvider sp;
  static PoseProvider poseProvider;
  static BaseRegulatedMotor mleft;
  static BaseRegulatedMotor mright;
  static EV3ColorSensor xx;
  static Navigator navigator;

  /**
   * An initialize method to prepare the robot
   * 
   */

  public Find() {

    Main m = new Main();			
    pilot = m.pilot;
    sp = m.sp;
    poseProvider = m.poseProvider;
    mleft = m.mleft;
    mright = m.mright;
    xx = m.xx;
    navigator = m.navigator;

  }


  float dis ; // store object distance
  boolean found = false; // Check if anything in the map

  float [] sample = new float[1];
  float [] sample2 = new float[1];


  int angle = 0;  // the total degree rotate so far
  int angle_rotate = 3; // the degree rotate everytime

  boolean NotFoundInRound = false; // when true : after roate 90�� still can not find thing
  boolean ajust = false;   // already ajust or not


  /**
   * This method is actual running method to finding. Once sensor find the item, it will compare distance to check is it in the map. 
   * if yes store it and move to next push.
   * If not, it will keep find the item until a term is finish, move back to original direction scan again
   * 
   * @return boolean found to check if we found the item
   */
  @Override
  public boolean takeControl() {
    if (Main.mapped == true) {
      while (angle < 90) {
        sp.fetchSample(sample, 0);
        pilot.rotate(angle_rotate); // change direction

        angle = angle + angle_rotate;
        if (sample[0] > 0.5) {  // don't forget
          System.out.println("Nothing  "+ poseProvider.getPose().getHeading());
          NotFoundInRound = true;
          found = false;
          return found;}
        else {
          System.out.println("In Range: "+sample[0]);
          pilot.rotate(angle_rotate*-1+1);
          NotFoundInRound = false;
          found = true;
        }}

      if (NotFoundInRound==true) {
        pilot.rotate(-89);   // reset the Heading degree
        NotFoundInRound = false;  // start a new 90�� rotating
        angle = 0;  // reset the total rotate
        System.out.println("Reset Heading:"+poseProvider.getPose().getHeading());}
    }

    return found;              

  }

  @Override
  public void suppress() {}

  @Override
  public void action() {  
    if (ajust == false) {
      ajustion(sample[0]);
    }
    if (found == true && ajust==true) {
      NotFoundInRound = false;
      System.out.println("Pushing");
      found = false; 
      Main.CallPush();
    }
  }

  /**
   * if still not found item in several times it will compare to the nearest item and push that item
   * 
   * @param sample1 the nearest item
   */


  public void ajustion(float sample1) {
    while(found == true&&ajust!=true) {		    //rotate and fetch another sample
      pilot.rotate(0.5);	
      Delay.msDelay(1000);
      sp.fetchSample(sample2, 0);       //if this sample is bigger then rotate back 
      Float diff = sample1-sample2[0];   // should be postive
      System.out.println("Ajusting:"+ diff);  
      if (diff<0f) {      // the sample1 is more better than 2
        pilot.rotate(-7);    
        ajust = false;}      
      if ((diff>0f&&diff<0.11f)||diff==0f) {   
        pilot.rotate(-0.5);   
        ajust = true;
        break;
      }
    }
  }

}


