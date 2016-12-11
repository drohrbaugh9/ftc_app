package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="ShooterAutoTest", group="Test")
@Disabled
public class ShooterAutoTest extends LinearOpMode {

    Servo ballFeeder, upDown;
    DcMotor shooter1, shooter2;

    final double SHOOT = 0.4, LOAD = 0.05;

    public void runOpMode() throws InterruptedException {
        ballFeeder = hardwareMap.servo.get("ballFeeder");
        upDown = hardwareMap.servo.get("upDown");

        ballFeeder.setPosition(LOAD);

        upDown.setPosition(0.6);

        shooter1 = hardwareMap.dcMotor.get("shooter1");
        shooter2 = hardwareMap.dcMotor.get("shooter2"); shooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        waitForStart();

        shooter1.setPower(0.27);
        shooter2.setPower(0.27);

        Thread.sleep(2000);

        ballFeeder.setPosition(SHOOT);

        Thread.sleep(200);

        ballFeeder.setPosition(LOAD);

        Thread.sleep(1000);

        ballFeeder.setPosition(SHOOT);

        Thread.sleep(300);

        shooter1.setPower(0);
        shooter2.setPower(0);
        ballFeeder.setPosition(LOAD);
    }
}
