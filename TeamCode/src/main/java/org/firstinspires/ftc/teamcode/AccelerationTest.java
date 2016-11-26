package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "Acceleration Test", group = "Test")
//@Disabled
public class AccelerationTest extends LinearOpMode {

    DcMotor rightBack, leftBack, rightFront, leftFront;

    final double MAX_POWER = 0.7, MIN_POWER = 0.3, INCREMENT = 0.06;
    final int WAIT = 40;

    double power = 0;

    public void runOpMode() throws InterruptedException {
        rightBack = hardwareMap.dcMotor.get("rightBack"); rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightFront = hardwareMap.dcMotor.get("rightFront"); rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront = hardwareMap.dcMotor.get("leftFront");

        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        waitForStart();

        while (power < MAX_POWER) {
            rightBack.setPower(power);
            leftBack.setPower(power);
            rightFront.setPower(power);
            leftFront.setPower(power);
            power += INCREMENT;
            Thread.sleep(WAIT);
        }

        Thread.sleep(1500);

        while (power > MIN_POWER) {
            rightBack.setPower(power);
            leftBack.setPower(power);
            rightFront.setPower(power);
            leftFront.setPower(power);
            power -= INCREMENT;
            Thread.sleep(WAIT);
        }

        Thread.sleep(1500);

        rightBack.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        leftFront.setPower(0);
    }

    //public void accelerate(double target) {
    //public void decelerate(double target) {
}
