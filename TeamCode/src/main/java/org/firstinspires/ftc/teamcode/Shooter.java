package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="Shooter Test", group="Test")
//@Disabled
public class Shooter extends LinearOpMode {

    private final String DEBUG = "SHOOTER ";

    DcMotor shooter1, shooter2;

    public void runOpMode() throws InterruptedException {
        shooter1 = hardwareMap.dcMotor.get("shooter1");
        shooter2 = hardwareMap.dcMotor.get("shooter2"); shooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        DcMotor[] temp = new DcMotor[2];
        temp[0] = shooter1;
        temp[1] = shooter2;

        Util.resetEncoders(this, temp);

        waitForStart();

        int i = 0;

        shooter1.setPower(0.24);
        shooter2.setPower(0.24);

        //Thread.sleep(3000);

        //while (!gamepad1.a);

        int oldPos1 = shooter1.getCurrentPosition(), oldPos2 = shooter2.getCurrentPosition(), currentPos1, currentPos2;

        long start = System.nanoTime();

        while (i < 2000) {
            /*double stick = gamepad1.right_stick_y;
            if (stick < 0) stick = 0;

            shooter1.setPower(stick * 0.24);
            shooter2.setPower(stick * 0.24);*/

            currentPos1 = shooter1.getCurrentPosition();
            currentPos2 = shooter2.getCurrentPosition();

            long time = System.nanoTime();

            Util.log(DEBUG + "time: " + (time - start));
            Util.log(DEBUG + "1: " + (currentPos1 - oldPos1));
            Util.log(DEBUG + "2: " + (currentPos2 - oldPos2));
            //Util.log(DEBUG + "i: " + i);

            oldPos1 = currentPos1;
            oldPos2 = currentPos2;

            i++;

            Thread.sleep(2);
        }

        long end = System.nanoTime();

        Util.log(DEBUG + "time: " + (end - start));
        Util.log(DEBUG + "measurements / sec: " + (3000 / ((end - start) / 1000000000.0)));
        telemetry.addData("measurements / sec", (3000 / ((end - start) / 1000000000.0)));
        telemetry.addData("elapsed time", end - start);
        telemetry.update();

        shooter1.setPower(0);
        shooter2.setPower(0);

        while (opModeIsActive());
    }
}
