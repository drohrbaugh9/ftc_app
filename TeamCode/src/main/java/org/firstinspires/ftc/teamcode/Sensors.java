/*
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.TouchSensor;

/* maybe try to copy the sensorLEGOTouch.java
to make the double hub work
 */


/**
 * Created by pdutoit on 8/14/17.
 */

/*
@TeleOp(name = "TouchSensorTest", group = "Test")
//@Disabled
public class Sensors extends OpMode {
    TouchSensor MySensor;
    public void init(){
        MySensor = hardwareMap.touchSensor.get("TouchSensor");
    }
   public void loop(){
       telemetry.addData("Touch Sensor Status",MySensor.isPressed());
       telemetry.update();
    }
}
*/
package org.firstinspires.ftc.teamcode;

        import com.qualcomm.robotcore.eventloop.opmode.Disabled;
        import com.qualcomm.robotcore.eventloop.opmode.OpMode;
        import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
        import com.qualcomm.robotcore.hardware.DigitalChannel;
        import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by pdutoit on 8/14/17.
 */
@TeleOp(name = "TouchSensorTest", group = "Test")
@Disabled
public class Sensors extends OpMode {
    TouchSensor MySensor;
    public void init(){
        MySensor = (TouchSensor)hardwareMap.get(DigitalChannel.class, "TouchSensor");
    }
    public void loop(){
        telemetry.addData("Touch Sensor Status",MySensor.isPressed());
        telemetry.update();
    }
}


