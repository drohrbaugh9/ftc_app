package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "FinalTeleOp", group = "Competition")
//@Disabled
public class FinalTeleOp extends LinearOpMode {

    DcMotor rightBack, leftBack, rightFront, leftFront;
    DcMotor intake, shooter1, shooter2;

    Servo ballFeeder;

    //final String NORMAL = "normal", STRAIGHT = "straight";
    final double POWER_FACTOR = 1, POSITIVE_STEP = 0.1, NEGATIVE_STEP = 0.3;
    final double INTAKE_POWER = 0.7;
    final double SHOOT = 0.5, LOAD = 0.95;

    //String driveMode = NORMAL;
    long shooterStart = System.nanoTime();
    double targetPowerR = 1, targetPowerL = 1, currentR = 1, currentL = 1;
    boolean shooterStatus = false; //, aHasBeenPressed = false;

    private int intakeStatus = 0;
    private boolean intakeChanged = false;

    public void runOpMode() throws InterruptedException {
        Util.init(this);

        this.rightBack = Util.rightBack;
        this.leftBack = Util.leftBack;
        this.rightFront = Util.rightFront;
        this.leftFront = Util.leftFront;

        shooter1 = hardwareMap.dcMotor.get("shooter1");
        shooter2 = hardwareMap.dcMotor.get("shooter2"); shooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        intake = Util.getMotor(hardwareMap, "intake1");

        ballFeeder = hardwareMap.servo.get("ballFeeder");

        ballFeeder.setPosition(LOAD);

        waitForStart();

        while (opModeIsActive()) {

            handleDriveMotors();

            handleIntake();

            handleShooter();

            Thread.sleep(10);
        }
    }

    private void handleDriveMotors() {
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

        telemetry.addData("right speed", r);
        telemetry.addData("left speed", l);
        telemetry.update();
    }

    private final double JOYSTICK_DEADZONE_LIMIT = 0.1;
    private final double MIN_POWER = 0.1;
    private final double B = 13.2699, A = 0.0684;

    private double scaleDriveJoystick(double joystickValue) {
        // if the joystick is in the deadzone I defined, return 0
        if (Math.abs(joystickValue) < JOYSTICK_DEADZONE_LIMIT) return 0.0;
        /* because exponentiation can change signs,
         * store the sign of the joystick position in a separate variable */
        double posOrNeg = 1;
        if (joystickValue < 0) posOrNeg = -1;
        // us the formula A*B^(joystickValue)
        double power = posOrNeg * A * Math.pow(B, Math.abs(joystickValue));
        if (Math.abs(power) < MIN_POWER) return 0.0;
        return Range.clip(power, -1.0, 1.0);
    }

    // intake variables
    private final int OFF = 0, INTAKE = 1, OUTTAKE = 2;

    private void handleIntake() {
        if ((gamepad1.right_bumper && gamepad1.left_bumper) && !intakeChanged) {
            /* if the intake is off, outtake
             * if the intake is intaking, outtake
             * if the intake is outtaking, do nothing
             */
            switch (intakeStatus) {
                case OFF:
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
                case OFF: break;
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
                case OFF: intake(); break;
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
        intakeStatus = OFF;
    }

    private void handleShooter() throws InterruptedException {
        long time = System.nanoTime() / 1000000;

        if (gamepad1.right_trigger >= 0.5) {
            shooter1.setPower(0.27);
            shooter2.setPower(0.27);
            shooterStart = time;
            shooterStatus = true;
        }
        if (gamepad1.left_trigger >= 0.5) {
            shooter1.setPower(0);
            shooter2.setPower(0);
            shooterStatus = false;
        }

        if (!shooterStatus) shooterStart = time;

        if (gamepad1.b && shooterStatus && (time - shooterStart) > 2000) {
            ballFeeder.setPosition(SHOOT);
            Thread.sleep(400);
            ballFeeder.setPosition(LOAD);
        }
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
