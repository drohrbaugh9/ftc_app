package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class TestAuto extends LinearOpMode {

    // WestCoastTank
    DcMotor rightFront, leftFront, rightBack, leftBack;

    //Tank
    DcMotor right, left;

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

        waitForStart();


    }
}
