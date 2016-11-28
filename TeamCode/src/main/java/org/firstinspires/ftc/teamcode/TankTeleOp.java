package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TankTeleOp", group="Competition")
//@Disabled
public class TankTeleOp extends LinearOpMode {

    DcMotor rightBack, leftBack, rightFront, leftFront;

    final String NORMAL = "normal", STRAIGHT = "straight";
    final double POWER_FACTOR = 1, POSITIVE_STEP = 0.1, NEGATIVE_STEP = 0.3;

    String driveMode = NORMAL;
    double targetPowerR = 1, targetPowerL = 1, currentR = 1, currentL = 1;
    boolean aHasBeenPressed = false;

    public void runOpMode() throws InterruptedException {
        Util.init(this);

        this.rightBack = Util.rightBack;
        this.leftBack = Util.leftBack;
        if (!Util.TANK) {
            this.rightFront = Util.rightFront;
            this.leftFront = Util.leftFront;
        }

        waitForStart();

        while(opModeIsActive()) {
            double r = Util.getGamepadRightJoystickY(gamepad1);
            double l = Util.getGamepadLeftJoystickY(gamepad1);

            r = scaleDriveJoystick(r);
            l = scaleDriveJoystick(l);

            targetPowerR = r + 1;
            if (driveMode.equals(NORMAL)) {
                targetPowerL = l + 1;
            } else {
                targetPowerL = r + 1;
            }

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
            } else if (currentL> targetPowerL) {
                currentL = targetPowerL;
            }

            if (driveMode.equals(NORMAL)) {
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

            if (!gamepad1.a) aHasBeenPressed = false;

            telemetry.addData("drive mode", driveMode);
            telemetry.addData("right speed", r);
            telemetry.addData("left speed", l);
            telemetry.update();

            Thread.sleep(10);
        }
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
}
