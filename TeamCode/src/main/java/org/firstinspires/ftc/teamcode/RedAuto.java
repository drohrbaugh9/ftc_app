package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

public class RedAuto extends LinearOpMode {

    // motors
    DcMotor rightBack, leftBack, rightFront, leftFront;
    DcMotor shooter1, shooter2;
    DcMotor[] motors;

    // servos
    Servo ballFeeder, upDown;

    // sensors
    OpticalDistanceSensor ods;

    public void runOpMode() throws InterruptedException {
        Util.init(this);

        // drive motors
        this.rightBack = Util.rightBack; this.leftBack = Util.leftBack;
        this.rightFront = Util.rightFront; this.leftFront = Util.leftFront;

        motors = new DcMotor[4]; motors[0] = this.rightBack; motors[1] = this.leftBack; motors[2] = this.rightFront; motors[3] = this.leftFront;

        // shooter motors
        this.shooter1 = Util.shooter1; this.shooter2 = Util.shooter2;

        // servos
        this.ballFeeder = Util.ballFeeder;
        this.upDown = Util.upDown;

        // sensors
        this.ods = Util.ods;
        I2C_ColorSensor.init(this);

        Util.resetEncoders(this, motors);

        waitForStart();
    }
}
