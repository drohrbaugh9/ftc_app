package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="WestCoastBluePID_AutoTest", group="Test")
//@Disabled
public class WestCoastBluePID_AutoTest extends LinearOpMode {

    Servo ballFeeder, upDown;
    DcMotor rightBack, leftBack, rightFront, leftFront, shooter1, shooter2;
    GyroSensor gyro;
    OpticalDistanceSensor ods;

    final double SHOOT = 0.4, LOAD = 0.05;

    public void runOpMode() throws InterruptedException {
        Util.init(this);

        I2C_ColorSensor.init(this);

        this.rightBack = Util.rightBack; this.leftBack = Util.leftBack;
        this.rightFront = Util.rightFront; this.leftFront = Util.leftFront;

        ballFeeder = hardwareMap.servo.get("ballFeeder");
        upDown = hardwareMap.servo.get("upDown");

        gyro = hardwareMap.gyroSensor.get("gyro");

        ods = hardwareMap.opticalDistanceSensor.get("ods");

        ballFeeder.setPosition(LOAD);

        upDown.setPosition(0.5);

        shooter1 = hardwareMap.dcMotor.get("shooter1");
        shooter2 = hardwareMap.dcMotor.get("shooter2"); shooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        gyro.resetZAxisIntegrator();

        gyro.calibrate();

        while (gyro.isCalibrating()) Thread.sleep(20);

        waitForStart();

        shooter1.setPower(0.25);
        shooter2.setPower(0.25);

        AutoUtil.moveBackward(1500, 0.15, gyro); // assuming this takes 0.5 second for following sleep()

        Thread.sleep(1500);

        shoot2();

        //AutoUtil.turnRight(100, 0.2, gyro);

        int leftBackPos = leftBack.getCurrentPosition();

        Util.setRightPowers(-0.2);
        Util.setLeftPowers(0.2);

        while (leftBack.getCurrentPosition() - leftBackPos < 1100) Thread.sleep(10);

        Util.setAllPowers(0);

        Thread.sleep(200);

        AutoUtil.moveBackward(3500, 0.2, gyro);

        Thread.sleep(200);

        //AutoUtil.turnRight(37, 0.2, gyro);

        leftBackPos = leftBack.getCurrentPosition();

        Util.setRightPowers(0.2);
        Util.setLeftPowers(-0.2);

        while (leftBack.getCurrentPosition() - leftBackPos > -650) Thread.sleep(10);

        Util.setAllPowers(0);

        Thread.sleep(200);

        int rightBackPos = rightBack.getCurrentPosition();

        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        Util.setAllPowers(-0.3);

        while (rightBack.getCurrentPosition() - rightBackPos > -1500) Thread.sleep(20);

        rightBackPos = rightBack.getCurrentPosition();

        Util.setRightPowers(-0.31);
        Util.setLeftPowers(-0.29);

        while (rightBack.getCurrentPosition() - rightBackPos > -1250) Thread.sleep(20);

        Util.setRightPowers(-0.12);
        Util.setLeftPowers(-0.10);

        while (ods.getLightDetected() < 0.50) Thread.sleep(20); // TODO: change to 0.5, 5, check twice

        Util.setAllPowers(0);

        rightBackPos = rightBack.getCurrentPosition();

        Util.setRightPowers(0.15);

        while (rightBack.getCurrentPosition() - rightBackPos < 400) Thread.sleep(10);

        Util.setAllPowers(0);

        if (I2C_ColorSensor.beaconIsRed(I2C_ColorSensor.synch1)) {
            telemetry.addData("beacon status", "RED");
            steerForward(500);
            upDown.setPosition(0.9);
            steerBackward(500);
            Thread.sleep(100);
            steerForward(500);
        } else if (I2C_ColorSensor.beaconIsBlue(I2C_ColorSensor.synch1)) {
            telemetry.addData("beacon status", "BLUE");
            steerBackward(500);
            upDown.setPosition(0.9);
            steerForward(250);
            Thread.sleep(100);
            steerBackward(500);
        }

        telemetry.update();

        upDown.setPosition(0.6);

        leftBackPos = leftBack.getCurrentPosition();

        rightFront.setPower(0.33);
        leftFront.setPower(0.27);
        rightBack.setPower(0.33);
        leftBack.setPower(0.27);

        while (leftBack.getCurrentPosition() < (leftBackPos + 2500)) Thread.sleep(20);

        rightFront.setPower(0.12);
        leftFront.setPower(0.10);
        rightBack.setPower(0.12);
        leftBack.setPower(0.10);

        while (ods.getLightDetected() < 0.50);

        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        leftBack.setPower(0);

        Thread.sleep(500);

        if (I2C_ColorSensor.beaconIsRed(I2C_ColorSensor.synch1)) {
            telemetry.addData("beacon status", "RED");
            steerForward(500);
            upDown.setPosition(0.95);
            steerBackward(750);
            Thread.sleep(100);
            steerForward(500);
        } else if (I2C_ColorSensor.beaconIsBlue(I2C_ColorSensor.synch1)) {
            telemetry.addData("beacon status", "BLUE");
            steerBackward(750);
            upDown.setPosition(0.95);
            steerForward(500);
            Thread.sleep(100);
            steerBackward(500);
        }
    }

    public void shoot2() throws InterruptedException {
        ballFeeder.setPosition(SHOOT);

        Thread.sleep(200);

        ballFeeder.setPosition(LOAD);

        Thread.sleep(1500);

        ballFeeder.setPosition(SHOOT);

        Thread.sleep(300);

        shooter1.setPower(0);
        shooter2.setPower(0);
        ballFeeder.setPosition(LOAD);
    }

    public void forward(int dist) {
        int pos = leftBack.getCurrentPosition();

        Util.setAllPowers(0.3);

        while (leftBack.getCurrentPosition() < (pos + dist)) ;

        Util.setAllPowers(0);
    }

    public void backward(int dist) {
        int pos = leftBack.getCurrentPosition();

        Util.setAllPowers(-0.3);

        while (leftBack.getCurrentPosition() > (pos - dist));

        Util.setAllPowers(0);
    }

    public void steerForward(int dist) {
        int pos = leftBack.getCurrentPosition();

        Util.setRightPowers(0.11);
        Util.setLeftPowers(0.09);

        while (leftBack.getCurrentPosition() < (pos + dist)) ;

        Util.setAllPowers(0);
    }

    public void steerBackward(int dist) {
        int pos = leftBack.getCurrentPosition();

        Util.setRightPowers(-0.11);
        Util.setLeftPowers(-0.09);

        while (leftBack.getCurrentPosition() > (pos - dist));

        Util.setAllPowers(0);
    }
}
