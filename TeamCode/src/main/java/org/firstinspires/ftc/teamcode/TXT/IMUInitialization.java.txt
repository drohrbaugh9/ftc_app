package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * Created by elliot on 1/3/18.
 */

public class IMUInitialization {
    protected static BNO055IMU imu;

    protected static Orientation angles;
    protected static Acceleration gravity;

    protected static BNO055IMU.Parameters params;

    private IMUInitialization() throws Exception {
        throw new Exception();
    }

    public static void init(LinearOpMode opMode) {
        params = new BNO055IMU.Parameters();
        params.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        params.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        params.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        params.loggingEnabled      = true;
        params.loggingTag          = "IMU";
        params.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = opMode.hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(params);
    }

    public static void startAccelerationIntegration(){
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
    }
}
