package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TankTeleOp", group="Competition")
//@Disabled
public class TankTeleOp extends LinearOpMode {

    DcMotor rightBack, leftBack, rightFront, leftFront;

    final String NORMAL = "normal", STRAIGHT = "straight";
    final double POWER_FACTOR = 1;

    String driveMode = NORMAL;

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

            r = scaleDriveJoystick(r) * POWER_FACTOR;
            l = scaleDriveJoystick(l) * POWER_FACTOR;

            if (driveMode.equals(NORMAL)) {
                rightBack.setPower(r);
                leftBack.setPower(l);
                if (!Util.TANK) {
                    rightFront.setPower(r);
                    leftFront.setPower(l);
                }
            } else {
                rightBack.setPower(r);
                leftBack.setPower(r);
                if (!Util.TANK) {
                    rightFront.setPower(r);
                    leftFront.setPower(r);
                }
            }

            if (gamepad1.a && driveMode.equals(NORMAL)) {
                driveMode = STRAIGHT;

            } else if (gamepad1.a && driveMode.equals(STRAIGHT)) {
                driveMode = NORMAL;
            }

            telemetry.addData("drive mode", driveMode);
            telemetry.addData("right speed", r);
            telemetry.addData("left speed", l);
            telemetry.update();
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
