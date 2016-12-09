package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
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

        /*Util.rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Util.leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Util.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Util.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);*/

        gyro = hardwareMap.gyroSensor.get("gyro");

        gyro.resetZAxisIntegrator();

        gyro.calibrate();

        while (gyro.isCalibrating());

        waitForStart();

        //AutoUtil.moveForward(10000, 0.5, gyro);
        AutoUtil.turnRight(90, 0.3, gyro);

        while(opModeIsActive()) {
            telemetry.addData("gyro", PID.heading(gyro));
            telemetry.update();
        }
    }
}