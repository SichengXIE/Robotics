
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

/**
 * This capping class can make robot your hand clap and return a yes order to next step of robot.
 * We will use sound sensor provided by lego
 * Import class: lejos.hardware.lcd.LCD    lejos.hardware.port.SensorPort
 *               lejos.hardware.sensor.NXTSoundSensor   lejos.robotics.SampleProvider
 *               lejos.robotics.subsumption.Behavior    lejos.utility.Delay.
 * @author sicheng
 * @since 2022.01
 */
public class Clapping implements Behavior{ 
  NXTSoundSensor ss = new NXTSoundSensor(SensorPort.S3);    // Start the sound sensor
  SampleProvider sp = ss.getDBAMode();
  private boolean clapped = false;  // Check if can go to next 
  int i = 0;

  /**
   * TakeControl() is using to check whether this class working successful and can we go next step.
   * It will check main class consequencely, once Main.getNextClap return true, next step wil start
   * 
   * @return return this method to check can calp run
   */

  @Override
  public boolean takeControl() {
    return Main.getNextClap();
  }

  /**
   * This method is actual running method. All running process will be inside. The sound value are 
   * be tested in a quite large room. you can change it to the actual running sound level to your background.
   * It will initialize the program first when you see "clap to continue" on the screen you can clap to continue 
   * 
   */
  @Override
  public void action() {
    clapped = false;   // store a default variable
    float[] samples = new float[1];
    float minSoundLvl = 0f; // The min sound value of room
    float maxSoundLvl = 0.5f;
    LCD.drawString("CLAP TO CONTINUE", 0, 0); // GUI, showing you should clap now
    while (i == 0) { // Once find the clap

      ss.fetchSample(samples, 0);

      if (samples[0] < minSoundLvl || samples[0] > maxSoundLvl) { // Check sound level to judge is it a clap
        clapped = true;
        i++;
        LCD.clear();
      }

    }
    Main.setNextClap(false);

  }
  @Override
  public void suppress() {
    Main.setNextClap(false);

  }
}