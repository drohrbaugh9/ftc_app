package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="BlueAuto", group="Competition")
//@Disabled
public class BlueAuto extends LinearOpMode {

    // motors
    DcMotor rightBack, leftBack, rightFront, leftFront;
    DcMotor shooter1, shooter2;
    DcMotor[] motors;

    // servos
    Servo ballFeeder, upDown;

    // sensors
    OpticalDistanceSensor ods;
    GyroSensor gyro;

    // autonomous constants
    final int BEACON_MOVE = 400;
    final double BEACON_POWER = 0.15;

    public void runOpMode() throws InterruptedException {
        Util.colorSensors = true; Util.otherSensors = true; Util.servos = true;
        Util.init(this);

        I2C_ColorSensor.disable();

        // turn on blue LED on Device Interface Module to indicate Blue Auto (and make sure red LED is off)
        DeviceInterfaceModule dim = hardwareMap.deviceInterfaceModule.get("Sensors");
        dim.setLED(1, false);
        dim.setLED(0, true);

        // drive motors
        this.rightBack = Util.rightBack; this.leftBack = Util.leftBack;
        this.rightFront = Util.rightFront; this.leftFront = Util.leftFront;

        motors = new DcMotor[4]; motors[0] = this.rightBack; motors[1] = this.leftBack; motors[2] = this.rightFront; motors[3] = this.leftFront;

        // shooter motors
        this.shooter1 = Util.shooter1; this.shooter2 = Util.shooter2;

        // servos
        this.ballFeeder = Util.ballFeeder;
        this.upDown = Util.upDown;

        // otherSensors
        this.ods = Util.ods;
        this.gyro = Util.gyro;
        //I2C_ColorSensor.init(this);

        Util.resetEncoders(this, motors);

        waitForStart();

        double shooterPower = FinalTeleOp.calculateShooterPower();
        shooter1.setPower(shooterPower);
        shooter2.setPower(shooterPower + FinalTeleOp.SHOOTER2_OFFSET);

        Thread.sleep(500);

        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        AutoUtil.PID_Forward(900, 0.2, true, gyro);

        Thread.sleep(200 + 500);

        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shoot2();

        AutoUtil.PID_Forward(1000, 0.2, true, gyro);

        Thread.sleep(100);

        // consider changing to 110 to mirror RedAuto
        AutoUtil.encoderTurnLeft(100, 0.2);

        Thread.sleep(100);

        AutoUtil.PID_Backward(4100, 0.3, true, gyro);

        Thread.sleep(100);

        AutoUtil.encoderTurnLeft(60, 0.2);

        Thread.sleep(100);

        AutoUtil.PID_Backward(2250, 0.3, false, gyro);

        I2C_ColorSensor.enable();

        AutoUtil.encoderSteerBackward(750, 0.3, false);

        AutoUtil.encoderSteerBackwardLine(0.5, 0.15, false);

        Thread.sleep(100);

        AutoUtil.encoderSteerForward(280, 0.15, true);

        //boolean done = false;
        if (I2C_ColorSensor.beaconIsBlueRed()) {
            // first try
            Util.telemetry("beacon status", "BLUE_RED", true);
            AutoUtil.encoderSteerForward(BEACON_MOVE, 0.2, true);
            AutoUtil.beaconDown(upDown);
            AutoUtil.encoderSteerBackward(BEACON_MOVE * 3 / 4, BEACON_POWER, true);
            Thread.sleep(100);
            AutoUtil.encoderSteerForward(500, BEACON_POWER, true);
            AutoUtil.encoderSteerForward(1500, 0.3, false);
            AutoUtil.beaconUp(upDown);
        } else if (I2C_ColorSensor.beaconIsRedBlue()) {
            Util.telemetry("beacon status", "RED_BLUE", true);
            AutoUtil.encoderSteerBackward(BEACON_MOVE, 0.2, true);
            AutoUtil.beaconDown(upDown);
            AutoUtil.encoderSteerForward(BEACON_MOVE * 3 / 4, BEACON_POWER, true);
            Thread.sleep(100);
            AutoUtil.encoderSteerBackward(BEACON_MOVE * 3 / 4, BEACON_POWER, true);
            AutoUtil.beaconUp(upDown);
            AutoUtil.encoderSteerForward(2000 + BEACON_MOVE, 0.3, false);
        }

        AutoUtil.encoderSteerForwardLine(0.5, 0.15, false);

        AutoUtil.encoderSteerForward(200, 0.15, true);

        if (I2C_ColorSensor.beaconIsBlueRed()) {
            // first try
            Util.telemetry("beacon status", "BLUE_RED", true);
            AutoUtil.encoderSteerForward(BEACON_MOVE, BEACON_POWER, true);
            AutoUtil.beaconDown(upDown);
            AutoUtil.encoderSteerBackward(BEACON_MOVE, BEACON_POWER * 3 / 4, true);
            Thread.sleep(100);
            AutoUtil.encoderSteerForward(BEACON_MOVE, BEACON_POWER * 3 / 4, true);
            AutoUtil.beaconUp(upDown);
            AutoUtil.encoderBackward(BEACON_MOVE * 4, 0.3, true);
        } else if (I2C_ColorSensor.beaconIsRedBlue()) {
            Util.telemetry("beacon status", "RED_BLUE", true);
            AutoUtil.encoderSteerBackward(BEACON_MOVE, BEACON_POWER, true);
            AutoUtil.beaconDown(upDown);
            AutoUtil.encoderSteerForward(BEACON_MOVE, BEACON_POWER * 3 / 4, true);
            Thread.sleep(100);
            AutoUtil.encoderBackward(BEACON_MOVE, BEACON_POWER * 3 / 4, true);
        }

        Util.setAllPowers(0);

        while(opModeIsActive()) Thread.sleep(100);
    }

    private void shoot2() throws InterruptedException {
        ballFeeder.setPosition(Util.SHOOT);

        Thread.sleep(400);

        ballFeeder.setPosition(Util.LOAD);

        Thread.sleep(1300);

        ballFeeder.setPosition(Util.SHOOT);

        Thread.sleep(500);

        shooter1.setPower(0);
        shooter2.setPower(0);
        ballFeeder.setPosition(Util.LOAD);
    }
}