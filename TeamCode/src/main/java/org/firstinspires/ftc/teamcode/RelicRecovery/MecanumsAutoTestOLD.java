package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;
import java.util.Locale;

/**
 * Created by elliot on 10/28/17.
 */

//this is the very messy verson of Autonomous.java

@Autonomous (name = "Mecanums Auto", group = "Test")
@Disabled
public class MecanumsAutoTestOLD extends LinearOpMode {


    DcMotor[] array = new DcMotor[4];

    public static final String TAG = "VuMarkDetector VuMark Sample";

    OpenGLMatrix lastLocation = null;

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the VuMarkDetector
     * localization engine.
     */
    VuforiaLocalizer vuforia;

    DcMotor rightFront;
    DcMotor leftFront;
    DcMotor leftBack;
    DcMotor rightBack;

    //IMU stuff
    BNO055IMU imu;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;

//    public void squareUpWithCryptoBox() throws InterruptedException {
//        Move.startForward(0.3, 959, true); //get of stone
//        Thread.sleep(500);
//        Move.startBackward(0.3, 317, true); //square up with stone
//        Thread.sleep(500);
//        Move.rotateCounterClockwise(45); //turn toward glyph pit
//        Thread.sleep(500);
//        Move.startBackward(0.3, 197, true);//get into glyph pit
//        Thread.sleep(500);
//        Move.rotateCounterClockwise(24);//turn towards cyrptobox
//        Thread.sleep(500);
//        Move.startBackward(0.3, 718, true);//move towards cryptobox
//        Thread.sleep(500);
//        Move.rotateCounterClockwise(24);
//        Thread.sleep(500);
//    }

    public void runOpMode() throws InterruptedException {

        rightBack = hardwareMap.dcMotor.get("rightBack");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftFront = hardwareMap.dcMotor.get("leftFront");

        array[0] = rightFront;
        array[1] = rightBack;
        array[2] = leftBack;
        array[3] = leftFront;

        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //servo = hardwareMap.servo.get("servo");

        //servo.setPosition(servoPos);

        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.REVERSE);

        /*
         * To start up VuMarkDetector, tell it the view that we wish to use for camera monitor (on the RC phone);
         * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);


        parameters.vuforiaLicenseKey = "AbO00Vr/////AAAAGR/4nmv4GEGQu4vW9wgUlT0WNu2/N6zIXjlp37lSukqaDsdyx8iBmehE5IfEYqu/GtzZtKCy0QQzlwjdsrctMkynApbZ24wN4Tju5IzuPGNjXzXA6ATEHyRFk8PuMHk6bRbtuBG2IxkjW3dzxKDAe2d0/dEbqbWYG7EqJ8zqBwl5P/hBucBmzNV0+EXCo7qZSgYROQycO4ZJhWvozkVXjIblEoXlfuKb2guewgkurxdrj87LSmzYoU6L7gKpKsMCVEdYrT3XhJ8h1G9IWhLr+aplqfRk9bMb0v+o9l4gfH+r8Q/kOAtbooVqUlNlTgaR6PimqNzDGvMLw2sRbWOi4n0LYRgg8vV6DAszuK3W0RgX";

        /*
         * We also indicate which camera on the RC that we wish to use.
         * Here we chose the back (HiRes) camera (for greater range), but
         * for a competition robot, the front camera might be more convenient.
         */
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        /**
         * Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
         * in this data set: all three of the VuMarks in the game were created from this one template,
         * but differ in their instance id information.
         * @see VuMarkInstanceId
         */
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        telemetry.update();


        Util.init(this);
        Util.resetEncoders(array);

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters params = new BNO055IMU.Parameters();
        params.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        params.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        params.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        params.loggingEnabled      = true;
        params.loggingTag          = "IMU";
        params.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(params);

        /**
        if I continue to get errors with the imu init try moving the whole block of code to the beginning of the whole opmode
        see forum posts
        https://ftcforum.usfirst.org/forum/ftc-technology/53534-imu-initialization-time
        https://ftcforum.usfirst.org/forum/ftc-technology/56611-rev-imu-not-initializing-if-battery-too-low
         */

        // Set up our telemetry dashboard
        composeTelemetry();

        waitForStart();

        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

        relicTrackables.activate();

        while (opModeIsActive()) {

            /**
             * See if any of the instances of {@link relicTemplate} are currently visible.
             * {@link RelicRecoveryVuMark} is an enum which can have the following values:
             * UNKNOWN, LEFT, CENTER, and RIGHT. When a VuMark is visible, something other than
             * UNKNOWN will be returned by {@link RelicRecoveryVuMark#from(VuforiaTrackable)}.
             */
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);


            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
                telemetry.addData("VuMark", "%s visible", vuMark);

                // save for when we go to pick up a glyph

                /*Move.startForward(0.3, 959, true); //get off stone
                Thread.sleep(500);
                Move.startBackward(0.3, 317, true); //square up with stone
                Thread.sleep(500);
                Move.startForward(0.3, 197, true); // move so not touching stone
                Thread.sleep(500);
                Move.rotateCounterClockwise(45); //turn toward glyph pit
                Thread.sleep(500);
                Move.startForward(0.3, 718, true);//get into glyph pit
                Thread.sleep(500);
                Move.rotateCounterClockwise(24);//turn towards cyrptobox
                Thread.sleep(500);
                Move.startBackward(0.3, 1640, true);//move towards cryptobox - moved to far (might have fixed by moving further into glyph pit)
                Thread.sleep(500);
                Move.rotateCounterClockwise(24);
                Thread.sleep(500);*/

                Move.startForward(0.3, 475, true); //get off stone - go less
                Thread.sleep(500);
                Move.rotateCounterClockwise(90); //face cryptobox
                Thread.sleep(500);
                rightBack.setPower(-0.5);
                rightFront.setPower(0.5);
                leftBack.setPower(0.5);
                leftFront.setPower(-0.5);
                Thread.sleep(200);
                Util.setDriveModeFloat();
                Util.setAllPowers(0);
                Thread.sleep(500);
                Util.setDriveModeBrake();
                Move.startBackward(0.3, 540, true); //move toward cryptobox
                Thread.sleep(500);

                if (vuMark == RelicRecoveryVuMark.CENTER) {
                    Move.startLeft(0.5, 75, true); // test to see if data is stored
                    break;
                }

                if (vuMark == RelicRecoveryVuMark.RIGHT) {
                    Move.startLeft(0.5, 143, true);
                    break;
                }

                if (vuMark == RelicRecoveryVuMark.LEFT) {
                    break;
                }
                Thread.sleep(500);


               /* *//* For fun, we also exhibit the navigational pose. In the Relic Recovery game,
                 * it is perhaps unlikely that you will actually need to act on this pose information, but
                 * we illustrate it nevertheless, for completeness. *//*
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
                telemetry.addData("Pose", format(pose));

                *//* We further illustrate how to decompose the pose into useful rotational and
                 * translational components *//*
                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                    // Extract the X, Y, and Z components of the offset of the target relative to the robot
                    double tX = trans.get(0);
                    double tY = trans.get(1);
                    double tZ = trans.get(2);

                    // Extract the rotational components of the target relative to the robot
                    double rX = rot.firstAngle;
                    double rY = rot.secondAngle;
                    double rZ = rot.thirdAngle;
                }*/

                 // don't need this pose stuff but keeping for fun
            }
            else {
                telemetry.addData("VuMark", "not visible");
            }

            telemetry.update();

        }
    }

    void composeTelemetry() {
        telemetry.addAction(new Runnable() { @Override public void run()
        {
            // Acquiring the angles is relatively expensive; we don't want
            // to do that in each of the three items that need that info, as that's
            // three times the necessary expense.
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = imu.getGravity();
        }
        });

        telemetry.addLine()
                .addData("heading", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
                .addData("roll", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }


}
