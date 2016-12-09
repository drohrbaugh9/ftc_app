package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;


@Autonomous(name="Gyro Test", group="test")
//@Disabled
public class GyroTest extends LinearOpMode {

    DcMotor right, left;
    GyroSensor gyro;

    public void runOpMode() throws InterruptedException {
        Util.init(this);

        right = Util.rightBack;
        left = Util.leftBack;

        gyro = hardwareMap.gyroSensor.get("gyro");

        gyro.resetZAxisIntegrator();

        gyro.calibrate();

        while (gyro.isCalibrating());

        waitForStart();

        AutoUtil.moveForward(1000000000, 0.5, gyro);

        right.setPower(0);
        left.setPower(0);
    }
}
