package org.firstinspires.ftc.teamcode.RelicRecovery;


import android.graphics.Bitmap;
import android.hardware.Camera;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.CameraDevice;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * Created by elliot on 1/2/18.
 */

public class Vuforia {

    protected static VuforiaLocalizer vuforia;
    protected static int cameraMonitorViewId;
    protected static VuforiaLocalizer.Parameters parameters;
    protected static VuforiaTrackables relicTrackables;
    protected static VuforiaTrackable relicTemplate;
    protected static Camera camera;
    protected static Camera.Parameters params;
    protected static boolean isFlashOn;

    private Vuforia() throws Exception {
        throw new Exception();
    }

    public static void init (LinearOpMode opMode) {
        final String TAG = "Vuforia VuMark Sample";
        isFlashOn = false;

        opMode.telemetry.update();


        cameraMonitorViewId = opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", opMode.hardwareMap.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);


        parameters.vuforiaLicenseKey = "AbO00Vr/////AAAAGR/4nmv4GEGQu4vW9wgUlT0WNu2/N6zIXjlp37lSukqaDsdyx8iBmehE5IfEYqu/GtzZtKCy0QQzlwjdsrctMkynApbZ24wN4Tju5IzuPGNjXzXA6ATEHyRFk8PuMHk6bRbtuBG2IxkjW3dzxKDAe2d0/dEbqbWYG7EqJ8zqBwl5P/hBucBmzNV0+EXCo7qZSgYROQycO4ZJhWvozkVXjIblEoXlfuKb2guewgkurxdrj87LSmzYoU6L7gKpKsMCVEdYrT3XhJ8h1G9IWhLr+aplqfRk9bMb0v+o9l4gfH+r8Q/kOAtbooVqUlNlTgaR6PimqNzDGvMLw2sRbWOi4n0LYRgg8vV6DAszuK3W0RgX";

        /*
         * We also indicate which camera on the RC that we wish to use.
         * Here we chose the back (HiRes) camera (for greater range), but
         * for a competition robot, the front camera might be more convenient.
         */

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //IDK IF THIS WORKS


        vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
    }

    public static RelicRecoveryVuMark VuMarkID(LinearOpMode opMode) {
        relicTrackables.activate();
        if (!isFlashOn) {
            CameraDevice.getInstance().setFlashTorchMode(true);
            isFlashOn = true;
        }

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);


       // opMode.telemetry.addData("Time", after - before);
        //opMode.telemetry.update();

        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
            opMode.telemetry.addData("VuMark", "%s visible", vuMark);
            opMode.telemetry.update();
            return vuMark;
        }
        return vuMark;
    }

    public static Mat getImage(LinearOpMode opMode){
        //opMode.telemetry.addLine("Entering the loop");
        //opMode.telemetry.update();

//        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
//        params.vuforiaLicenseKey = "AbO00Vr/////AAAAGR/4nmv4GEGQu4vW9wgUlT0WNu2/N6zIXjlp37lSukqaDsdyx8iBmehE5IfEYqu/GtzZtKCy0QQzlwjdsrctMkynApbZ24wN4Tju5IzuPGNjXzXA6ATEHyRFk8PuMHk6bRbtuBG2IxkjW3dzxKDAe2d0/dEbqbWYG7EqJ8zqBwl5P/hBucBmzNV0+EXCo7qZSgYROQycO4ZJhWvozkVXjIblEoXlfuKb2guewgkurxdrj87LSmzYoU6L7gKpKsMCVEdYrT3XhJ8h1G9IWhLr+aplqfRk9bMb0v+o9l4gfH+r8Q/kOAtbooVqUlNlTgaR6PimqNzDGvMLw2sRbWOi4n0LYRgg8vV6DAszuK3W0RgX";
//        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
//
//        VuforiaLocalizer locale = ClassFactory.createVuforiaLocalizer(parameters);
        com.vuforia.Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true); //enables RGB565 format for the image
//        locale.setFrameQueueCapacity(1); //tells VuforiaLocalizer to only store one frame at a time
        vuforia.setFrameQueueCapacity(1);
        Image rgb = null;
        Mat tmp = null;
        //opMode.telemetry.addLine("Set Mat");
        //opMode.telemetry.update();
/*To access the image: you need to iterate through the images of the frame object:*/
        try{
            VuforiaLocalizer.CloseableFrame frame = vuforia.getFrameQueue().take(); //takes the frame at the head of the queue

//            opMode.telemetry.addLine("Got Queue");
//            opMode.telemetry.update();
            long numImages = frame.getNumImages();

//            opMode.telemetry.addLine("Copied pixels to buffer");
//            opMode.telemetry.update();
            for (int i = 0; i < numImages; i++) {
                if (frame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {
                    rgb = frame.getImage(i);
                    Bitmap bm = Bitmap.createBitmap(rgb.getWidth(), rgb.getHeight(), Bitmap.Config.RGB_565);
                    bm.copyPixelsFromBuffer(rgb.getPixels());

//put the image into a MAT for OpenCV
//                    opMode.telemetry.addLine("Copied pixels to buffer");
//                    opMode.telemetry.update();
                    tmp = new Mat(rgb.getWidth(), rgb.getHeight(), CvType.CV_8UC4);
//                    opMode.telemetry.addLine("Created Mat");
//                    opMode.telemetry.update();
                    tmp.convertTo(tmp, CvType.CV_64FC3);
//                    opMode.telemetry.addLine("Converted Mat type");
//                    opMode.telemetry.update();
                    Utils.bitmapToMat(bm, tmp);
//                    opMode.telemetry.addLine("Converted bitmap to mat");
//                    opMode.telemetry.update();
//close the frame, prevents memory leaks and crashing
                    frame.close();
                    break;
                }//if
            }//for
        } catch(InterruptedException ex) {
            System.err.println("An InterruptedException was caught: " + ex.getMessage());
        }
        return tmp;
    }

}
