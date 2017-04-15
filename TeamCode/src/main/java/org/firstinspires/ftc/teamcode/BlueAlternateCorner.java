package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="BlueAlternateCorner", group = "Competition")
@Disabled
public class BlueAlternateCorner extends LinearOpMode {
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

        // turn on red LED on Device Interface Module to indicate Red Auto (and make sure blue LED is off)
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

        ShooterPID.init();

        waitForStart();

        /*telemetry.addData("auto status", "waiting 12 seconds");
        telemetry.update();

        Thread.sleep(15 * 1000); // 12*/

        AutoLoopTest.driveAndShoot(3200, 2);

        AutoUtil.encoderTurnRight(60, 0.2);

        Thread.sleep(200);

        AutoUtil.PID_Forward(5500, 0.3, true, gyro);

        Thread.sleep(500);

        Util.setAllPowers(0);

        while (opModeIsActive()) Thread.sleep(10);
    }
}