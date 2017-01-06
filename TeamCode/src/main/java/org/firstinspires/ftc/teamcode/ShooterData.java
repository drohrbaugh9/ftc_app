package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="ShooterData", group="Test")
//@Disabled
public class ShooterData extends LinearOpMode {

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

        int i = 1;
        double power = 0.24;

        //telemetry.addData("battery voltage", Util.getBatteryVoltage());

        shooter1.setPower(power);
        shooter2.setPower(power + 0.07);

        Thread.sleep(3000);

        //while (!gamepad1.a);

        int oldPos1 = shooter1.getCurrentPosition(), oldPos2 = shooter2.getCurrentPosition(), currentPos1, currentPos2;

        long delta1 = 0, delta2 = 0;
        long deltat = 0;

        long start = System.nanoTime();
        long old = start;

        while (i < 1301) {
            /*double stick = gamepad1.right_stick_y;
            if (stick < 0) stick = 0;

            shooter1.setPower(stick * 0.24);
            shooter2.setPower(stick * 0.24);*/

            currentPos1 = shooter1.getCurrentPosition();
            currentPos2 = shooter2.getCurrentPosition();

            delta1 = currentPos1 - oldPos1;
            delta2 = currentPos2 - oldPos2;

            long time = System.nanoTime();

            deltat = time - old;

            //Util.log(DEBUG + "time: " + (time - start));
            //Util.log(DEBUG + "1: " + delta1);
            //Util.log(DEBUG + "2: " + delta2);
            Util.log(DEBUG + "power: " + power);
            Util.log(DEBUG + "RPM1: " + ((delta1 * 1000000000 * 60) / (deltat * 44.4)));
            Util.log(DEBUG + "RPM2: " + ((delta2 * 1000000000 * 60) / (deltat * 44.4)));

            oldPos1 = currentPos1;
            oldPos2 = currentPos2;
            old = time;

            if (i % 100 == 0) {
                power += 0.01;
                shooter1.setPower(power);
                shooter2.setPower(power + 0.07);
            }

            i++;

            Thread.sleep(10);
        }

        long end = System.nanoTime();

        Util.log(DEBUG + "time: " + (end - start));
        Util.log(DEBUG + "measurements / sec: " + (i / ((end - start) / 1000000000.0)));
        /*telemetry.addData("measurements / sec", (i / ((end - start) / 1000000000.0)));
        telemetry.addData("elapsed time", end - start);

        telemetry.addData(" ", " ");

        telemetry.addData("last deltat", deltat);
        telemetry.addData("last delta1", delta1);
        telemetry.addData("motor 1 tics / second", (1000000000 * delta1) / deltat);
        telemetry.addData("motor 1 tics / minute", (1000000000 * delta1 * 60) / deltat);
        telemetry.addData("motor 1 RPM", ((1000000000 * delta1 * 60) / (deltat * 44.5)));
        telemetry.update();*/

        shooter1.setPower(0);
        shooter2.setPower(0);

        //while (opModeIsActive());
    }
}
