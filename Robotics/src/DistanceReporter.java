

import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;


public class DistanceReporter {

	private static EV3UltrasonicSensor us1 = new EV3UltrasonicSensor(SensorPort.S2);

	
	
	public static void main(String[] args) {
		final SampleProvider sp = us1.getDistanceMode();
		while (Button.ENTER.isDown()!= true) {
        	float [] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0); 
            
            
            String distance =  String.valueOf(sample[0] * 100).substring(0, String.valueOf(sample[0] * 100).indexOf(".") + 2);    //     make 0.1234567  into 12.3
            
            if (sample[0] > 2.5) {
            	System.out.println("Too Far or Close");
            	
			}else {
				System.out.println(distance+"   " +sample[0]);
			}    
			Delay.msDelay(100);
        }
	}
	
}