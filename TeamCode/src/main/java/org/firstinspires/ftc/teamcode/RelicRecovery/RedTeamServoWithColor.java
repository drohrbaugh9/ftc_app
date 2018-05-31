package org.firstinspires.ftc.teamcode.RelicRecovery;


import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;

@Autonomous(name="Servo and Color for the Red team", group="Test")
@Disabled
public class RedTeamServoWithColor extends LinearOpMode {

    boolean OtherJewel = false;

    Servo servo;

    NormalizedColorSensor colorSensor;
    View relativeLayout;

    @Override
    public void runOpMode() throws InterruptedException {

        servo = hardwareMap.servo.get("servo");
        servo.setPosition(0.25);

        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        runSample(); // actually execute the sample

    }

    protected void runSample() throws InterruptedException {

        // values is a reference to the hsvValues array.
        float[] hsvValues = new float[3];
        final float values[] = hsvValues;


        // Get a reference to our sensor object.

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "ServoColorSensor");


        // If possible, turn the light on in the beginning (it might already be on anyway,
        // we just make sure it is if we can).
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight) colorSensor).enableLight(true);
        }

        boolean XButton = gamepad1.x;

        if (XButton){
            OtherJewel = true;
            telemetry.addLine("MAKE SURE YOU MEAN FOR THIS TO BE TRUE!!!!!");
            telemetry.update();
        }

        // Wait for the start button to be pressed.
        waitForStart();


        // Loop until we are asked to stop
        while (opModeIsActive()) {

            // Read the sensor
            NormalizedRGBA colors = colorSensor.getNormalizedColors();


            /** We also display a conversion of the colors to an equivalent Android color integer.
             * @see Color */

            float max = Math.max(Math.max(Math.max(colors.red, colors.green), colors.blue), colors.alpha);
            colors.red /= max;
            colors.green /= max;
            colors.blue /= max;
            int color = colors.toColor();


            telemetry.addLine("normalized color:  ")
                    .addData("a", "%02x", Color.alpha(color))
                    .addData("r", "%02x", Color.red(color))
                    .addData("g", "%02x", Color.green(color))
                    .addData("b", "%02x", Color.blue(color));
            telemetry.update();


            /**
             * move servo based on color
             */

            float AmountOfRed = colors.red;
            float AmountOfBlue = colors.blue;

            /*
            the color sensor needs to be pointed to the right if facing the wall from the field
             */

            Thread.sleep(3000);
            if (OtherJewel){
                if ((AmountOfRed) > AmountOfBlue) {
                    servo.setPosition((0.5)); //for actual thing the robot will rotate clockwise
                } else {
                    servo.setPosition(0);//rotate counterclockwise
                }
            }
            else {
                if ((AmountOfRed) > AmountOfBlue) {
                    servo.setPosition((0)); //for actual thing the robot will rotate clockwise
                } else {
                    servo.setPosition(0.5);//rotate counterclockwise
                }
                //rotate back to center
            }
        }
    }
}