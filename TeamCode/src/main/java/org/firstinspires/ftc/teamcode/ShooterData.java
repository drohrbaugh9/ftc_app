package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

//@TeleOp(name="ShooterData", group="Test")
@Autonomous(name="TestWithData", group="Test")
//@Disabled
public class ShooterData extends LinearOpMode {

    private final String DEBUG = "SHOOTER ";

    private DcMotor shooter1, shooter2, r1, l1, r2, l2, in;
    private Servo ballFeeder, upDown;

    public void runOpMode() throws InterruptedException {
        shooter1 = hardwareMap.dcMotor.get("shooter1");
        shooter2 = hardwareMap.dcMotor.get("shooter2"); shooter2.setDirection(DcMotorSimple.Direction.REVERSE);
        ballFeeder = hardwareMap.servo.get("ballFeeder");

        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        DcMotor[] temp = new DcMotor[2];
        temp[0] = shooter1;
        temp[1] = shooter2;

        Util.resetEncoders(this, temp);

        Util.linearOpMode = this;

        ballFeeder.setPosition(Util.LOAD);

        waitForStart();

        int i = 1;
        double power = 0.3;
        boolean shooterStart = true, shoot1 = true, shoot2 = true, load1 = true;

        //Util.log(DEBUG + "battery voltage: " + Util.getBatteryVoltage());

        /*shooter1.setPower(power);
        shooter2.setPower(power + 0.07);*/

        //while (!gamepad1.a);

        int oldPos1 = shooter1.getCurrentPosition(), oldPos2 = shooter2.getCurrentPosition(), currentPos1, currentPos2;

        long delta1 = 0, delta2 = 0;
        long deltat = 0;

        long start = System.nanoTime();
        long old = start;

        while (i < (700 + 1)) {
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
            Util.log(DEBUG + "batteryVoltage: " + Util.getBatteryVoltage());
            Util.log(DEBUG + "RPM1: " + ((delta1 * 1000000000 * 60) / (deltat * 44.4)));
            Util.log(DEBUG + "RPM2: " + ((delta2 * 1000000000 * 60) / (deltat * 44.4)));

            oldPos1 = currentPos1;
            oldPos2 = currentPos2;
            old = time;

            /*if (i % 100 == 0) {
                power += 0.01;
                shooter1.setPower(power);
                shooter2.setPower(power + 0.07);
            }*/

            /*if (change && ((time - start) > 2000)) {
                shooter1.setPower(0.24);
                shooter2.setPower(0.24 + 0.07);
                change = false;
            }*/

            if (shooterStart && (time - start) > 1000000000) {
                shooter1.setPower(power);
                shooter2.setPower(power + 0.07);
                shooterStart = false;
            }

            if (shoot1 && (time - start) > 3 * 1000000000.0) {
                ballFeeder.setPosition(Util.SHOOT);
                shoot1 = false;
            }

            if (load1 && (time - start) > 3.4 * 1000000000.0) {
                ballFeeder.setPosition(Util.LOAD);
                load1 = false;
            }

            if (shoot2 && (time - start) > 5 * 1000000000.0) {
                ballFeeder.setPosition(Util.SHOOT);
                shoot2 = false;
            }

            i++;

            Thread.sleep(10);
        }

        long end = System.nanoTime();

        shooter1.setPower(0);
        shooter2.setPower(0);

        ballFeeder.setPosition(Util.LOAD);

        Util.log(DEBUG + "time: " + (end - start));
        Util.log(DEBUG + "measurements / sec: " + ((i - 2) / ((end - start) / 1000000000.0)));
        /*telemetry.addData("measurements / sec", (i / ((end - start) / 1000000000.0)));
        telemetry.addData("elapsed time", end - start);
        telemetry.addData(" ", " ");
        telemetry.addData("last deltat", deltat);
        telemetry.addData("last delta1", delta1);
        telemetry.addData("motor 1 tics / second", (1000000000 * delta1) / deltat);
        telemetry.addData("motor 1 tics / minute", (1000000000 * delta1 * 60) / deltat);
        telemetry.addData("motor 1 RPM", ((1000000000 * delta1 * 60) / (deltat * 44.5)));
        telemetry.update();*/

        //while (opModeIsActive());
    }

    public void runThatOpMode() throws InterruptedException { // renamed to runThatOpMode() to not conflict with newer runOpMode()
        shooter1 = hardwareMap.dcMotor.get("shooter1");
        shooter2 = hardwareMap.dcMotor.get("shooter2"); shooter2.setDirection(DcMotorSimple.Direction.REVERSE);
        ballFeeder = hardwareMap.servo.get("ballFeeder");
        upDown = hardwareMap.servo.get("upDown");

        r1 = Util.getMotor(hardwareMap, "rightBack"); r1.setDirection(DcMotorSimple.Direction.REVERSE);
        l1 = Util.getMotor(hardwareMap, "leftBack");
        r2 = Util.getMotor(hardwareMap, "rightFront"); r2.setDirection(DcMotorSimple.Direction.REVERSE);
        l2 = Util.getMotor(hardwareMap, "leftFront");

        in = Util.getMotor(hardwareMap, "intake");

        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        DcMotor[] temp = new DcMotor[2];
        temp[0] = shooter1;
        temp[1] = shooter2;

        Util.resetEncoders(this, temp);

        Util.linearOpMode = this;

        ballFeeder.setPosition(Util.LOAD);

        upDown.setPosition(Util.BEACON_UP);

        waitForStart();

        int i = 1;
        double power = 0.18;
        boolean shooterStart = true, motorsStart = true; //, shoot1 = true, shoot2 = true, load1 = true;

        //Util.log(DEBUG + "battery voltage: " + Util.getBatteryVoltage());

        /*shooter1.setPower(power);
        shooter2.setPower(power + 0.07);*/

        //while (!gamepad1.a);

        int oldPos1 = shooter1.getCurrentPosition(), oldPos2 = shooter2.getCurrentPosition(), currentPos1, currentPos2;

        long delta1 = 0, delta2 = 0;
        long deltat = 0;

        //long start = System.nanoTime();
        //long old = start;

        /*while (i < ((2 * 60 * 10) + 1)) {
            /*double stick = gamepad1.right_stick_y;
            if (stick < 0) stick = 0;

            shooter1.setPower(stick * 0.24);
            shooter2.setPower(stick * 0.24);*//*

            currentPos1 = shooter1.getCurrentPosition();
            currentPos2 = shooter2.getCurrentPosition();

            delta1 = currentPos1 - oldPos1;
            delta2 = currentPos2 - oldPos2;

            long time = System.nanoTime();

            deltat = time - old;

            //Util.log(DEBUG + "time: " + (time - start));
            //Util.log(DEBUG + "1: " + delta1);
            //Util.log(DEBUG + "2: " + delta2);
            Util.log(DEBUG + "batteryVoltage: " + Util.getBatteryVoltage());
            Util.log(DEBUG + "RPM1: " + ((delta1 * 1000000000 * 60) / (deltat * 44.4)));
            Util.log(DEBUG + "RPM2: " + ((delta2 * 1000000000 * 60) / (deltat * 44.4)));

            oldPos1 = currentPos1;
            oldPos2 = currentPos2;
            old = time;

            /*if (i % 100 == 0) {
                power += 0.01;
                shooter1.setPower(power);
                shooter2.setPower(power + 0.07);
            }*/

            /*if (change && ((time - start) > 2000)) {
                shooter1.setPower(0.24);
                shooter2.setPower(0.24 + 0.07);
                change = false;
            }*//*

            if (shooterStart && (time - start) > 1000000000) {
                shooter1.setPower(power);
                shooter2.setPower(power + 0.07);
                shooterStart = false;
            }

            if (motorsStart && (time - start) > 3 * 1000000000.0) {
                r1.setPower(-0.2); r2.setPower(-0.2);
                l1.setPower(0.2);  l2.setPower(0.2);
                //in.setPower(0.7);
                motorsStart = false;
            }

            i++;

            Thread.sleep(100);
        }*/

        Util.log(DEBUG + "batteryVoltage: " + Util.getBatteryVoltage());

        shooter1.setPower(power);
        shooter2.setPower(power + 0.07);

        Thread.sleep(3000);

        oldPos1 = shooter1.getCurrentPosition();
        oldPos2 = shooter2.getCurrentPosition();

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

            if (i % 10 == 0) {
                power += 0.001;
                shooter1.setPower(power);
                shooter2.setPower(power + 0.07);
            }

            i++;

            Thread.sleep(10);
        }

        long end = System.nanoTime();

        shooter1.setPower(0);
        shooter2.setPower(0);

        ballFeeder.setPosition(Util.LOAD);

        Util.log(DEBUG + "time: " + (end - start));
        Util.log(DEBUG + "measurements / sec: " + ((i - 2) / ((end - start) / 1000000000.0)));
        /*telemetry.addData("measurements / sec", (i / ((end - start) / 1000000000.0)));
        telemetry.addData("elapsed time", end - start);

        telemetry.addData(" ", " ");

        telemetry.addData("last deltat", deltat);
        telemetry.addData("last delta1", delta1);
        telemetry.addData("motor 1 tics / second", (1000000000 * delta1) / deltat);
        telemetry.addData("motor 1 tics / minute", (1000000000 * delta1 * 60) / deltat);
        telemetry.addData("motor 1 RPM", ((1000000000 * delta1 * 60) / (deltat * 44.5)));
        telemetry.update();*/

        //while (opModeIsActive());
    }
}
