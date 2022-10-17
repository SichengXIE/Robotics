import lejos.hardware.Button;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class FindDemo {
	final static float WHEEL_DIAMETER = 50; // The diameter (mm) of the wheels
    final static float AXLE_LENGTH = 125; // The distance (mm) your two driven wheels
    final static float ANGULAR_SPEED = 100; // How fast around corners (degrees/sec)
    final static float LINEAR_SPEED = 70; // How fast in a straight line (mm/sec)

	
	public static void main(String[] args) {
	    BaseRegulatedMotor mleft = new EV3LargeRegulatedMotor(MotorPort.A);
	    BaseRegulatedMotor mright = new EV3LargeRegulatedMotor(MotorPort.B);
	    Wheel Left = WheeledChassis.modelWheel(mleft, WHEEL_DIAMETER).offset(-AXLE_LENGTH / 2);
	    Wheel Right = WheeledChassis.modelWheel(mright, WHEEL_DIAMETER).offset(AXLE_LENGTH / 2);
	    Chassis chassis = new WheeledChassis((new Wheel[] {Right,Left}), WheeledChassis.TYPE_DIFFERENTIAL);
	    MovePilot pilot = new MovePilot(chassis);
	    
	    EV3UltrasonicSensor us1 = new EV3UltrasonicSensor(SensorPort.S2);
		final SampleProvider sp = us1.getDistanceMode();
		float [] sample = new float[sp.sampleSize()];	
		
		
		
		int angle = 0;
    	int angle_rotate = 5;
     	while (angle < 90) {
     		pilot.rotate(angle_rotate);
     		sp.fetchSample(sample, 0);
     		String distance =  String.valueOf(sample[0] * 100).substring(0, String.valueOf(sample[0] * 100).indexOf(".") + 2);    //     make 0.1234567  into 12.3
     		
     		if (sample[0] > 0.1) {  //2.5
     			System.out.println("Nothings in Range");
     			angle = angle + angle_rotate;}   //Nothings in Range
     		else {
     			System.out.println(distance+"  Item found");
     			break;}
     		
     		
     	 	}
		
     	Button.ENTER.waitForPressAndRelease();

	}

}
