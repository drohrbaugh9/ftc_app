package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

@Autonomous(name = "Test", group = "Test")
//@Disabled
public class TestAuto extends LinearOpMode {

    // WestCoastTank
    DcMotor rightFront, leftFront, rightBack, leftBack;

    //Tank
    DcMotor right, left;

    OpticalDistanceSensor ods;

    final double POWER_FACTOR = 0.8;

    public void runOpMode() {
        // WestCoastTank
        /*rightFront = hardwareMap.dcMotor.get("rightFront"); rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightBack = hardwareMap.dcMotor.get("rightBack"); rightBack.setDirection(DcMotor.Direction.REVERSE);
        leftBack = hardwareMap.dcMotor.get("leftBack");*/

        // Tank
        right = hardwareMap.dcMotor.get("right"); right.setDirection(DcMotor.Direction.REVERSE);
        left = hardwareMap.dcMotor.get("left");

        ods = hardwareMap.opticalDistanceSensor.get("ods");

        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        right.setPower(0.3);
        left.setPower(0.3);

        while (ods.getLightDetected() < 0.05) {
            telemetry.addData("light value", ods.getLightDetected());
        }

        telemetry.addData("light value", ods.getLightDetected());

        right.setPower(0);
        left.setPower(0);
    }
}
