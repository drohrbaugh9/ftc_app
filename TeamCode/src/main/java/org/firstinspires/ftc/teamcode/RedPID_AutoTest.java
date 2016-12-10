package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

@Autonomous(name="PID_AutoTest", group="test")
//@Disabled
public class RedPID_AutoTest extends LinearOpMode {

    GyroSensor gyro;

    public void runOpMode() throws InterruptedException {
        Util.init(this);

        Util.rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Util.leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Util.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Util.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        gyro = hardwareMap.gyroSensor.get("gyro");

        gyro.resetZAxisIntegrator();

        gyro.calibrate();

        while (gyro.isCalibrating());

        waitForStart();

        AutoUtil.moveBackward(1600, 0.3, gyro);
        // shoot
        AutoUtil.turnRight(100, 0.3, gyro);
        AutoUtil.moveForward(4400, 0.3, gyro);
        AutoUtil.turnRight(75, 0.3, gyro);
        AutoUtil.moveForward(5040, 0.3, gyro); // add steering

        /*
        * go slow and look for line
        * do 1st beacon
        * back up, still steering into wall at 0.3 power til close to line
        * go slow and look for line
        * do 2nd beacon
        */
    }
}
