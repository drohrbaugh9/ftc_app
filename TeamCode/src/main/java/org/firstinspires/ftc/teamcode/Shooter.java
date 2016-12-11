package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.LinkedList;
import java.util.Queue;

@Autonomous(name="Shooter", group="Test")
//@Disabled
public class Shooter extends LinearOpMode {

    DcMotor shooter1, shooter2;
    final int RPM_TARGET = 1250, MOVING_AVERAGE_LENGTH = 10, MEASURING_INTERVAL = 20;
    final double TICS_PER_ROTATION = Util.NEVEREST_37_TICS_PER_ROTATION;
    final double TICS_TARGET = ((RPM_TARGET / 60) / MEASURING_INTERVAL) * TICS_PER_ROTATION; // tics per MEASURING_INTERVAL


    private static Queue<Integer> shooter1Queue, shooter2Queue;

    public void runOpMode() throws InterruptedException {
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

        waitForStart();

        for(double i = 0.1; i < 0.36; i+=0.01) {
            shooter1.setPower(i); shooter2.setPower(i);
            Thread.sleep(20);
        }

        double shooter1Power = 0.36, shooter2Power = 0.36;

        int oldPos1 = shooter1.getCurrentPosition(), oldPos2 = shooter2.getCurrentPosition(), currentPos1, currentPos2;

        int delta1, delta2;
        long deltat;

        while(opModeIsActive()) {
            long time = System.nanoTime();

            manageEncoderData();

            delta1 = shooter1Sum / MOVING_AVERAGE_LENGTH;
            delta2 = shooter2Sum / MOVING_AVERAGE_LENGTH;

            Util.log("SHOOTER ticsDelta1: " + delta1);
            Util.log("SHOOTER ticsDelta2: " + delta2);

            double[] powers = ShooterPID.PI_Shooter(delta1, delta2, TICS_TARGET, shooter1Power, shooter2Power);

            shooter1.setPower(powers[0]);
            shooter2.setPower(powers[1]);

            Thread.sleep(MEASURING_INTERVAL);
        }
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