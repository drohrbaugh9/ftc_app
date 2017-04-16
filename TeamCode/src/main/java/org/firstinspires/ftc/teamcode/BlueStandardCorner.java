package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="BlueStandardCorner", group="Competition")
//@Disabled
public class BlueStandardCorner extends LinearOpMode {

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
    double offBeaconPower, onBeaconPower;

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

        AutoLoopTest.driveAndShoot(1600, 2);

        offBeaconPower = AutoUtil.offBeaconPower; onBeaconPower = AutoUtil.onBeaconPower;

        Util.setDriveModeBrake();

        // turn toward the closer beacon and corner vortex
        AutoUtil.rampEncoderTurnLeft(105, 0.5); // 110

        Thread.sleep(100);

        // drive near to the closer beacon
        AutoUtil.PID_Backward(3200, 0.3, false, gyro);
        AutoUtil.PID_Backward(1000, 0.2, true, gyro);

        Thread.sleep(100);

        // turn toward far beacon
        AutoUtil.rampEncoderTurnLeft(57, 0.4);

        Thread.sleep(100);

        // move toward the wall
        AutoUtil.PID_Backward(2250, 0.6, false, gyro);

        // enable the color sensors 'cause we're about to use them
        I2C_ColorSensor.enable();

        // follow the wall...
        AutoUtil.encoderSteerBackward(1850, 0.3, false);

        // ...find the white line...
        if (AutoUtil.encoderSteerBackwardLineSafe(0.5, 0.1, 2650, false) == -1) { // TODO: adjust maxDist value
            Util.telemetry("failsafe", "------FAILSAFE ENGAGED------", true);
            Util.setDriveModeFloat();
            Util.setAllPowers(0);
            while (opModeIsActive()) Thread.sleep(20);
        }
        Util.telemetry("failsafe", "-----FAILSAFE DIDN'T ENGAGE-----", true);

        // ...and center the robot on the beacon
        AutoUtil.encoderSteerBackward(80, 0.1, true);

        /* based on which side is blue, move to that side,
         * lower our button pusher,
         * and roll over the button
         */
        boolean tryAgain = false;
        int frontRed, backRed;
        double frontRatio, backRatio;
        final int TRUE = 1, FALSE = 0, UNKNOWN = -1;

        Util.log("BEACON ----------------far beacon-----------------");
        do {
            tryAgain = !tryAgain;

            double frontRedVal = I2C_ColorSensor.frontRedVal();   Util.log("BEACON frontRed " + frontRedVal);
            double frontBlueVal = I2C_ColorSensor.frontBlueVal(); Util.log("BEACON frontBlue " + frontBlueVal);
            double backRedVal = I2C_ColorSensor.backRedVal();     Util.log("BEACON backRed " + backRedVal);
            double backBlueVal = I2C_ColorSensor.backBlueVal();   Util.log("BEACON backBlue " + backBlueVal);
            frontRatio = frontRedVal / frontBlueVal;
            backRatio = backRedVal / backBlueVal;

            if (frontRatio > 1.1) frontRed = TRUE;
            else if (frontRatio < 0.9) frontRed = FALSE;
            else frontRed = UNKNOWN;

            if (backRatio > 1.1) backRed = TRUE;
            else if (backRatio < 0.9) backRed = FALSE;
            else backRed = UNKNOWN;
        } while (frontRed == UNKNOWN && backRed == UNKNOWN && tryAgain);

        Util.telemetry("frontRatio", frontRatio, false); Util.log("BEACON frontRatio " + frontRatio);
        Util.telemetry("backRatio", backRatio, false);   Util.log("BEACON backRatio " + backRatio);
        Util.telemetry("frontRed", frontRed, false);     Util.log("BEACON frontRed " + frontRed);
        Util.telemetry("backRed", backRed, true);        Util.log("BEACON backRed " + backRed);

        boolean pressFront = false, pressBack = false;
        if ((frontRed == FALSE && backRed != FALSE) || (frontRed != TRUE && backRed == TRUE)) pressFront = true;
        else if ((frontRed != FALSE && backRed == FALSE) || (frontRed == TRUE && backRed != TRUE)) pressBack = true;
        else if (frontRed == TRUE && backRed == TRUE) {
            if (frontRatio > backRatio) pressBack = true;
            else if (backRatio > frontRatio) pressFront = true;
        }

        if (!pressFront && !pressBack) {
            AutoUtil.encoderSteerForward(2800, 0.3, false);
            AutoUtil.beaconUp(upDown);
        } else if (pressFront) {
            AutoUtil.encoderSteerForward(BEACON_MOVE, offBeaconPower, true);
            AutoUtil.beaconDown(upDown);
            AutoUtil.encoderSteerBackward(BEACON_MOVE, onBeaconPower, true);
            Thread.sleep(100);

            AutoUtil.encoderSteerForward(BEACON_MOVE / 2, onBeaconPower, true);
            Thread.sleep(100);
            AutoUtil.encoderSteerBackward(BEACON_MOVE / 2, onBeaconPower, true);
            Thread.sleep(100);

            AutoUtil.encoderSteerForward(BEACON_MOVE, offBeaconPower, false);
            AutoUtil.beaconUp(upDown);
            AutoUtil.encoderSteerForward(2800 - BEACON_MOVE, 0.3, false);
        } else if (pressBack) {
            AutoUtil.encoderSteerBackward(BEACON_MOVE, offBeaconPower, true);
            AutoUtil.beaconDown(upDown);
            AutoUtil.encoderSteerForward(BEACON_MOVE, onBeaconPower, true);
            Thread.sleep(100);

            AutoUtil.encoderSteerBackward(BEACON_MOVE / 2, onBeaconPower, true);
            Thread.sleep(100);
            AutoUtil.encoderSteerForward(BEACON_MOVE / 2, onBeaconPower, true);
            Thread.sleep(100);

            AutoUtil.encoderSteerBackward(BEACON_MOVE, offBeaconPower, true);
            AutoUtil.beaconUp(upDown);
            Thread.sleep(100);
            AutoUtil.encoderSteerForward(2800 + BEACON_MOVE, 0.3, false);
        } else {
            AutoUtil.encoderSteerForward(2800, 0.3, false);
            AutoUtil.beaconUp(upDown);
        }

        // move to the closer beacon
        if (AutoUtil.encoderSteerForwardLineSafe(0.5, 0.1, 3200, false) == -1) {
            Util.telemetry("failsafe", "------FAILSAFE ENGAGED------", true);
            Util.setDriveModeFloat();
            Util.setAllPowers(0);
            while (opModeIsActive()) Thread.sleep(20);
        }
        Util.telemetry("failsafe", "-----FAILSAFE DIDN'T ENGAGE-----", true);

        // center the robot on the beacon
        AutoUtil.encoderSteerForward(100, 0.15, true);

        Thread.sleep(100);

        /* based on which side is blue, move to that side,
         * lower our button pusher,
         * and roll over the button
         */
        tryAgain = false;
        frontRed = 0; backRed = 0;

        Util.log("BEACON ----------------near beacon-----------------");
        do {
            tryAgain = !tryAgain;

            double frontRedVal = I2C_ColorSensor.frontRedVal();   Util.log("BEACON frontRed " + frontRedVal);
            double frontBlueVal = I2C_ColorSensor.frontBlueVal(); Util.log("BEACON frontBlue " + frontBlueVal);
            double backRedVal = I2C_ColorSensor.backRedVal();     Util.log("BEACON backRed " + backRedVal);
            double backBlueVal = I2C_ColorSensor.backBlueVal();   Util.log("BEACON backBlue " + backBlueVal);
            frontRatio = frontRedVal / frontBlueVal;
            backRatio = backRedVal / backBlueVal;

            if (frontRatio > 1.1) frontRed = TRUE;
            else if (frontRatio < 0.9) frontRed = FALSE;
            else frontRed = UNKNOWN;

            if (backRatio > 1.1) backRed = TRUE;
            else if (backRatio < 0.9) backRed = FALSE;
            else backRed = UNKNOWN;
        } while (frontRed == UNKNOWN && backRed == UNKNOWN && tryAgain);

        Util.telemetry("frontRatio", frontRatio, false); Util.log("BEACON frontRatio " + frontRatio);
        Util.telemetry("backRatio", backRatio, false);   Util.log("BEACON backRatio " + backRatio);
        Util.telemetry("frontRed", frontRed, false);     Util.log("BEACON frontRed " + frontRed);
        Util.telemetry("backRed", backRed, true);        Util.log("BEACON backRed " + backRed);

        pressFront = false; pressBack = false;
        if ((frontRed == FALSE && backRed != FALSE) || (frontRed != TRUE && backRed == TRUE)) pressFront = true;
        else if ((frontRed != FALSE && backRed == FALSE) || (frontRed == TRUE && backRed != TRUE)) pressBack = true;
        else if (frontRed == TRUE && backRed == TRUE) {
            if (frontRatio > backRatio) pressBack = true;
            else if (backRatio > frontRatio) pressFront = true;
        }

        if (!pressFront && !pressBack) {
            AutoUtil.encoderSteerBackward(BEACON_MOVE, 0.3, true);
            AutoUtil.beaconUp(upDown);
        } else if (pressFront) {
            AutoUtil.encoderSteerForward(BEACON_MOVE, offBeaconPower, true);
            AutoUtil.beaconDown(upDown);
            AutoUtil.encoderSteerBackward(BEACON_MOVE, onBeaconPower, true);
            Thread.sleep(100);

            AutoUtil.encoderSteerForward(BEACON_MOVE / 2, onBeaconPower, true);
            Thread.sleep(100);
            AutoUtil.encoderSteerBackward(BEACON_MOVE / 2, onBeaconPower, true);
            Thread.sleep(100);

            AutoUtil.encoderSteerForward(BEACON_MOVE, offBeaconPower, true);
            AutoUtil.beaconUp(upDown);
            Thread.sleep(100);
            AutoUtil.encoderSteerBackward(BEACON_MOVE * 2, 0.3, true);
        } else if (pressBack) {
            AutoUtil.encoderSteerBackward(BEACON_MOVE, offBeaconPower, true);
            AutoUtil.beaconDown(upDown);
            AutoUtil.encoderSteerForward(BEACON_MOVE, onBeaconPower, true);
            Thread.sleep(100);

            AutoUtil.encoderSteerBackward(BEACON_MOVE / 2, onBeaconPower, true);
            Thread.sleep(100);
            AutoUtil.encoderSteerForward(BEACON_MOVE / 2, onBeaconPower, true);
            Thread.sleep(100);

            AutoUtil.encoderSteerBackward(BEACON_MOVE, offBeaconPower, true);
            AutoUtil.beaconUp(upDown);
        } else {
            AutoUtil.encoderSteerBackward(BEACON_MOVE, 0.3, true);
            AutoUtil.beaconUp(upDown);
        }

        Util.setDriveModeBrake();

        Thread.sleep(100);

        AutoUtil.encoderSteerForward(1200, 0.05, 1, false);

        Util.setRightPowers(0.6);
        Util.setLeftPowers(0.1);

        Thread.sleep(1300);

        //Util.setDriveModeFloat();

        Util.setAllPowers(0);

        //Thread.sleep(500);
        /*Thread.sleep(100);

        AutoUtil.encoderTurnLeft(85, 0.25);

        Thread.sleep(100);

        Util.setDriveModeFloat();

        AutoUtil.encoderBackward(3700, 0.8, true);

        Thread.sleep(1000);*/

        Util.setDriveModeBrake();

        while(opModeIsActive()) Thread.sleep(100);
    }
}