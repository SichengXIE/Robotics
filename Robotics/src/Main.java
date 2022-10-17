
import java.awt.Button;
import java.awt.Point;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * This class make sure all program will run successfully and in order.
 * 
 * @author sicheng
 * @since 2022.03
 *
 */


public class Main {
  final static float WHEEL_DIAMETER = 56; // The diameter (mm) of the wheels
  final static float AXLE_LENGTH = 125; // The distance (mm) your two driven wheels
  final static float ANGULAR_SPEED = 100; // How fast around corners (degrees/sec)
  final static float LINEAR_SPEED = 70; // How fast in a straight line (mm/sec)
  BaseRegulatedMotor mleft = new EV3LargeRegulatedMotor(MotorPort.A);
  BaseRegulatedMotor mright = new EV3LargeRegulatedMotor(MotorPort.B);
  Wheel Left = WheeledChassis.modelWheel(mleft, WHEEL_DIAMETER).offset(-AXLE_LENGTH / 2);
  Wheel Right = WheeledChassis.modelWheel(mright, WHEEL_DIAMETER).offset(AXLE_LENGTH / 2);
  Chassis chassis = new WheeledChassis((new Wheel[] {Right,Left}), WheeledChassis.TYPE_DIFFERENTIAL);
  MovePilot pilot = new MovePilot(chassis);
  PoseProvider poseProvider = new OdometryPoseProvider(pilot);
  Navigator navigator = new Navigator(pilot, poseProvider);
  EV3UltrasonicSensor us1 = new EV3UltrasonicSensor(SensorPort.S2);
  SampleProvider sp = us1.getDistanceMode();
  EV3ColorSensor xx = new EV3ColorSensor(SensorPort.S1); 


  static Point cornerCoord = new Point(300f, 300f);     // test
  static boolean called = false;     //for Push()
  static boolean nextClap = true;
  static boolean mapped = false;         // Find() start after mapped
  static boolean pushed = false;

  /**
   * this method will call push interface to run and start pushing
   */

  public static void CallPush() {
    called = true;
  }

  /**
   * This method will check is it time to turn the direction
   * 
   * @param corner the corner data was collected by machine automatically and store in cornerCoord
   */

  public static void setCornerCoord(Point corner) {
    cornerCoord = corner;
  }

  /**
   * Construct method for robot to know he has to move to next step
   * 
   * @return a boolean to check, true move to next, else not move
   */

  public static boolean getNextClap() {
    return nextClap;
  }

  /**
   * Start clap interface after start
   * 
   * @param clap
   */

  public static void setNextClap(boolean clap) {
    nextClap = clap;
  }

  /**
   * main running method to organize everything here
   * 
   * @param args
   */

  public static void main(String [] args) {
    System.out.println("Welcome! Authors:");
    System.out.println("Sicheng Axie");
    System.out.println("Oscar Cowper");
    System.out.println("Julius Ssali");
    System.out.println("Yiming You");
    System.out.println("Version: 24/3/22");
    System.out.println("Press to Continue");   //start screen
    Button.ENTER.waitForPressAndRelease();
    System.out.println("");
    System.out.println("");
    System.out.println("");
    System.out.println("");
    System.out.println("");
    System.out.println("");
    System.out.println("");
    Behavior clapping = new Clapping(); // Start the interfaces
    Behavior find = new Find();
    Behavior push = new Push();	
    Behavior map = new Mapping();

    Behavior [] bArray = {find, push, map, clapping};
    Arbitrator arby = new Arbitrator(bArray);
    arby.go();

  }

}