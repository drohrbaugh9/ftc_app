package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//@TeleOp(name = "Test", group = "Test")
@Autonomous(name = "Test", group = "Test")
//@Disabled
public class TestOpMode extends LinearOpMode {

    public void runOpMode() throws InterruptedException {
        Util.linearOpMode = this;
        I2C_ColorSensor.init(this);

        waitForStart();

        Util.telemetry("frontRed", I2C_ColorSensor.normalizedRed(I2C_ColorSensor.synchFront), false);
        Util.telemetry("frontBlue", I2C_ColorSensor.normalizedBlue(I2C_ColorSensor.synchFront), false);
        Util.telemetry("backRed", I2C_ColorSensor.normalizedRed(I2C_ColorSensor.synchBack), false);
        Util.telemetry("backBlue", I2C_ColorSensor.normalizedBlue(I2C_ColorSensor.synchBack), true);

        while(opModeIsActive()) Thread.sleep(100);
    }
}
