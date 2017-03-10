package com.qualcomm.ftcrobotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class ____TestingTeleOp extends LinearOpMode {

    DcMotor right, left, rightFront, leftFront;
    Servo leftDoor, bumper;

    private boolean stalled = false;
    private double powerFactor = 1;
    private float stallEnabledTime = 0f, currentTime;

    private final double FRONT_SCALE = Util.POWER_LIMIT, BACK_SCALE = FRONT_SCALE * 1.3;
    final static double SEC_TO_NSEC = Util.SEC_TO_NSEC;

    public void runOpMode() throws InterruptedException {
        Util.init(this);
        StallProtection.init();
        AutoUtil.resetEncoders();
        right = Util.rightBack;
        left = Util.leftBack;
        rightFront = Util.rightFront;
        leftFront = Util.leftFront;

        leftDoor = Util.leftDoor;
        bumper = Util.rightTrigger;

        waitForStart();

        stallEnabledTime = System.nanoTime();

        while (true) {
            currentTime = System.nanoTime();

            float r = -gamepad1.right_stick_y;
            float l = -gamepad1.left_stick_y;

            r = joystickToDrivePower(r);
            l = joystickToDrivePower(l);

            if ((r + l) == 0) stallEnabledTime = currentTime;

            stallProtection((r + l) / 2);

            this.right.setPower(powerFactor * BACK_SCALE * r);
            this.rightFront.setPower(powerFactor * FRONT_SCALE * r);
            this.left.setPower(powerFactor * BACK_SCALE * l);
            this.leftFront.setPower(powerFactor * FRONT_SCALE * l);

        }
    }

    private float JOYSTICK_DEADZONE_LIMIT = 0.2f;
    private float MIN_POWER = 0.2f;
    private double POWER_EXPONENT = 1.4;

    private float joystickToDrivePower(float joystickValue) {
        if (Math.abs(joystickValue) < JOYSTICK_DEADZONE_LIMIT) joystickValue = 0;
        float power = (float) ((joystickValue / Math.abs(joystickValue)) * Math.pow(joystickValue, POWER_EXPONENT));
        if (Math.abs(power) < MIN_POWER) power = 0;
        return Range.clip(power, -1, 1);
    }

    private void stallProtection(double requestedPower) {
        boolean enabled = ((currentTime - stallEnabledTime) / SEC_TO_NSEC > 0.02 && requestedPower > (0.9 * FRONT_SCALE)); //TODO add other criteria
        if (enabled && StallProtection.stalled() == 1) {
            powerFactor -= 0.01;
            if (powerFactor < 0) powerFactor = 0;
        }
    }
}
