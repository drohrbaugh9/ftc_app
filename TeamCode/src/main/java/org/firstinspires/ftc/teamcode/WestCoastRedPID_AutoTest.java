package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="WestCoastRedPID_AutoTest", group="Test")
//@Disabled
public class WestCoastRedPID_AutoTest extends LinearOpMode {

    Servo ballFeeder, upDown;
    DcMotor shooter1, shooter2;
    GyroSensor gyro;

    final double SHOOT = 0.4, LOAD = 0.05;

    public void runOpMode() throws InterruptedException {
        Util.init(this);

        ballFeeder = hardwareMap.servo.get("ballFeeder");
        upDown = hardwareMap.servo.get("upDown");

        gyro = hardwareMap.gyroSensor.get("gyro");

        ballFeeder.setPosition(LOAD);

        upDown.setPosition(0.6);

        shooter1 = hardwareMap.dcMotor.get("shooter1");
        shooter2 = hardwareMap.dcMotor.get("shooter2"); shooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        gyro.resetZAxisIntegrator();

        gyro.calibrate();

        while (gyro.isCalibrating());

        waitForStart();

        shooter1.setPower(0.27);
        shooter2.setPower(0.27);

        AutoUtil.moveBackward(1600, 0.15, gyro); // assuming this takes 0.5 second for following sleep()

        Thread.sleep(1500);

        shoot2();

        AutoUtil.turnRight(100, 0.2, gyro);

        Thread.sleep(200);

        AutoUtil.moveForward(3500, 0.2, gyro);

        Thread.sleep(200);

        AutoUtil.turnRight(45, 0.2, gyro);

        Thread.sleep(200);

        //AutoUtil.moveForward(5040, 0.3, gyro); // add steering

        /*
        * go slow and look for line
        * do 1st beacon
        * back up, still steering into wall at 0.3 power til close to line
        * go slow and look for line
        * do 2nd beacon
        */
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
}
