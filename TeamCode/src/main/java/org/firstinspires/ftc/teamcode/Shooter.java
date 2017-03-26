package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.LinkedList;
import java.util.Queue;

@Autonomous(name="Shooter", group="Test")
@Disabled
public class Shooter extends LinearOpMode {

    DcMotor shooter1, shooter2;
    Servo ballFeeder, s;

    final int MEASURING_INTERVAL = 10, MOVING_AVERAGE_LENGTH = 50;
    final double RPM_TARGET = 1250.0;
    final double TICS_PER_ROTATION = Util.NEVEREST_37_TICS_PER_ROTATION;
    final double TICS_TARGET = ((RPM_TARGET / 60.0) / (MEASURING_INTERVAL * 10.0)) * TICS_PER_ROTATION; // tics per MEASURING_INTERVAL, is 46.25 if target is 1250
    final String DEBUG = "SHOOTER ";

    private static Queue<Integer> shooter1Queue, shooter2Queue;

    public void runOpMode() throws InterruptedException {
        Util.linearOpMode = this;

        ballFeeder = hardwareMap.servo.get("ballFeeder"); ballFeeder.setPosition(Util.LOAD);
        s = hardwareMap.servo.get("upDown"); s.setPosition(Util.BEACON_UP);

        shooter1 = hardwareMap.dcMotor.get("shooter1");
        shooter2 = hardwareMap.dcMotor.get("shooter2"); shooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        DcMotor[] temp = new DcMotor[2];
        temp[0] = shooter1;
        temp[1] = shooter2;

        Util.resetEncoders(this, temp);

        shooter1Queue = new LinkedList<Integer>();
        shooter2Queue = new LinkedList<Integer>();

        for (int i = 0; i < MOVING_AVERAGE_LENGTH; i++) {
            shooter1Queue.add(0);
            shooter2Queue.add(0);
        }

        long delta1, delta2;
        long deltat, time, start, old;

        Util.telemetry("tics_target", TICS_TARGET, true);

        boolean shoot1 = true, shoot2 = true, load1 = true;

        waitForStart();

        ShooterPID.resetShooterIntegrals();

        double cp = 0.24; // FinalTeleOp.calculateShooterPower();
        double shooter1Power = cp, shooter2Power = cp + 0.07;

        shooter1.setPower(shooter1Power);
        shooter2.setPower(shooter2Power);

        Thread.sleep(1500); //1500);

        telemetry.addData("PID status", "PID engaged");
        telemetry.update();

        for (int i = 0; i < 50; i++) {

            manageEncoderData();

            Thread.sleep(10);
        }

        start = System.nanoTime();
        old = start;

        while((old - start) / 1000000000 < 10) {

            manageEncoderData();

            delta1 = shooter1Sum / MOVING_AVERAGE_LENGTH;
            delta2 = shooter2Sum / MOVING_AVERAGE_LENGTH;

            time = System.nanoTime();
            deltat = time - old;

            Util.log(DEBUG + "RPM1: " + ((delta1 * 1000000000 * 60) / (deltat * 103.6)));
            Util.log(DEBUG + "RPM2: " + ((delta2 * 1000000000 * 60) / (deltat * 103.6)));

            double[] powers = ShooterPID.PI_Shooter(delta1, delta2, TICS_TARGET, shooter1Power, shooter2Power);

            shooter1Power = powers[0];
            shooter2Power = powers[1];

            Util.telemetry("power1", shooter1Power, false);
            Util.telemetry("power2", shooter2Power, true);

            shooter1.setPower(shooter1Power);
            shooter2.setPower(shooter2Power);

            if (shoot1 && (time - start) > 3 * 1000000000.0) {
                ballFeeder.setPosition(Util.SHOOT);
                shoot1 = false;
            }

            if (load1 && (time - start) > 3.4 * 1000000000.0) {
                ballFeeder.setPosition(Util.LOAD);
                load1 = false;
            }

            if (shoot2 && (time - start) > 6 * 1000000000.0) {
                ballFeeder.setPosition(Util.SHOOT);
                shoot2 = false;
            }

            old = time;

            Thread.sleep(MEASURING_INTERVAL);
        }


        ballFeeder.setPosition(Util.LOAD);

        Thread.sleep(1000);
    }

    int shooter1Diff, shooter2Diff;
    int shooter1Sum, shooter2Sum;
    int shooter1Pos, shooter2Pos;
    int lastShooter1Pos = 0, lastShooter2Pos = 0;

    public void manageEncoderData() {
        shooter1Pos = shooter1.getCurrentPosition();
        shooter2Pos = shooter2.getCurrentPosition();

        shooter1Diff = Math.abs(shooter1Pos - lastShooter1Pos);
        shooter2Diff = Math.abs(shooter2Pos - lastShooter2Pos);

        shooter1Sum = shooter1Sum + shooter1Diff - shooter1Queue.poll();
        shooter1Queue.add(shooter1Diff);
        shooter2Sum = shooter2Sum + shooter2Diff - shooter2Queue.poll();
        shooter2Queue.add(shooter2Diff);

        lastShooter1Pos = shooter1Pos;
        lastShooter2Pos = shooter2Pos;
    }
}