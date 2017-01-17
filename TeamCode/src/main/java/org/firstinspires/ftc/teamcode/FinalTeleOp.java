package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "FinalTeleOp", group = "Competition")
//@Disabled
public class FinalTeleOp extends LinearOpMode {

    private DcMotor rightBack, leftBack, rightFront, leftFront;
    private DcMotor intake, shooter1, shooter2;

    private Servo ballFeeder;

    private OpticalDistanceSensor ods;

    //final String NORMAL = "normal", STRAIGHT = "straight";
    private final double POWER_FACTOR = 1, POSITIVE_STEP = 0.2, NEGATIVE_STEP = 0.5;
    protected static final double SHOOTER2_OFFSET = 0.07;
    private final double INTAKE_POWER = 0.7;
    private final double SHOOT = Util.SHOOT, LOAD = Util.LOAD;
    private final long MILLIS_PER_NANO = 1000000;

    //String driveMode = NORMAL;
    private long shooterStart = System.nanoTime(), shooterLoadTimer = shooterStart;
    private double targetPowerR = 1, targetPowerL = 1, currentR = 1, currentL = 1;
    private boolean shooterStatus = false; //, aHasBeenPressed = false;

    private int intakeStatus = 0;
    private boolean intakeChanged = false;

    public void runOpMode() throws InterruptedException {
        Util.colorSensors = false; Util.otherSensors = true; Util.servos = true;
        Util.init(this);

        this.rightBack = Util.rightBack;
        this.leftBack = Util.leftBack;
        this.rightFront = Util.rightFront;
        this.leftFront = Util.leftFront;

        this.shooter1 = Util.shooter1;
        this.shooter2 = Util.shooter2;

        this.intake = Util.intake;

        this.ballFeeder = Util.ballFeeder;

        this.ods = Util.ods;

        waitForStart();

        while (opModeIsActive()) {

            handleDriveMotors();

            handleIntake();

            handleShooter();

            //telemetry.addData("battery voltage", Util.getBatteryVoltage());

            telemetry.addData("shooter power", shooter1.getPower());

            telemetry.update();

            Thread.sleep(10);
        }
    }

    private void handleDriveMotors() throws InterruptedException {
        if (gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.dpad_left || gamepad1.dpad_right) {
            DpadDrive();
        } else if (Math.abs(gamepad1.right_stick_y) > JOYSTICK_DEADZONE_LIMIT ||
                   Math.abs(gamepad1.left_stick_y) > JOYSTICK_DEADZONE_LIMIT) {
            joystickDrive();
        } else if (gamepad1.a || gamepad1.y) {
            pressBeacon();
        } else {
            Util.setAllPowers(0);
            currentR = 1; currentL = 1;
        }

        /*telemetry.addData("right speed", r);
        telemetry.addData("left speed", l);
        telemetry.update();*/
    }

    final double DpadPower = 0.2;
    final int DpadTime = 100;

    private void DpadDrive() throws InterruptedException {
        if (gamepad1.dpad_up) {
            if (!gamepad1.dpad_right) Util.setLeftPowers(DpadPower);
            if (!gamepad1.dpad_left) Util.setRightPowers(DpadPower);
        } else if (gamepad1.dpad_down) {
            if (!gamepad1.dpad_right) Util.setLeftPowers(-DpadPower);
            if (!gamepad1.dpad_left) Util.setRightPowers(-DpadPower);
        } else if (gamepad1.dpad_right) {
            Util.setRightPowers(-DpadPower);
            Util.setLeftPowers(DpadPower);
        } else if (gamepad1.dpad_left) {
            Util.setRightPowers(DpadPower);
            Util.setLeftPowers(-DpadPower);
        }

        Thread.sleep(DpadTime);
        Util.setAllPowers(0);

        while (gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.dpad_left || gamepad1.dpad_right) Thread.sleep(10);
    }

    private void joystickDrive() {
        double r = Util.getGamepadRightJoystickY(gamepad1);
        double l = Util.getGamepadLeftJoystickY(gamepad1);

        r = scaleDriveJoystick(r);
        l = scaleDriveJoystick(l);

        targetPowerR = r + 1;
        targetPowerL = l + 1;
            /*if (driveMode.equals(NORMAL)) {
                targetPowerL = l + 1;
            } else {
                targetPowerL = r + 1;
            }*/

        if (currentR < (targetPowerR - POSITIVE_STEP)) {
            currentR += POSITIVE_STEP;
        } else if (currentR < targetPowerR) {
            currentR = targetPowerR;
        }

        if (currentR > (targetPowerR + NEGATIVE_STEP)) {
            currentR -= NEGATIVE_STEP;
        } else if (currentR > targetPowerR) {
            currentR = targetPowerR;
        }

        if (currentL < (targetPowerL - POSITIVE_STEP)) {
            currentL += POSITIVE_STEP;
        } else if (currentL < targetPowerL) {
            currentL = targetPowerL;
        }

        if (currentL > (targetPowerL + NEGATIVE_STEP)) {
            currentL -= NEGATIVE_STEP;
        } else if (currentL > targetPowerL) {
            currentL = targetPowerL;
        }

        rightBack.setPower((currentR - 1) * POWER_FACTOR);
        leftBack.setPower((currentL - 1) * POWER_FACTOR);
        rightFront.setPower((currentR - 1) * POWER_FACTOR);
        leftFront.setPower((currentL - 1) * POWER_FACTOR);
    }

    private final double JOYSTICK_DEADZONE_LIMIT = 0.1;
    private final double MIN_POWER = 0.1;
    private final double B = 13.2699, A = 0.0684;

    private double scaleDriveJoystick(double joystickValue) {
        // if the joystick is in the deadzone I defined, return 0
        if (Math.abs(joystickValue) < JOYSTICK_DEADZONE_LIMIT) return 0.0;
        // use the formula A*B^(joystickValue)
        double power = Math.signum(joystickValue) * A * Math.pow(B, Math.abs(joystickValue));
        if (Math.abs(power) < MIN_POWER) return 0.0;
        return Range.clip(power, -1.0, 1.0);
    }

    private void pressBeacon() throws InterruptedException {
        Util.upDown.setPosition(Util.BEACON_DOWN);
        if (gamepad1.y) {
            if (beaconForward() == -1) return;
        }
        else if (gamepad1.a && !gamepad1.start) {
            if (beaconBackward() == -1) return;
        }
    }

    private int beaconForward() throws InterruptedException {
        Util.setRightPowers(0.16);
        Util.setLeftPowers(0.14);
        //if (lookForLineAndCheckJoystick(0.50) == -1) return -1;
        if (sleepAndCheckJoystick(2000) == -1) return -1;
        Util.setAllPowers(0);
        return 0;
    }

    private int beaconBackward() throws InterruptedException {
        Util.setRightPowers(-0.16);
        Util.setLeftPowers(-0.14);
        //if (lookForLineAndCheckJoystick(0.50) == -1) return -1;
        if (sleepAndCheckJoystick(2000) == -1) return -1;
        Util.setAllPowers(0);
        return 0;
    }

    private int lookForLineAndCheckJoystick(double lightThreshold) throws InterruptedException {
        while (ods.getLightDetected() < lightThreshold) {
            if (Math.abs(gamepad1.right_stick_y) > JOYSTICK_DEADZONE_LIMIT || Math.abs(gamepad1.left_stick_y) > JOYSTICK_DEADZONE_LIMIT) return -1;
            Thread.sleep(20);
        }
        return 0;
    }

    // Might not need this method in the end
    private int sleepAndCheckJoystick(int sleepTimeMillis) throws InterruptedException {
        long startTime = System.nanoTime() / MILLIS_PER_NANO;
        while (((System.nanoTime() / MILLIS_PER_NANO) - startTime) < sleepTimeMillis) {
            if (Math.abs(gamepad1.right_stick_y) > JOYSTICK_DEADZONE_LIMIT || Math.abs(gamepad1.left_stick_y) > JOYSTICK_DEADZONE_LIMIT) return -1;
            Thread.sleep(20);
        }
        return 0;
    }

    // intake variables
    private final int INTAKE_OFF = 0, INTAKE = 1, OUTTAKE = 2;

    private void handleIntake() {
        if ((gamepad1.right_bumper && gamepad1.left_bumper) && !intakeChanged) {
            /* if the intake is off, outtake
             * if the intake is intaking, outtake
             * if the intake is outtaking, do nothing
             */
            switch (intakeStatus) {
                case INTAKE_OFF:
                case INTAKE: outtake(); break;
                case OUTTAKE: break;
            }
            intakeChanged = true;
        }
        if (gamepad1.left_bumper && !intakeChanged) {
            /* if the intake is off, do nothing
             * if the intake is intaking, turn it off
             * if the intake is outtaking, turn it off
             */
            switch (intakeStatus) {
                case INTAKE_OFF: break;
                case INTAKE:
                case OUTTAKE: intakeOff(); break;
            }
            intakeChanged = true;
        }
        if (gamepad1.right_bumper && !intakeChanged) {
            /* if the intake is off, intake
             * if the intake is intaking, do nothing
             * if the intake is outtaking, intake
             */
            switch (intakeStatus) {
                case INTAKE_OFF: intake(); break;
                case INTAKE: break;
                case OUTTAKE: intake(); break;
            }
            intakeChanged = true;
        }
        // wait until the user releases all intake-related buttons before allowing the user to change the intake again
        else if (!gamepad1.right_bumper && !gamepad1.left_bumper) {
            intakeChanged = false;
            if (intakeStatus == OUTTAKE) {
                intakeOff();
            }
        }
    }

    // the three following methods standardize intaking, outtaking, and neither
    private void intake() {
        this.intake.setPower(INTAKE_POWER);
        intakeStatus = INTAKE;
    }

    private void outtake() {
        this.intake.setPower(-INTAKE_POWER);
        intakeStatus = OUTTAKE;
    }

    private void intakeOff() {
        this.intake.setPower(0);
        intakeStatus = INTAKE_OFF;
    }

    private boolean SHOOTER_ON = true, SHOOTER_OFF = false;
    private int shooterSpinUp = 2000, shooterLoad = 2000, shooterFire = 400;

    private void handleShooter() throws InterruptedException {
        long time = System.nanoTime() / 1000000;

        if (gamepad1.right_trigger >= 0.5) {
            double power = calculateShooterPower();
            shooter1.setPower(power);
            shooter2.setPower(power + SHOOTER2_OFFSET);
            shooterStart = time;
            shooterStatus = SHOOTER_ON;
            telemetry.addData("shooter power", power);
        }
        if (gamepad1.left_trigger >= 0.5) {
            shooter1.setPower(0);
            shooter2.setPower(0);
            shooterStatus = SHOOTER_OFF;
        }

        if (gamepad1.b && shooterStatus && (time - shooterStart) > shooterSpinUp) { // && (time - shooterLoadTimer) > shooterLoad) {
            ballFeeder.setPosition(this.SHOOT);
            Thread.sleep(shooterFire);
            ballFeeder.setPosition(this.LOAD);
            //shooterLoadTimer = System.nanoTime();
        }
    }

    protected static double calculateShooterPower() {
        double voltage = Util.getBatteryVoltage();
        if (voltage >= 13.8) return 0.21;
        else return -0.048*Util.getBatteryVoltage() + 0.880;
    }
}
            /*if (driveMode.equals(NORMAL)) {
                rightBack.setPower((currentR - 1) * POWER_FACTOR);
                leftBack.setPower((currentL - 1) * POWER_FACTOR);
                if (!Util.TANK) {
                    rightFront.setPower((currentR - 1) * POWER_FACTOR);
                    leftFront.setPower((currentL - 1) * POWER_FACTOR);
                }
            } else {
                rightBack.setPower((currentR - 1) * POWER_FACTOR);
                leftBack.setPower((currentR - 1) * POWER_FACTOR);
                if (!Util.TANK) {
                    rightFront.setPower((currentR - 1) * POWER_FACTOR);
                    leftFront.setPower((currentR - 1) * POWER_FACTOR);
                }
            }

            if (!aHasBeenPressed && gamepad1.a && driveMode.equals(NORMAL)) {
                driveMode = STRAIGHT;
                aHasBeenPressed = true;
            } else if (!aHasBeenPressed && gamepad1.a && driveMode.equals(STRAIGHT)) {
                driveMode = NORMAL;
                aHasBeenPressed = true;
            }

            if (!gamepad1.a) aHasBeenPressed = false;*/

            /*telemetry.addData("drive mode", driveMode);
            telemetry.update();*/
