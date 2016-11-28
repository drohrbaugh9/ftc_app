package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp (name = "Tank_Color", group = "Test")
@Disabled
public class Tank_Color extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    byte[] colorCcacheMSB, colorCcacheLSB;
    I2cDevice colorC;
    I2cDeviceSynch colorCreader;

    //OpticalDistanceSensor ods;

    DcMotor right, left;
    Servo upDown;

    double grey;

    public void runOpMode() throws InterruptedException {
        /*colorC = hardwareMap.i2cDevice.get("color");
        colorCreader = new I2cDeviceSynchImpl(colorC, I2cAddr.create8bit(0x3c), false);
        colorCreader.engage();*/

        I2C_ColorSensor.init(this);

        right = hardwareMap.dcMotor.get("right");
        right.setDirection(DcMotor.Direction.REVERSE);
        left = hardwareMap.dcMotor.get("left");
        upDown = hardwareMap.servo.get("upDown");

        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //ods = hardwareMap.opticalDistanceSensor.get("ods");

        waitForStart();

        runtime.reset();
        //colorCreader.write8(3, 1);

        right.setPower(-0.21);
        left.setPower(-0.185);

        Thread.sleep(2750);

        right.setPower(0);
        left.setPower(0);

        Thread.sleep(200);

        right.setPower(0.3);
        left.setPower(0.265);
        Thread.sleep(4500);
        // stop
        right.setPower(0);
        left.setPower(0);

        /* SO HERE'S THE PLAN:
        move steerForward to line up shooter
        shoot
        turn 45 degrees toward beacons
        move steerForward ~4 feet to close to wall
        turn 40 degrees away from beacons
        move steerForward slowly, then follow wall by turning slightly into it
        calibrate sensor as it passes over first white line
        move until close to line
        move steerForward slowly until the line
        check color
        hit appropriate button
        <steps to go back to first beacon here>
        check color
        hit appropriate button
        attempt to park somewhere?
        ("steerForward" does not necessarily mean the front of the robot is moving steerForward)
         */

        //telemetry.addData("light value", ods.getLightDetected());

        while (opModeIsActive()) {
            telemetry.addData("red", I2C_ColorSensor.normalizedRed());
            telemetry.addData("blue", I2C_ColorSensor.normalizedBlue());
            telemetry.addData("upDown", upDown.getPosition());
            telemetry.addData("test", "test");
        }
    }
}