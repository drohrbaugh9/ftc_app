package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="RedAlternate", group = "Competition")
//@Disabled
public class RedAlternate extends LinearOpMode {
    // motors
    DcMotor rightBack, leftBack, rightFront, leftFront;
    DcMotor shooter1, shooter2;
    DcMotor[] motors;

    // servos
    Servo ballFeeder, upDown;

    // sensors
    OpticalDistanceSensor ods;
    GyroSensor gyro;

    public void runOpMode() throws InterruptedException {
        Util.colorSensors = false;
        Util.otherSensors = true;
        Util.servos = true;
        Util.init(this);

        DeviceInterfaceModule dim = hardwareMap.deviceInterfaceModule.get("Sensors");
        dim.setLED(0, false);
        dim.setLED(1, true);

        // drive motors
        this.rightBack = Util.rightBack;
        this.leftBack = Util.leftBack;
        this.rightFront = Util.rightFront;
        this.leftFront = Util.leftFront;

        motors = new DcMotor[4];
        motors[0] = this.rightBack;
        motors[1] = this.leftBack;
        motors[2] = this.rightFront;
        motors[3] = this.leftFront;

        // shooter motors
        this.shooter1 = Util.shooter1;
        this.shooter2 = Util.shooter2;

        // servos
        this.ballFeeder = Util.ballFeeder;
        this.upDown = Util.upDown;

        // otherSensors
        //this.ods = Util.ods;
        this.gyro = Util.gyro;
        //I2C_ColorSensor.init(this);

        Util.resetEncoders(this, motors);

        waitForStart();

        telemetry.addData("auto status", "waiting 12 seconds");
        telemetry.update();

        Thread.sleep(12 * 1000);

        double shooterPower = FinalTeleOp.calculateShooterPower();
        shooter1.setPower(shooterPower);
        shooter2.setPower(shooterPower + FinalTeleOp.SHOOTER2_OFFSET);

        telemetry.addData("auto status", "starting shooter motors with 3 secs to go");
        telemetry.update();

        Thread.sleep(3000);

        telemetry.addData("auto status", "starting movements");
        telemetry.update();

        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        AutoUtil.PID_Forward(1800, 0.2, true, gyro);

        Thread.sleep(700);

        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shoot();

        AutoUtil.encoderTurnLeft(50, 0.2);

        Thread.sleep(200);

        AutoUtil.PID_Forward(6000, 0.3, true, gyro);

        while (opModeIsActive()) {
            Thread.sleep(10);
        }
    }

    private void shoot() throws InterruptedException {
        ballFeeder.setPosition(Util.SHOOT);

        Thread.sleep(400);

        ballFeeder.setPosition(Util.LOAD);

        Thread.sleep(1000);

        shooter1.setPower(0);
        shooter2.setPower(0);
    }
}