/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaNavigation;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * This OpMode illustrates the basics of using the VuMarkDetector engine to determine
 * the identity of VuMarkDetector VuMarks encountered on the field. The code is structured as
 * a LinearOpMode. It shares much structure with {@link ConceptVuforiaNavigation}; we do not here
 * duplicate the core VuMarkDetector documentation found there, but rather instead focus on the
 * differences between the use of VuMarkDetector for navigation vs VuMark identification.
 *
 * @see ConceptVuforiaNavigation
 * @see VuforiaLocalizer
 * @see VuforiaTrackableDefaultListener
 * see  ftc_app/doc/tutorial/FTC_FieldCoordinateSystemDefinition.pdf
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own VuMarkDetector license key as
 * is explained in {@link ConceptVuforiaNavigation}.
 */

@Autonomous(name="Concept: Cryptobox id", group ="Concept")
@Disabled
public class CryptoboxVuforia extends LinearOpMode {

    public static final String TAG = "VuMarkDetector VuMark Sample";

    OpenGLMatrix lastLocation = null;

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the VuMarkDetector
     * localization engine.
     */
    VuforiaLocalizer vuforia;

    @Override public void runOpMode() {

        /*
         * To start up VuMarkDetector, tell it the view that we wish to use for camera monitor (on the RC phone);
         * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // OR...  Do Not Activate the Camera Monitor View, to save power
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        /*
         * IMPORTANT: You need to obtain your own license key to use VuMarkDetector. The string below with which
         * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
         * A VuMarkDetector 'Development' license key, can be obtained free of charge from the VuMarkDetector developer
         * web site at https://developer.vuforia.com/license-manager.
         *
         * VuMarkDetector license keys are always 380 characters long, and look as if they contain mostly
         * random data. As an example, here is a example of a fragment of a valid key:
         *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
         * Once you've obtained a license key, copy the string from the VuMarkDetector web site
         * and paste it in to your code onthe next line, between the double quotes.
         */
        parameters.vuforiaLicenseKey = "AbO00Vr/////AAAAGR/4nmv4GEGQu4vW9wgUlT0WNu2/N6zIXjlp37lSukqaDsdyx8iBmehE5IfEYqu/GtzZtKCy0QQzlwjdsrctMkynApbZ24wN4Tju5IzuPGNjXzXA6ATEHyRFk8PuMHk6bRbtuBG2IxkjW3dzxKDAe2d0/dEbqbWYG7EqJ8zqBwl5P/hBucBmzNV0+EXCo7qZSgYROQycO4ZJhWvozkVXjIblEoXlfuKb2guewgkurxdrj87LSmzYoU6L7gKpKsMCVEdYrT3XhJ8h1G9IWhLr+aplqfRk9bMb0v+o9l4gfH+r8Q/kOAtbooVqUlNlTgaR6PimqNzDGvMLw2sRbWOi4n0LYRgg8vV6DAszuK3W0RgX";

        /*
         * We also indicate which camera on the RC that we wish to use.
         * Here we chose the back (HiRes) camera (for greater range), but
         * for a competition robot, the front camera might be more convenient.
         */
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        /**
         * Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
         * in this data set: all three of the VuMarks in the game were created from this one template,
         * but differ in their instance id information.
         * @see VuMarkInstanceId
         */
        VuforiaTrackables cryptoboxTrackables = this.vuforia.loadTrackablesFromAsset("BlueCryptobox");
        VuforiaTrackable Cryptobox = cryptoboxTrackables.get(0);
        //relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        cryptoboxTrackables.get(0).setName("Blue_Cryptobox");

        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();

        cryptoboxTrackables.activate();



        while (opModeIsActive()) {



           for (VuforiaTrackable box : cryptoboxTrackables) {
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)box.getListener()).getPose();
                telemetry.addData("Pose", format(pose));

                /* We further illustrate how to decompose the pose into useful rotational and
                 * translational components */
                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                    telemetry.addData(box.getName() + "-Translation", trans);

                    double degreesToTurn = Math.toDegrees(Math.atan2(trans.get(1), trans.get(2)));

                    telemetry.addData(box.getName() + "-Degrees", degreesToTurn);

                    /* Extract the X, Y, and Z components of the offset of the target relative to the robot
                    double tX = trans.get(0);
                    double tY = trans.get(1);
                    double tZ = trans.get(2);

                    // Extract the rotational components of the target relative to the robot
                    double rX = rot.firstAngle;
                    double rY = rot.secondAngle;
                    double rZ = rot.thirdAngle;
                    */
                }
           }


            telemetry.update();
        }
    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}
