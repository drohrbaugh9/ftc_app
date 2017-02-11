package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

import java.util.LinkedList;
import java.util.Queue;

public final class ShooterPID {

    private static final float shooterKp = 0.00005f; // Ku = 0.0001
    private static final float shooterKi = 0.0f;

    static final double MOVING_AVERAGE_LENGTH = 50, MEASURING_INTERVAL = 10;
    static final double RPM_TARGET = 1150.0; // 1250.0
    static final double TICS_PER_ROTATION = Util.NEVEREST_37_TICS_PER_ROTATION;
    static final double TICS_TARGET = TICS_PER_ROTATION * (RPM_TARGET / 60.0) * (MEASURING_INTERVAL / 1000.0); // tics per MEASURING_INTERVAL, is 46.25 if target is 1250

    private static double shooterIntegral1 = 0, shooterIntegral2 = 0;

    private static Queue<Double> shooter1Queue, shooter2Queue;
    private static Queue<Long> elapsedTimeQueue;

    private static boolean queueClear;

    private ShooterPID() throws Exception { throw new Exception(); }

    public static void init() { clearQueue(); }

    public static double[] PID_calculateShooterPower(double power1, double power2) {
        double delta1 = shooter1Sum / MOVING_AVERAGE_LENGTH;
        double delta2 = shooter2Sum / MOVING_AVERAGE_LENGTH;
        //long deltat = timeSum / MOVING_AVERAGE_LENGTH;

        //double ticsTarget = ((RPM_TARGET / 60.0) / elapsedTime) * TICS_PER_ROTATION;

        //                  tics per rotation * rotations per second * seconds
        /*double ticsTarget = TICS_PER_ROTATION * (RPM_TARGET / 60.0) * (deltat / 1000.0);
        Util.telemetry("ticsTarget", ticsTarget, false);*/
        Util.telemetry("delta1", delta1, false);
        Util.telemetry("delta2", delta2, false);
        Util.telemetry("shooter1Sum", shooter1Sum, true);

        return PI_Shooter(delta1, delta2, TICS_TARGET, power1, power2);
    }

    public static double[] PI_Shooter(double tics1, double tics2, double tics_target, double power1, double power2) {
        double error1 = tics_target - tics1, error2 = tics_target - tics2;
        shooterIntegral1 += error1; shooterIntegral2 += error2;
        double adjust1 = shooterKp * error1 + shooterKi * shooterIntegral1;
        double adjust2 = shooterKp * error2 + shooterKi * shooterIntegral2;
        double[] toReturn = {Range.clip(power1 + adjust1, 0, 1), Range.clip(power2 + adjust2, 0, 1)};
        return toReturn;
    }

    public static void resetShooterIntegrals() { shooterIntegral1 = 0; shooterIntegral2 = 0; }

    private static double shooter1Diff, shooter2Diff;
    private static double shooter1Sum, shooter2Sum;
    private static int shooter1Pos, shooter2Pos;
    private static int lastShooter1Pos = 0, lastShooter2Pos = 0;

    private static long timeSum;

    public static void manageEncoderData(double elapsedTime) {
        shooter1Pos = Util.shooter1.getCurrentPosition();
        shooter2Pos = Util.shooter2.getCurrentPosition();

        shooter1Diff = Math.abs(shooter1Pos - lastShooter1Pos);
        shooter2Diff = Math.abs(shooter2Pos - lastShooter2Pos);

        shooter1Diff = (MEASURING_INTERVAL / elapsedTime) * shooter1Diff;
        shooter2Diff = (MEASURING_INTERVAL / elapsedTime) * shooter2Diff;

        shooter1Sum = shooter1Sum + shooter1Diff - shooter1Queue.poll();
        shooter1Queue.add(shooter1Diff);
        shooter2Sum = shooter2Sum + shooter2Diff - shooter2Queue.poll();
        shooter2Queue.add(shooter2Diff);
        /*timeSum = timeSum + elapsedTime - elapsedTimeQueue.poll();
        elapsedTimeQueue.add(elapsedTime);*/

        lastShooter1Pos = shooter1Pos;
        lastShooter2Pos = shooter2Pos;

        Util.log("SHOOTER " + "RPM1: " + ((shooter1Diff * 1000 * 60) / (elapsedTime * 103.6)));
        Util.log("SHOOTER " + "RPM2: " + ((shooter2Diff * 1000 * 60) / (elapsedTime * 103.6)));

        queueClear = false;
    }

    public static void clearQueue() {
        if (queueClear) return;

        shooter1Queue = new LinkedList<>();
        shooter2Queue = new LinkedList<>();
        //elapsedTimeQueue = new LinkedList<>();

        shooter1Sum = 0;
        shooter2Sum = 0;
        timeSum = 0;

        for (int i = 0; i < MOVING_AVERAGE_LENGTH; i++) {
            shooter1Queue.add(0.0);
            shooter2Queue.add(0.0);
            //elapsedTimeQueue.add((long)0);
        }

        queueClear = true;
    }
}
