package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="ChassisAutoTest", group="Test")
@Disabled
public class ChassisAutoTest extends LinearOpMode {

    DcMotor right, left;
    Servo servo;


    private void turnLeft(int turns) throws InterruptedException{
        for(int i = 0; i < turns; i++) {
            right.setPower(0.5);
            left.setPower(-0.5);

            Thread.sleep(1000);

            right.setPower(0);
            left.setPower(0);

            Thread.sleep(500);

            right.setPower(0.5);
            left.setPower(0.5);

            Thread.sleep(600);

            right.setPower(0);
            left.setPower(0);

            Thread.sleep(500);
        }
    }

    public void runOpMode() throws InterruptedException {
        right = hardwareMap.dcMotor.get("right");
        left = hardwareMap.dcMotor.get("left");
        servo = hardwareMap.servo.get("servo");

        left.setDirection(DcMotor.Direction.REVERSE);

        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        servo.setPosition(0.1);

        waitForStart();

        for(int i = 0; i < 4; i++) {
            right.setPower(0.5);
            left.setPower(0.5);

            Thread.sleep(400);

            servo.setPosition(0.9);

            Thread.sleep(400);

            right.setPower(0);
            left.setPower(0);

            Thread.sleep(500);

            servo.setPosition(0.1);

            Thread.sleep(250);

            turnLeft(3);

            servo.setPosition(0.9);

            Thread.sleep(400);

            right.setPower(0.5);
            left.setPower(0.5);


            right.setPower(0);
            left.setPower(0);
        }
    }
}
