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
    DcMotor[] driveMotors, shooterMotors;

    // servos
    Servo ballFeeder, upDown;

    // sensors
    OpticalDistanceSensor ods;
    GyroSensor gyro;

    // autonomous constants
    final int BEACON_MOVE = 400;
    final double BEACON_POWER = 0.15;

    // variables to hold motor powers
    double shooter1Power, shooter2Power;

    public void runOpMode() throws InterruptedException {

        Util.colorSensors = true; Util.otherSensors = true; Util.servos = true;
        Util.init(this);

        // disable color sensors to speed up gyro (for now)
        I2C_ColorSensor.disable();

        // turn on blue LED on Device Interface Module to indicate Blue Auto (and make sure red LED is off)
        DeviceInterfaceModule dim = hardwareMap.deviceInterfaceModule.get("Sensors");
        dim.setLED(1, false);
        dim.setLED(0, true);

        // drive motors
        this.rightBack = Util.rightBack; this.leftBack = Util.leftBack;
        this.rightFront = Util.rightFront; this.leftFront = Util.leftFront;

        driveMotors = new DcMotor[4]; driveMotors[0] = this.rightBack; driveMotors[1] = this.leftBack; driveMotors[2] = this.rightFront; driveMotors[3] = this.leftFront;

        // shooter motors
        this.shooter1 = Util.shooter1; this.shooter2 = Util.shooter2;

        shooterMotors = new DcMotor[2]; shooterMotors[0] = this.shooter1; shooterMotors[1] = this.shooter2;

        // servos
        this.ballFeeder = Util.ballFeeder;
        this.upDown = Util.upDown;

        // otherSensors
        this.ods = Util.ods;
        this.gyro = Util.gyro;
        //I2C_ColorSensor.init(this);

        // reset the encoders on the DC motors
        Util.resetEncoders(this, driveMotors);
        Util.resetEncoders(this, shooterMotors);

        ShooterPID.init();

        waitForStart();

        // spin up the shooter motors to a power calculated from the battery voltage
        shooter1Power = FinalTeleOp.calculateShooterPower();
        shooter2Power = shooter1Power + FinalTeleOp.SHOOTER2_OFFSET;
        shooter1.setPower(shooter1Power);
        shooter2.setPower(shooter2Power);

        Thread.sleep(500);

        // make the robot coast to a stop in the next movement
        Util.setDrivePowersFloat();

        // move out from the wall into shooting position
        AutoUtil.PID_Forward(1900, 0.2, true, gyro);

        ShooterPID.fillQueue();

        sleepAndShooterPID(1200);

        //Thread.sleep(200 + 500);

        // make the robot brake when it is set to zero power
        Util.setDrivePowersBrake();

        // accelerate two particles so that they fall into the center vortex
        shoot2();

        // turn toward the closer beacon and corner vortex
        AutoUtil.encoderTurnLeft(105, 0.2); // 110

        Thread.sleep(100);

        // drive near to the closer beacon
        AutoUtil.PID_Backward(4300, 0.3, true, gyro);

        Thread.sleep(100);

        // turn toward far beacon
        AutoUtil.encoderTurnLeft(47, 0.2);

        Thread.sleep(100);

        // move toward the wall
        AutoUtil.PID_Backward(2250, 0.3, false, gyro);

        // enable the color sensors 'cause we're about to use them
        I2C_ColorSensor.enable();

        // follow the wall...
        AutoUtil.encoderSteerBackward(750, 0.3, false);

        // ...find the white line...
        if (AutoUtil.encoderSteerBackwardLineSafe(0.5, 0.15, 3500, false) == -1) { // TODO: adjust maxDist value
            Util.telemetry("failsafe", "------FAILSAFE ENGAGED------", true);
            Util.setDrivePowersFloat();
            Util.setAllPowers(0);
            while (opModeIsActive()) Thread.sleep(20);
        }
        Util.telemetry("failsafe", "-----FAILSAFE DIDN'T ENGAGE-----", true);

        Thread.sleep(100);

        // ...and center the robot on the beacon
        AutoUtil.encoderSteerForward(280, 0.15, true);

        /* based on which side is blue, move to that side,
         * lower our button pusher,
         * and roll over the button
         */
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

        // move to the closer beacon
        if (AutoUtil.encoderSteerForwardLineSafe(0.5, 0.1, 3700, false) == -1) {
            Util.telemetry("failsafe", "------FAILSAFE ENGAGED------", true);
            Util.setDrivePowersFloat();
            Util.setAllPowers(0);
            while (opModeIsActive()) Thread.sleep(20);
        }
        Util.telemetry("failsafe", "-----FAILSAFE DIDN'T ENGAGE-----", true);

        // center the robot on the beacon
        AutoUtil.encoderSteerForward(200, 0.15, true);

        Thread.sleep(100);

        /* based on which side is blue, move to that side,
         * lower our button pusher,
         * and roll over the button
         */
        if (I2C_ColorSensor.beaconIsBlueRed()) {
            // first try
            Util.telemetry("beacon status", "BLUE_RED", true);
            AutoUtil.encoderSteerForward(BEACON_MOVE, BEACON_POWER, true);
            AutoUtil.beaconDown(upDown);
            AutoUtil.encoderSteerBackward(BEACON_MOVE, BEACON_POWER * 3 / 4, true);
            Thread.sleep(100);
            AutoUtil.encoderSteerForward(BEACON_MOVE, BEACON_POWER * 3 / 4, true);
            AutoUtil.beaconUp(upDown);
            //AutoUtil.encoderBackward(BEACON_MOVE * 4, 0.3, true);
        } else if (I2C_ColorSensor.beaconIsRedBlue()) {
            Util.telemetry("beacon status", "RED_BLUE", true);
            AutoUtil.encoderSteerBackward(BEACON_MOVE, BEACON_POWER, true);
            AutoUtil.beaconDown(upDown);
            AutoUtil.encoderSteerForward(BEACON_MOVE, BEACON_POWER * 3 / 4, true);
            Thread.sleep(100);
            AutoUtil.encoderSteerBackward(BEACON_MOVE, BEACON_POWER, true);
        }

        Util.setRightPowers(-0.1);
        Util.setLeftPowers(-0.7);

        Thread.sleep(1200);

        //Util.setDrivePowersFloat();

        Util.setAllPowers(0);

        //Thread.sleep(500);
        Thread.sleep(100);

        AutoUtil.encoderTurnLeft(65, 0.25);

        Thread.sleep(100);

        Util.setDrivePowersFloat();

        AutoUtil.encoderBackward(3300, 0.5, true);

        Thread.sleep(1000);

        Util.setDrivePowersBrake();

        while(opModeIsActive()) Thread.sleep(100);
    }

    private void shoot2() throws InterruptedException {
        ballFeeder.setPosition(Util.SHOOT);

        sleepAndShooterPID(400);

        //Thread.sleep(400);

        ballFeeder.setPosition(Util.LOAD);

        sleepAndShooterPID(1500);

        //Thread.sleep(1300);

        ballFeeder.setPosition(Util.SHOOT);

        sleepAndShooterPID(500);

        //Thread.sleep(500);

        shooter1.setPower(0);
        shooter2.setPower(0);
        ballFeeder.setPosition(Util.LOAD);
    }

    private void sleepAndShooterPID(int sleep) throws InterruptedException {
        long start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
        long currentTime = start, oldTime = start - 10;

        while ((currentTime - start) < sleep) {
            currentTime = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
            ShooterPID.manageEncoderData(currentTime - oldTime);
            double[] powers = ShooterPID.PID_calculateShooterPower(shooter1Power, shooter2Power);
            shooter1Power = powers[0];
            shooter2Power = powers[1];
            shooter1.setPower(shooter1Power);
            shooter2.setPower(shooter2Power);
            oldTime = currentTime;
            Thread.sleep(10);
        }
    }
}