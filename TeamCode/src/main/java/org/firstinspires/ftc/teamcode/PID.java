package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

public final class PID {

    // drive
    private static final float driveKp = 0.04f;   //proportional constant      //TODO final tune
    private static final float driveKi = 0.0f;    //driveIntegral constant     //TODO tune
    private static final int offset = 0;          //value that <gyroHeading> should be
    private static double driveIntegral = 0;      //variable to hold driveIntegral value (accumulated error)

    // shooter
    private static final float shooterKp = 0.005f;
    private static final float shooterKi = 0.0f;
    private static double shooterIntegral1 = 0, shooterIntegral2 = 0;

    //testing
    private static boolean log = false;// FtcRobotControllerActivity.LOG;

    private PID() throws Exception { throw new Exception(); }

    public static double[] P(GyroSensor gyro, double Tp) {
        //TODO factor in battery power
        int heading = heading(gyro);
        int error = heading - offset;
        double turn = driveKp * error;
        double[] toReturn = {Range.clip(Tp - turn, -1, 1), Range.clip(Tp + turn, -1, 1)};

        if (!log) {
            return toReturn;
        }

        //logging
        RobotLog.i("-----------P start-----------");
        RobotLog.i("Tp (power) " + Tp);
        RobotLog.i("gyro heading: " + gyro.getHeading());
        RobotLog.i("scaled heading: " + heading);
        RobotLog.i("error: " + error);
        RobotLog.i("turn: " + turn);
        RobotLog.i("right power: " + toReturn[0]);
        RobotLog.i("left power: " + toReturn[1]);
        RobotLog.i("-----------P end-----------");

        return toReturn;
    }

    public static double[] PI(GyroSensor gyro, double Tp) {
        //TODO factor in battery power
        int heading = heading(gyro);
        int error = heading - offset;
        driveIntegral += error;
        double turn = driveKp * error + driveKi * driveIntegral;
        double[] toReturn = {Range.clip(Tp + turn, -1, 1), Range.clip(Tp - turn, -1, 1)};

        if (!log) {
            return toReturn;
        }

        //logging
        RobotLog.i("-----------P start-----------");
        RobotLog.i("Tp (power) " + Tp);
        RobotLog.i("gyro heading: " + gyro.getHeading());
        RobotLog.i("scaled heading: " + heading);
        RobotLog.i("error: " + error);
        RobotLog.i("integral: " + driveIntegral);
        RobotLog.i("turn: " + turn);RobotLog.i("right power: " + toReturn[0]);
        RobotLog.i("left power: " + toReturn[1]);
        RobotLog.i("-----------P end-----------");

        return toReturn;
    }

    public static void PsetMotors(GyroSensor gyro, double Tp) {
        double[] motors = P(gyro, Tp);
        Util.setRightPowers(motors[0]);
        Util.setLeftPowers(motors[1]);
    }

    public static void PIsetMotors(GyroSensor gyro, double Tp) {
        double motors[] = PI(gyro, Tp);
        Util.setRightPowers(motors[0]);
        Util.setLeftPowers(motors[1]);
    }

    public static double[] PI_Shooter(int tics1, int tics2, double tics_target, double power1, double power2) {
        double error1 = tics1 - tics_target, error2 = tics2 - tics_target;
        shooterIntegral1 += error1; shooterIntegral2 += error2;
        double adjust1 = shooterKp * error1 + shooterKi * shooterIntegral1;
        double adjust2 = shooterKp * error2 + shooterKi * shooterIntegral2;
        double[] toReturn = {Range.clip(power1 + adjust1, -1, 1), Range.clip(power2 + adjust2, -1, 1)};
        return toReturn;
    }

    public static int heading(GyroSensor gyro) {
        int heading = gyro.getHeading();
        if (heading > 180) return heading - 360;
        return heading;
        //-179 - 180
    }

    public static void resetDriveIntegral() {
        driveIntegral = 0;
    }

    public static void resetShooterIntegrals() { shooterIntegral1 = 0; shooterIntegral2 = 0; }
}