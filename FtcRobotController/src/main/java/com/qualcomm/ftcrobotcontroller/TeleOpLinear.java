package com.qualcomm.ftcrobotcontroller;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/* FTC Team 9899 TeleOp opmode
 * accepts input from two gamepads which control the...
 *  ...tank drive system
 *  ...arm
 *  ...doors and intake on the arm
 *  ...trigger activators
 * automatically...
 *  ...reduces and then, after 3 seconds of continuous stalling, zeroes all motor powers (stall protection)
 */

public class TeleOpLinear extends LinearOpMode {

    // motors and sensors
    private DcMotor rightBack, leftBack, rightFront, leftFront, arm, intake, hanger;
    private Servo leftDoor, rightDoor, rightTrigger, leftTrigger;
    private GyroSensor gyro;

    // robot status variables
    private int intakeStatus = 0;
    private boolean intakeChanged = false;
    private boolean robotTurning = false;
    private boolean robotGoingForward = false;
    private boolean robotGoingBackward = false;
    private double powerFactor = Util.POWER_LIMIT;
    private float motorRampUpTime;
    private float checkIntervalTime;
    private float hasBeenStalledTime;
    private boolean continuousStall = false;
    private int numStalls = 0;
    protected static boolean stallProtectionGloballyEnabled = false;

    // power constants
    private final double BACK_SCALE = Util.BACK_SCALE;
    private final double POWER_FLOAT = Util.POWER_FLOAT;

    // stall protection limits and thresholds
    private final double MOTOR_POWER_THRESHOLD = 0.9 * Util.POWER_LIMIT;
    private final double TIME_THRESHOLD = 0.3 * Util.SEC_TO_NSEC;
    private final double GYRO_MOUNTAIN_THRESHOLD = -15;
    private final double FORWARD_MIN_STALL_POWER = 0.35;
    private final double BACKWARD_MIN_STALL_POWER = 0.25;
    private final int HAS_BEEN_STALLED_LIMIT = 3;

    //private final double GYRO_FORWARD_TIP_THRESHOLD = 5;

    private View relativeLayout;

    public TeleOpLinear() {
    }

    public void runOpMode() throws InterruptedException {
        // initialization
        Util.init(this);
        StallProtection.init();

        // get motors from shared class
        rightBack = Util.rightBack;
        leftBack = Util.leftBack;
        rightFront = Util.rightFront;
        leftFront = Util.leftFront;
        arm = Util.arm;
        intake = Util.intake;
        hanger = Util.hanger;

        // get servos from shared class
        leftDoor = Util.leftDoor;
        rightDoor = Util.rightDoor;
        rightTrigger = Util.rightTrigger;
        leftTrigger = Util.leftTrigger;

        // get sensor(s) from shared class
        gyro = Util.gyro;

        relativeLayout = ((Activity) hardwareMap.appContext).findViewById(R.id.RelativeLayout);

        waitForStart();

        motorRampUpTime = System.nanoTime();

        while (opModeIsActive()) {

            // drive motors, arm, and hang mechanism
            double r = Util.getGamepadRightJoystickY(gamepad1);
            double l = Util.getGamepadLeftJoystickY(gamepad1);
            double a = -Util.getGamepadRightJoystickY(gamepad2);
            double h = (Util.getGamepadLeftJoystickY(gamepad2) + (gamepad1.right_trigger - gamepad1.left_trigger)) / 2;

            r = scaleDriveJoystick(r);
            l = scaleDriveJoystick(l);
            a = scaleActuatorJoystick(a);

            h = scaleActuatorJoystick(h);
            if (h < 0) h *= 0.7;
            h -= a*0.4;
            h = Range.clip(h, -1, 1);

            robotTurning = (r > 0 && l < 0) || (r < 0 && l > 0);
            robotGoingForward = (r > 0 && l > 0);
            robotGoingBackward = (r < 0 && l < 0);

            adjustDrivePowers(((r + l) / 2) * Util.POWER_LIMIT); // this is mostly stall protection

            this.rightBack.setPower(powerFactor * BACK_SCALE * r);
            this.rightFront.setPower(powerFactor * r);
            this.leftBack.setPower(powerFactor * BACK_SCALE * l);
            this.leftFront.setPower(powerFactor * l);
            this.arm.setPower(a);
            this.hanger.setPower(h);

            // check the gamepad bumpers and set the intake accordingly
            handleIntake();

            // check the gamepad triggers and set the door servos accordingly
            handleDoorServos();

            // if a is pressed on gamepad1, move the front wheels backward and then forward very quickly
			// this is used to shake the robot when it is on the mountain, making dumping faster 
			if (gamepad1.a && r == 0 && l == 0) {
				Util.setFrontPowers(0.35);
                Util.setBackPowers(1 * Util.POWER_LIMIT);
				Thread.sleep(300);
				Util.setAllPowers(0);
			}
			
			// if b is pressed on gamepad2, move the right trigger activator out
            if (gamepad2.b) {
                rightTrigger.setPosition(Util.RIGHT_TRIGGER_OUT);
            }

            // if x is pressed on gamepad2, move the left trigger activator out
            if (gamepad2.x) {
                leftTrigger.setPosition(Util.LEFT_TRIGGER_OUT);
            }

            // if a is pressed on gamepad2, move both trigger activators in
            if (gamepad2.a) {
                rightTrigger.setPosition(Util.RIGHT_TRIGGER_IN);
                leftTrigger.setPosition(Util.LEFT_TRIGGER_IN);
            }

			// if y is pressed on gamepad2, reset the gyro heading
            if (gamepad2.y) {
                AutoUtil.resetGyroHeading(gyro);
                //Util.log("--------- reset gyro heading ------------");
            }

            if (gamepad2.guide && gamepad2.dpad_up) {
                stallProtectionGloballyEnabled = true;
                telemetry.addData("SP globally enabled status", "ON");
                AutoUtil.resetGyroHeading(gyro);
            }

            // tip protection
            /*if (Util.SENSORS && gyro.rawZ() < -2000) {
                Util.setAllPowers(0);
                arm.setPower(0);
                while (Math.abs(gamepad1.right_stick_y) > 0.2 && Math.abs(gamepad1.left_stick_y) > 0.2);
            }*/

            telemetry.addData("gyro heading ", PID.heading(gyro));

            //telemetry.addData("numStalls", numStalls);

            // sleep for 2 ms
            Thread.sleep(2, 0);
        }
    }

    private final double JOYSTICK_DEADZONE_LIMIT = 0.2;
    private final double MIN_POWER = 0.2;
    private final double POWER_EXPONENT = 1.4;

    private double scaleDriveJoystick(double joystickValue) {
        // if the joystick is in the deadzone I defined, return 0
        if (Math.abs(joystickValue) < JOYSTICK_DEADZONE_LIMIT) return 0;
        /* because exponentiation can change signs,
         * store the sign of the joystick position in a separate variable */
        double posOrNeg = 1;
        if (joystickValue < 0) posOrNeg = -1;
        double power = posOrNeg * Math.pow(Math.abs(joystickValue), POWER_EXPONENT);
        if (Math.abs(power) < MIN_POWER) return 0;
        return Range.clip(power, -1, 1);
    }

    private double scaleActuatorJoystick(double joystickValue) {
        // if the joystick is in the deadzone I defined, return 0
        if (Math.abs(joystickValue) < JOYSTICK_DEADZONE_LIMIT) return 0;
        /* because exponentiation can change signs,
         * store the sign of the joystick position in a separate variable */
        double posOrNeg = 1;
        if (joystickValue < 0) posOrNeg = -1;
        // adjust the value 
        double power = posOrNeg * Math.pow(Math.abs(joystickValue), 1 / POWER_EXPONENT);
        if (Math.abs(power) < MIN_POWER) return 0;
        return Range.clip(power, -1, 1);
    }

    private void handleDoorServos() {
        // get the joystick positions
        double ls = gamepad2.left_trigger;
        double rs = gamepad2.right_trigger;

        // scale the joystick input to the servo range
        ls = Util.LEFT_DOOR_MIN + (ls * (Util.LEFT_DOOR_MAX - Util.LEFT_DOOR_MIN));
        rs = Util.RIGHT_DOOR_MAX - (rs * (Util.RIGHT_DOOR_MAX - Util.RIGHT_DOOR_MIN));

        // make sure the value is neither less than the min nor greater than the max
        ls = Range.clip(ls, Util.LEFT_DOOR_MIN, Util.LEFT_DOOR_MAX);
        rs = Range.clip(rs, Util.RIGHT_DOOR_MIN, Util.RIGHT_DOOR_MAX);

        // set the servos to the appropriate position
        leftDoor.setPosition(ls);
        rightDoor.setPosition(rs);
    }

    // intake variables
    private final int OFF = 0, INTAKE = 1, OUTTAKE = 2;

    private void handleIntake() {
        if ((gamepad1.right_bumper || gamepad2.right_bumper) && !intakeChanged) {
            /* if the intake is off, intake
             * if the intake is intaking, outtake
             * if the intake is outtaking, intake
             */
            switch (intakeStatus) {
                case OFF:
                    intake();
                    break;
                case INTAKE:
                    outtake();
                    break;
                case OUTTAKE:
                    intake();
                    break;
            }
            intakeChanged = true;
        }
        if ((gamepad1.left_bumper || gamepad2.left_bumper) && !intakeChanged) {
            /*
             * if the intake is off, outtake
             * if the intake is intaking or outtaking, turn if off
             */
            switch (intakeStatus) {
                case OFF:
                    outtake();
                    break;
                case INTAKE:
                case OUTTAKE:
                    intakeOff();
                    break;
            }
            intakeChanged = true;
        }
        // wait until the user releases all intake-related buttons before allowing the user to change the intake again
        else if (!gamepad1.right_bumper && !gamepad1.left_bumper && !gamepad2.right_bumper && !gamepad2.left_bumper)
            intakeChanged = false;
    }

    // the three following methods standardize intaking, outtaking, and neither
    private void intake() {
        this.intake.setPower(0.6);
        intakeStatus = INTAKE;
    }

    private void outtake() {
        this.intake.setPower(-0.6);
        intakeStatus = OUTTAKE;
    }

    private void intakeOff() {
        this.intake.setPower(0);
        intakeStatus = OFF;
    }

    // adjust the drive powers based on the presence or absence of a stall condition
    private void adjustDrivePowers(double requestedPower) {
        float currentTime = System.nanoTime();
        // store encoder positions from all drive motors
        StallProtection.storeEncoderData();
        // only check for a stall every 0.01 seconds
        if ((currentTime - checkIntervalTime) > (0.01 * Util.SEC_TO_NSEC)) {
            /* check for a stall when stall protection is globally enabled,
             * the driver is requesting enough power,
             * and the robot is not on the mountain, respectively */
            boolean enabled = stallProtectionGloballyEnabled &&
                    (Math.abs(requestedPower) > MOTOR_POWER_THRESHOLD) &&
                    ((PID.heading(gyro)) > GYRO_MOUNTAIN_THRESHOLD);
            if (enabled) {
                // store how much each drive encoder moved in the last 0.01 seconds
                StallProtection.manageEncoderData();
                // check for a stall when the drive motors should be at speed
                if ((currentTime - motorRampUpTime) > TIME_THRESHOLD) {
                    // check for a stall
                    int isStalled = StallProtection.stalled();
                    int tempColor = Color.WHITE;
                    // if the robot is stalled
                    if (isStalled == 1) {
                        //numStalls++;
                        // if the robot did not stall the last time through the loop
                        if (!continuousStall) {
                            // reset the continuous stall timer
                            hasBeenStalledTime = currentTime;
                            // the robot DID stall THIS time through the loop, so...
                            continuousStall = true;
                        }
                        // decrease the power factor
                        powerFactor -= 0.05;
                        tempColor = Color.YELLOW;
                        // if the robot has been continuously stalling for 3 seconds, shut down the motors
                        if (((currentTime - hasBeenStalledTime) / Util.SEC_TO_NSEC) > HAS_BEEN_STALLED_LIMIT) {
                            powerFactor = 0;
                            tempColor = Color.RED;
                        }
                        // keep the drive motors going at the minimum power if it hasn't stalled for three seconds yet
                        else {
                            if (robotGoingForward && (powerFactor < FORWARD_MIN_STALL_POWER)) {
                                powerFactor = FORWARD_MIN_STALL_POWER;
                            } else if (powerFactor < BACKWARD_MIN_STALL_POWER) {
                                powerFactor = BACKWARD_MIN_STALL_POWER;
                            }
                        }
                    }
                    // if the robot is not stalled
                    else if (isStalled == 0) {
                        // increase the power factor
                        powerFactor += 0.02;
                        // keep the drive motors from going over the power limit
                        if (powerFactor > Util.POWER_LIMIT) powerFactor = Util.POWER_LIMIT;
                        // reset the continuous stall timer and boolean, respectively
                        hasBeenStalledTime = currentTime;
                        continuousStall = false;
                    }

                    // change the backround color of the app to reflect stall status
                    /*final int color = tempColor;
                    relativeLayout.post(new Runnable() {
                        public void run() {
                            relativeLayout.setBackgroundColor(color);
                        }
                    });*/

                    // change the background color of the app based on the stall status for debugging purposes
                    switch (tempColor) {
                        case Color.RED:
                            telemetry.addData("stall status", "FULL STALL");
                            break;
                        case Color.YELLOW:
                            telemetry.addData("stall status", "PARTIAL STALL");
                            break;
                        case Color.WHITE:
                            telemetry.addData("stall status", "NO STALL");
                            break;
                    }
                    Util.log("my_debug stall_protection_was ENABLED");
                }
            } else {
                StallProtection.reset();
                // reset the timer that allows the robot to get up to speed before enabling stall protection
                motorRampUpTime = currentTime;
                // if the robot is turning or the driver is requesting enough power, increase the power factor
                if (robotTurning || (Math.abs(requestedPower) > MOTOR_POWER_THRESHOLD)) {
                    powerFactor += 0.02;
                    // keep the drive motors from going over the power limit
                    if (powerFactor > Util.POWER_LIMIT) powerFactor = Util.POWER_LIMIT;
                }
                else {
                    // give the driver a decent amount of power to start with
                    powerFactor = Util.STARTING_POWER;
                }
                // reset the continuous stall timer and boolean, respectively
                hasBeenStalledTime = currentTime;
                continuousStall = false;
            }
            // reset the timer that checks for a stall every 0.01 seconds
            checkIntervalTime = System.nanoTime();
        }
    }
}

