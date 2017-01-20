package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name="ShooterControl", group="Test")
//@Disabled
public class ShooterControl extends OpMode {

    DcMotor shooter1, shooter2;

    boolean aPressed = false, on = false;

    public void init() {
        shooter1 = hardwareMap.dcMotor.get("shooter1");
        shooter2 = hardwareMap.dcMotor.get("shooter2"); shooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void loop() {
        if (gamepad1.a && !on && !aPressed) {
            shooter1.setPower(0.24);
            shooter2.setPower(0.24);

            aPressed = true;
            on = true;
        } else if (gamepad1.a &&  on && !aPressed) {
            shooter1.setPower(0);
            shooter2.setPower(0);

            aPressed = true;
            on = false;
        } else if (!gamepad1.a && aPressed) {
            aPressed = false;
        }
    }
}
