package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "RunAMotorOrTwo", group="Test")
//@Disabled
public class RunAMotorOrTwo extends OpMode {

    DcMotor motor1, motor2;
    double rightPower, leftPower;
    boolean bPressed, xPressed;
    boolean aPressed, yPressed;
    boolean upPressed, downPressed;
    boolean motor1On, motor2On;
    final double buttonPower = 0.5;

    public void init() {
        motor1 = hardwareMap.dcMotor.get("intake1");
        motor2 = hardwareMap.dcMotor.get("intake2");
        rightPower = 0; leftPower = 0;
        bPressed = false; xPressed = false;
        aPressed = false; yPressed = false;
        upPressed = false; downPressed = false;
        motor1On = false; motor2On = false;
    }

    public void loop() {
        double r = -gamepad1.right_stick_y;
        double l = -gamepad1.left_stick_y;

        handleB_XButtons();

        handleA_YButtons();

        handleDpad();

        if (Math.abs(r) <= 0.1 && motor1On) r = 0; else rightPower = r;
        if (Math.abs(l) <= 0.1 && motor2On) l = 0; else leftPower = l;

        if (!(gamepad1.a || gamepad1.b || gamepad1.x || gamepad1.y || gamepad1.dpad_up || gamepad1.dpad_down)) {
            rightPower = r;
            leftPower = l;
        }

        motor1.setPower(rightPower);
        motor2.setPower(leftPower);

        telemetry.addData("motor 1 power", motor1.getPower());
        telemetry.addData("motor 2 power", motor2.getPower());
    }

    public void handleB_XButtons() {
        if (!bPressed && gamepad1.b && !motor1On) {
            rightPower = buttonPower;
            motor1On = true;
            bPressed = true;
        }
        if (!bPressed && gamepad1.b && motor1On) {
            rightPower = 0;
            motor1On = false;
            bPressed = true;
        }
        if (!xPressed && gamepad1.x && !motor2On) {
            leftPower = buttonPower;
            motor2On = true;
            xPressed = true;
        }
        if (!xPressed && gamepad1.x && motor2On) {
            leftPower = 0;
            motor2On = false;
            xPressed = true;
        }

        if (!gamepad1.b) bPressed = false;
        if (!gamepad1.x) xPressed = false;
    }

    public void handleA_YButtons() {
        if (!yPressed && gamepad1.y) {
            rightPower += 0.1;
            if (rightPower > 1) rightPower = 1;
            yPressed = true;
        }
        if (!aPressed && gamepad1.a) {
            rightPower -= 0.1;
            if (rightPower < -1) rightPower = -1;
            aPressed = true;
        }
        if (!aPressed) aPressed = false;
        if (!yPressed) yPressed = false;
    }

    public void handleDpad() {
        if (!upPressed && gamepad1.dpad_up) {
            leftPower += 0.1;
            if (leftPower > 1) leftPower = 1;
            upPressed = true;
        }
        if (!downPressed && gamepad1.dpad_down) {
            leftPower -= 0.1;
            downPressed = true;
        }
        if (!upPressed) upPressed = false;
        if (!downPressed) downPressed = false;
    }
}
