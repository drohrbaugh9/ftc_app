package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="RedAuto", group="Competition")
//@Disabled
public class RedAuto extends LinearOpMode {

    // motors
    DcMotor rightBack, leftBack, rightFront, leftFront;
    DcMotor shooter1, shooter2;
    DcMotor[] motors;

    // servos
    Servo ballFeeder, upDown;

    // sensors
    OpticalDistanceSensor ods;
    GyroSensor gyro;

    public void runOpMode() throws InterruptedException {
        Util.sensors = true; Util.servos = true;
        Util.init(this);

        // drive motors
        this.rightBack = Util.rightBack; this.leftBack = Util.leftBack;
        this.rightFront = Util.rightFront; this.leftFront = Util.leftFront;

        motors = new DcMotor[4]; motors[0] = this.rightBack; motors[1] = this.leftBack; motors[2] = this.rightFront; motors[3] = this.leftFront;

        // shooter motors
        this.shooter1 = Util.shooter1; this.shooter2 = Util.shooter2;

        // servos
        this.ballFeeder = Util.ballFeeder;
        this.upDown = Util.upDown;

        // sensors
        this.ods = Util.ods;
        this.gyro = Util.gyro;
        I2C_ColorSensor.init(this);

        Util.resetEncoders(this, motors);

        waitForStart();

        /*double shooterPower = FinalTeleOp.calculateShooterPower();
        shooter1.setPower(shooterPower);
        shooter2.setPower(shooterPower + FinalTeleOp.SHOOTER2_OFFSET);

        Thread.sleep(1000);

        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        AutoUtil.PID_Forward(2500, 0.15, true, gyro);

        Thread.sleep(1000);

        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shoot2();

        AutoUtil.encoderTurnLeft(70, 0.2);

        Thread.sleep(200);

        AutoUtil.PID_Forward(4000, 0.2, true, gyro);

        Thread.sleep(200);

        AutoUtil.encoderTurnRight(60, 0.2);

        Thread.sleep(200);*/

        AutoUtil.PID_Forward(2250, 0.3, false, gyro);

        AutoUtil.encoderSteerForward(1500, 0.3, false);

        AutoUtil.encoderSteerForwardLine(0.5, 0.1, false);

        AutoUtil.encoderSteerForward(240, 0.1, true);

        if (I2C_ColorSensor.beaconIsRedBlue()) {
            AutoUtil.encoderSteerForward(750, 0.1, true);
            upDown.setPosition(Util.BEACON_DOWN);
            AutoUtil.encoderSteerBackward(750, 0.1, true);
        }

        while(opModeIsActive()) Thread.sleep(100);
    }

    private void shoot2() throws InterruptedException {
        ballFeeder.setPosition(Util.SHOOT);

        Thread.sleep(400);

        ballFeeder.setPosition(Util.LOAD);

        Thread.sleep(1000);

        ballFeeder.setPosition(Util.SHOOT);

        Thread.sleep(500);

        shooter1.setPower(0);
        shooter2.setPower(0);
        ballFeeder.setPosition(Util.LOAD);
    }
}
