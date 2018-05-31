package org.firstinspires.ftc.teamcode.RelicRecovery;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

/**
 * Created by elliot on 1/4/18.
 */

public class ColorSensor {

    protected static NormalizedColorSensor [] array = new NormalizedColorSensor[3];

    protected static NormalizedColorSensor RightColorSensor;
    protected static NormalizedColorSensor LeftColorSensor;
    protected static NormalizedColorSensor BallColorSensor;

    protected static NormalizedRGBA RightColors;
    protected static NormalizedRGBA LeftColors;
    protected static NormalizedRGBA BallColors;

    public static int RightColor;
    public static int LeftColor;
    protected static int BallColor;

    private ColorSensor() throws Exception {
        throw new Exception();
    }

    public static void init(LinearOpMode opMode) {
        // values is a reference to the hsvValues array.
        float[] hsvValues = new float[3];
        final float values[] = hsvValues;

        // Get a reference to our sensor object.

        RightColorSensor = opMode.hardwareMap.get(NormalizedColorSensor.class, "RightColorSensor");
        LeftColorSensor = opMode.hardwareMap.get(NormalizedColorSensor.class, "LeftColorSensor");
        BallColorSensor = opMode.hardwareMap.get(NormalizedColorSensor.class, "BallColorSensor");

        array [0] = RightColorSensor;
        array [1] = LeftColorSensor;
        array [2] = BallColorSensor;

        // If possible, turn the light on in the beginning (it might already be on anyway,
        // we just make sure it is if we can).
        for (Object colorsensor : array) {
            if (colorsensor instanceof SwitchableLight) {
                ((SwitchableLight) colorsensor).enableLight(true);
            }
        }

    }

    public static void ReadSensor(LinearOpMode opMode) {
        RightColors = RightColorSensor.getNormalizedColors();
        LeftColors = LeftColorSensor.getNormalizedColors();
        BallColors = BallColorSensor.getNormalizedColors();

        /** We also display a conversion of the colors to an equivalent Android color integer.
         * @see Color */


        float Rightmax = Math.max(Math.max(Math.max(RightColors.red, RightColors.green), RightColors.blue), RightColors.alpha);
        RightColors.red   /= Rightmax;
        RightColors.green /= Rightmax;
        RightColors.blue  /= Rightmax;
        RightColor = RightColors.toColor();
        float Leftmax = Math.max(Math.max(Math.max(LeftColors.red, LeftColors.green), LeftColors.blue), LeftColors.alpha);
        LeftColors.red   /= Leftmax;
        LeftColors.green /= Leftmax;
        LeftColors.blue  /= Leftmax;
        LeftColor = LeftColors.toColor();
        float Ballmax = Math.max(Math.max(Math.max(BallColors.red, BallColors.green), BallColors.blue), BallColors.alpha);
        BallColors.red   /= Ballmax;
        BallColors.green /= Ballmax;
        BallColors.blue  /= Ballmax;
        BallColor = BallColors.toColor();


//        opMode.telemetry.addLine("normalized color (Right):  ")
//                .addData("a", Color.alpha(RightColor))
//                .addData("r", Color.red(RightColor))
//                .addData("g", Color.green(RightColor))
//                .addData("b", Color.blue(RightColor));
//        opMode.telemetry.addLine("normalized color (Left):  ")
//                .addData("a", Color.alpha(LeftColor))
//                .addData("r", Color.red(LeftColor))
//                .addData("g", Color.green(LeftColor))
//                .addData("b", Color.blue(LeftColor));
//        opMode.telemetry.addLine("normalized color (Ball):  ")
//                .addData("a", Color.alpha(BallColor))
//                .addData("r", Color.red(BallColor))
//                .addData("g", Color.green(BallColor))
//                .addData("b", Color.blue(BallColor));
//        opMode.telemetry.update();
    }
}
