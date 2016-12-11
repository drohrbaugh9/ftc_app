package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous(name="TankRedPID_AutoTest", group="test")
//@Disabled
public class TankRedPID_AutoTest extends LinearOpMode {

    DcMotor right, left, shooter;
    DcMotor[] motors;
    Servo upDown;
    GyroSensor gyro;
    OpticalDistanceSensor ods;

    int pos;

    public void runOpMode() throws InterruptedException {
        Util.init(this);

        right = Util.rightBack; left = Util.leftBack;

        motors = new DcMotor[2]; motors[0] = right; motors[1] = left;

        shooter = hardwareMap.dcMotor.get("shooter");
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        upDown = hardwareMap.servo.get("upDown");

        gyro = hardwareMap.gyroSensor.get("gyro");

        ods = hardwareMap.opticalDistanceSensor.get("ods");

        I2C_ColorSensor.init(this);

        Util.resetEncoders(this, motors);

        gyro.resetZAxisIntegrator();

        gyro.calibrate();

        while (gyro.isCalibrating()) ;

        waitForStart();

        AutoUtil.moveForward(2000, 0.3, gyro);

        shooter.setPower(1);

        Thread.sleep(6000);

        shooter.setPower(0);

        AutoUtil.turnLeft(50, 0.3, gyro);

        Thread.sleep(200);

        AutoUtil.moveForward(3750, 0.4, gyro);

        AutoUtil.turnRight(60, 0.4, gyro);

        /*gyro.calibrate(); while(gyro.isCalibrating());
        gyro.resetZAxisIntegrator();*/

        Util.setRightPowers(0.44);
        Util.setLeftPowers(0.36);

        Thread.sleep(1750);

        Util.setRightPowers(0.22);
        Util.setLeftPowers(0.18);

        while (ods.getLightDetected() < 0.25) ;

        Util.setAllPowers(0);

        if (I2C_ColorSensor.beaconIsRed()) {
            //telemetry.addData("beacon status", "RED");
            forward();
            upDown.setPosition(0.9);
            backward();
        } else if (I2C_ColorSensor.beaconIsBlue()) {
            //telemetry.addData("beacon status", "BLUE");
            backward();
            upDown.setPosition(0.9);
            forward();
        }

        //telemetry.update();

        Thread.sleep(200);

        pos = left.getCurrentPosition();

        right.setPower(-0.47);
        left.setPower(-0.33);

        while (left.getCurrentPosition() > (pos - 750)) ;

        upDown.setPosition(0.6);

        right.setPower(-0.34);
        left.setPower(-0.26);

        while (ods.getLightDetected() < 0.25) ;

        right.setPower(0);
        left.setPower(0);

        Thread.sleep(200);
        pos = left.getCurrentPosition();

        right.setPower(0.34);
        left.setPower(0.26);

        while (left.getCurrentPosition() < (pos + 250)) ;

        right.setPower(0);
        left.setPower(0);

        Thread.sleep(200);

        if (I2C_ColorSensor.beaconIsRed()) {
            telemetry.addData("beacon status", "RED");
            forward();
            upDown.setPosition(0.9);
            backward();
        } else if (I2C_ColorSensor.beaconIsBlue()) {
            telemetry.addData("beacon status", "BLUE");
            backward();
            upDown.setPosition(0.9);
            forward();
        }

        while (opModeIsActive()) {
            telemetry.addData("gyro", PID.heading(gyro));
            telemetry.addData("ods", ods.getLightDetected());
            telemetry.update();
        }
    }

    public void forward() {
        pos = left.getCurrentPosition();

        right.setPower(0.24);
        left.setPower(0.16);

        while (left.getCurrentPosition() < (pos + 750)) ;

        right.setPower(0);
        left.setPower(0);
    }

    public void backward() throws InterruptedException {
        pos = left.getCurrentPosition();

        right.setPower(-0.24);
        left.setPower(-0.16);

        while (left.getCurrentPosition() > (pos - 500));

        right.setPower(0);
        left.setPower(0);

        Thread.sleep(200);

        pos = left.getCurrentPosition();

        right.setPower(0.24);
        left.setPower(0.16);

        while (left.getCurrentPosition() > (pos + 500)) ;

        right.setPower(0);
        left.setPower(0);
    }
}