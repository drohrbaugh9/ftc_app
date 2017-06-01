package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

public final class PID {

    // drive
    private static final float Kp = 0.01f;   //proportional constant      //TODO final tune
    private static final float Ki = 0.0f;    //integral constant     //TODO tune
    private static final int offset = 0;          //value that <gyroHeading> should be
    private static double integral = 0;      //variable to hold integral value (accumulated error)

    //testing
    private static boolean log = false;// FtcRobotControllerActivity.LOG;

    private PID() throws Exception { throw new Exception(); }

    public static double[] P(GyroSensor gyro, double Tp) {
        //TODO factor in battery power
        int heading = heading(gyro);
        int error = heading - offset;
        double turn = Kp * error;
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
        integral += error;
        double turn = Kp * error + Ki * integral;
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
        RobotLog.i("integral: " + integral);
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

    public static int heading(GyroSensor gyro) {
        int heading = gyro.getHeading();
        if (heading > 180) return heading - 360;
        return heading;
        //-179 - 180
    }

    public static void resetDriveIntegral() {
        integral = 0;
    }
}