package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous (name = "WestCoast_AutoTest", group = "Test")
@Disabled
public class WestCoast_AutoTest extends LinearOpMode {

    int pos = 0;

    final boolean RED_ALLIANCE = true;

    DcMotor rightFront, leftFront, rightBack, leftBack;
    DcMotor[] motors;

    Servo upDown;

    OpticalDistanceSensor ods;

    public void runOpMode() throws InterruptedException {
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        leftBack = hardwareMap.dcMotor.get("leftBack");

        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        motors = new DcMotor[1]; motors[0] = leftBack;

        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        Util.resetEncoders(this, motors);

        I2C_ColorSensor.init(this);

        upDown = hardwareMap.servo.get("upDown");

        upDown.setPosition(0.6);

        ods = hardwareMap.opticalDistanceSensor.get("ods");

        waitForStart();

        backward(1100);

        Thread.sleep(1200);

        pos = leftBack.getCurrentPosition();

        rightFront.setPower(-0.3);
        leftFront.setPower(0.3);
        rightBack.setPower(-0.3);
        leftBack.setPower(0.3);

        while (leftBack.getCurrentPosition() < (pos + 1500));

        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        leftBack.setPower(0);

        Thread.sleep(200);

        forward(6000);

        Thread.sleep(200);

        pos = leftBack.getCurrentPosition();

        rightFront.setPower(-0.3);
        leftFront.setPower(0.3);
        rightBack.setPower(-0.3);
        leftBack.setPower(0.3);

        while (leftBack.getCurrentPosition() < (pos + 250));

        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        leftBack.setPower(0);

        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        Thread.sleep(200);

        pos = leftBack.getCurrentPosition();

        rightFront.setPower(0.33);
        leftFront.setPower(0.27);
        rightBack.setPower(0.33);
        leftBack.setPower(0.27);

        while (leftBack.getCurrentPosition() < (pos + 2500));

        rightFront.setPower(0.11);
        leftFront.setPower(0.09);
        rightBack.setPower(0.11);
        leftBack.setPower(0.09);

        telemetry.addData("ods", ods.getLightDetected());
        telemetry.update();

        while (ods.getLightDetected() < 0.50);

        telemetry.addData("ods", ods.getLightDetected());
        telemetry.update();

        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        leftBack.setPower(0);

        Thread.sleep(200);

        if (RED_ALLIANCE && I2C_ColorSensor.beaconIsRed(I2C_ColorSensor.synch1)) {
            telemetry.addData("beacon status", "RED");
            steerBackward(500);
            upDown.setPosition(0.9);
            steerForward(250);
            Thread.sleep(100);
            steerBackward(500);
        } else if (RED_ALLIANCE && I2C_ColorSensor.beaconIsBlue(I2C_ColorSensor.synch1)) {
            telemetry.addData("beacon status", "BLUE");
            steerForward(500);
            upDown.setPosition(0.9);
            steerBackward(500);
            Thread.sleep(100);
            steerForward(500);
        } /*else if (!RED_ALLIANCE && I2C_ColorSensor.beaconIsRed()) {
            telemetry.addData("beacon status", "RED");
            steerForward(500);
            upDown.setPosition(0.9);
            steerBackward(500);
        } else if (!RED_ALLIANCE && I2C_ColorSensor.beaconIsBlue()) {
            telemetry.addData("beacon status", "BLUE");
            steerBackward(500);
            upDown.setPosition(0.9);
            steerForward(500);
        }*/

        upDown.setPosition(0.6);

        pos = leftBack.getCurrentPosition();

        rightFront.setPower(-0.33);
        leftFront.setPower(-0.27);
        rightBack.setPower(-0.33);
        leftBack.setPower(-0.27);

        while (leftBack.getCurrentPosition() > (pos - 2500)) ;

        rightFront.setPower(-0.11);
        leftFront.setPower(-0.09);
        rightBack.setPower(-0.11);
        leftBack.setPower(-0.09);

        while (ods.getLightDetected() < 0.50);

        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        leftBack.setPower(0);

        Thread.sleep(100);

        steerForward(300);

        Thread.sleep(100);

        if (RED_ALLIANCE && I2C_ColorSensor.beaconIsRed(I2C_ColorSensor.synch1)) {
            telemetry.addData("beacon status", "RED");
            steerBackward(750);
            upDown.setPosition(0.95);
            steerForward(500);
            Thread.sleep(100);
            steerBackward(500);
        } else if (RED_ALLIANCE && I2C_ColorSensor.beaconIsBlue(I2C_ColorSensor.synch1)) {
            telemetry.addData("beacon status", "BLUE");
            steerForward(500);
            upDown.setPosition(0.95);
            steerBackward(750);
            Thread.sleep(100);
            steerForward(500);
        } /*else if (!RED_ALLIANCE && I2C_ColorSensor.beaconIsRed()) {
            telemetry.addData("beacon status", "RED");
            steerForward(500);
            upDown.setPosition(0.9);
            steerBackward(500);
        } else if (!RED_ALLIANCE && I2C_ColorSensor.beaconIsBlue()) {
            telemetry.addData("beacon status", "BLUE");
            steerBackward(500);
            upDown.setPosition(0.9);
            steerForward(500);
        }*/

        /*while (opModeIsActive()) {
            telemetry.addData("ods", ods.getLightDetected());
            telemetry.update();
        }*/
    }

    public void forward(int dist) {
        pos = leftBack.getCurrentPosition();

        rightBack.setPower(0.3);
        leftBack.setPower(0.3);
        rightFront.setPower(0.3);
        leftFront.setPower(0.3);

        while (leftBack.getCurrentPosition() < (pos + dist)) ;

        rightBack.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        leftFront.setPower(0);
    }

    public void backward(int dist) {
        pos = leftBack.getCurrentPosition();

        rightBack.setPower(-0.3);
        leftBack.setPower(-0.3);
        rightFront.setPower(-0.3);
        leftFront.setPower(-0.3);

        while (leftBack.getCurrentPosition() > (pos - dist));

        rightBack.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        leftFront.setPower(0);
    }

    public void steerForward(int dist) {
        pos = leftBack.getCurrentPosition();

        rightBack.setPower(0.11);
        leftBack.setPower(0.09);
        rightFront.setPower(0.11);
        leftFront.setPower(0.09);

        while (leftBack.getCurrentPosition() < (pos + dist)) ;

        rightBack.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        leftFront.setPower(0);
    }

    public void steerBackward(int dist) {
        pos = leftBack.getCurrentPosition();

        rightBack.setPower(-0.11);
        leftBack.setPower(-0.09);
        rightFront.setPower(-0.11);
        leftFront.setPower(-0.09);

        while (leftBack.getCurrentPosition() > (pos - dist));

        rightBack.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        leftFront.setPower(0);
    }
}
