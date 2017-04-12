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
    protected static final double SHOOTER2_OFFSET = 0.04; // 0.07
    private final double INTAKE_POWER = 0.9;
    private final double SHOOT = Util.SHOOT, LOAD = Util.LOAD;
    protected static final long MILLIS_PER_NANO = 1000000;

    //String driveMode = NORMAL;
    private long shooterStart = System.nanoTime(), shooterLoadTimer = shooterStart;
    private double targetPowerR = 1, targetPowerL = 1, currentR = 1, currentL = 1;
    private boolean shooterStatus = false; //, aHasBeenPressed = false;

    private int intakeStatus = 0;
    private boolean intakeChanged = false;
    private long oldLoopTime;

    public void runOpMode() throws InterruptedException {
        Util.colorSensors = false; Util.otherSensors = true; Util.servos = true;
        Util.init(this);
        ShooterPID.init();

        this.rightBack = Util.rightBack;
        this.leftBack = Util.leftBack;
        this.rightFront = Util.rightFront;
        this.leftFront = Util.leftFront;

        this.shooter1 = Util.shooter1;
        this.shooter2 = Util.shooter2;

        DcMotor[] temp = new DcMotor[6];
        temp[0] = this.rightBack; temp[1] = this.leftBack;
        temp[2] = this.rightFront; temp[3] = this.leftFront;
        temp[4] = this.shooter1; temp[5] = this.shooter2;

        Util.resetEncoders(this, temp);

        this.intake = Util.intake;

        this.ballFeeder = Util.ballFeeder;

        this.ods = Util.ods;

        ShooterPID.realRPMtarget = 1050;
        ShooterPID.calcuateTicsTarget(1050);

        Util.upDown.setPosition(Util.BEACON_DOWN);

        waitForStart();

        //long start = System.nanoTime();
        oldLoopTime = System.nanoTime();

        while (opModeIsActive()) {
        //for (int i = 0; i < 1000; i++) {

            handleDriveMotors();

            handleIntake();

            handleShooter();

            //telemetry.addData("battery voltage", Util.getBatteryVoltage());

            //Util.telemetry("shooter power", shooter1.getPower(), true);

            Thread.sleep(10);
        }

        /*/
        long end = System.nanoTime();

        Util.telemetry("average loop time (ms)", (end - start) / 1000000000, true);

        while (opModeIsActive()) Thread.sleep(20);
        /**/
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
        Util.setRightPowers(0.26);
        Util.setLeftPowers(0.28);
        //if (lookForLineAndCheckJoystick(0.50) == -1) return -1;
        if (lookForLineAndCheckJoystick(0.5) == -1) return -1;
        if (sleepAndCheckJoystick(250) == -1) return -1;
        Util.setAllPowers(0);
        if (sleepAndCheckJoystick(50) == -1) return -1;
        Util.setRightPowers(-0.16);
        Util.setLeftPowers(-0.14);
        if (sleepAndCheckJoystick(1000) == -1) return -1;
        return 0;
    }

    private int beaconBackward() throws InterruptedException {
        Util.setRightPowers(-0.28);
        Util.setLeftPowers(-0.26);
        //if (lookForLineAndCheckJoystick(0.50) == -1) return -1;
        if (lookForLineAndCheckJoystick(0.5) == -1) return -1;
        if (sleepAndCheckJoystick(50) == -1) return -1;
        Util.setAllPowers(0);
        if (sleepAndCheckJoystick(50) == -1) return -1;
        Util.setRightPowers(0.16);
        Util.setLeftPowers(0.14);
        if (sleepAndCheckJoystick(1000) == -1) return -1;
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
        /*if ((gamepad1.right_bumper && gamepad1.left_bumper) && !intakeChanged) {
            /* if the intake is off, outtake
             * if the intake is intaking, outtake
             * if the intake is outtaking, do nothing
             *//*
            switch (intakeStatus) {
                case INTAKE_OFF:
                case INTAKE: outtake(); break;
                case OUTTAKE: break;
            }
            intakeChanged = true;
        }*/
        if (gamepad1.left_bumper && !intakeChanged) {
            /* if the intake is off, do nothing
             * if the intake is intaking, turn it off
             * if the intake is outtaking, turn it off
             */
            switch (intakeStatus) {
                case INTAKE_OFF: outtake(); break;
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
                case INTAKE:
                case OUTTAKE: intakeOff(); break;
                //case OUTTAKE: break;
            }
            intakeChanged = true;
        }
        // wait until the user releases all intake-related buttons before allowing the user to change the intake again
        else if (!gamepad1.right_bumper && !gamepad1.left_bumper) {
            intakeChanged = false;
            /*if (intakeStatus == OUTTAKE) {
                intakeOff();
            }*/
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
    private int shooterPID = 1200, shooterSpinUp = 1000 /* 1500 */, shooterLoad = 2000, shooterFire = 300;
    private double shooter1Power = 0, shooter2Power = 0;

    private void handleShooter() throws InterruptedException {
        long time = System.nanoTime() / MILLIS_PER_NANO;

        if (!shooterStatus && gamepad1.right_trigger >= 0.5) {
            double power = calculateShooterPower();
            shooter1Power = power;
            shooter2Power = power + SHOOTER2_OFFSET;
            shooter1.setPower(shooter1Power);
            shooter2.setPower(shooter2Power);
            shooterStart = time;
            shooterStatus = SHOOTER_ON;
            telemetry.addData("shooter power", power);
        }
        if (gamepad1.left_trigger >= 0.5) {
            shooter1.setPower(0);
            shooter2.setPower(0);
            shooter1Power = 0;
            shooter2Power = 0;
            shooterStatus = SHOOTER_OFF;
        }

        if (shooterStatus) {
            ShooterPID.manageEncoderData(time - oldLoopTime);

            if ((time - shooterStart) > shooterPID) {
                //Util.telemetry("elapsedTime", time - oldLoopTime, false);
                double[] powers = ShooterPID.PID_calculateShooterPower(shooter1Power, shooter2Power);
                shooter1Power = powers[0];
                shooter2Power = powers[1];
                shooter1.setPower(shooter1Power);
                shooter2.setPower(shooter2Power);
                /*Util.telemetry("power1", powers[0], false);
                Util.telemetry("power2", powers[1], true);*/
            }

            if (gamepad1.b && (time - shooterStart) > shooterSpinUp) { // && (time - shooterLoadTimer) > shooterLoad) {
                ballFeeder.setPosition(this.SHOOT);
                Util.log("STATUS: shots fired as of now");
                Thread.sleep(shooterFire);
                shooter1.setPower(0.5);
                shooter2.setPower(0.5);
                Thread.sleep(50);
                ballFeeder.setPosition(this.LOAD);
                shooter1.setPower(shooter1Power);
                shooter2.setPower(shooter2Power);
                shooterStart = time;
                //shooterLoadTimer = System.nanoTime();
            }
        } else {
            ShooterPID.clearQueue();
        }

        oldLoopTime = time;
    }

    protected static double calculateShooterPower() {
        double voltage = Util.getBatteryVoltage();
        if (voltage >= 13.6) return -0.033*voltage + 0.71; //0.696
        else return -0.04*Util.getBatteryVoltage() + 0.80; //0.784
    }
}
