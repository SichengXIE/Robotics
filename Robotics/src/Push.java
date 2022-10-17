

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

/**
 * This class is pushing class. to push item outside of boundary  if we found the item. 
 * It will record how long we have been run and compare the length of map to decide whether we neeed to stop.
 * We will use two motors in this class
 * 
 * @author sicheng
 * @since 2022.02
 *
 */

public class Push implements Behavior {
  MovePilot pilot;
  SampleProvider sp;
  PoseProvider poseProvider;
  BaseRegulatedMotor mleft; // Initialize the motors
  BaseRegulatedMotor mright;

  boolean PushedNeedControl = false; // Stop push
  float Max_X = Main.cornerCoord.x;
  float Max_Y = Main.cornerCoord.y;
  float x_now; // current X-coordinate
  float y_now; // current Y-coordinate

  /**
   * Initialize the robot prepare for push
   */

  public Push() {                                 // using the Sensors from Main() via Find()
    this.pilot = Find.pilot;
    this.sp = Find.sp;
    this.poseProvider = Find.poseProvider;
    this.mleft = Find.mleft;
    this.mright = Find.mright;
  }

  /**
   * Decide whether we should stop the robot. 
   * It will check the coordinate of robot if it outside the boundary.
   * 
   * @return boolean true/false
   */

  @Override
  public boolean takeControl() {

    if (Main.called == true && PushedNeedControl == false) {return true;}
    else if (PushedNeedControl == true) {
      x_now = poseProvider.getPose().getX();
      y_now = poseProvider.getPose().getY();
      return true;
    }else {return PushedNeedControl;}
  }

  /**
   * Actual running method.
   * 
   * If pushedNeedControl didn't be called. It will keep moving forward.
   */

  @Override
  public void action(){
    System.out.println(poseProvider.getPose().toString());
    if (PushedNeedControl == false && Main.called == true) {	
      pilot.forward();   // Moving forward
      PushedNeedControl = true;
      Main.called = false;}
    if (x_now > Max_X + 50 || y_now > Max_Y + 50) {suppress();}  // Defaut distance for the arms of robot
  }

  @Override
  public void suppress() {
    System.out.println("Stopped");

    Main.called =false;
    PushedNeedControl = false;
    Main.pushed = true;

    //Find.mleft.close();
    //Find.mright.close();
    System.exit(0);
    System.out.println(poseProvider.getPose().toString());}


}

