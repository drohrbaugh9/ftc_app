
package org.firstinspires.ftc.teamcode.RelicRecovery;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.teamcode.Util;

/**
 * Created by elliot on 1/3/18.
 */

public class KnockOffJewel {

    protected static NormalizedColorSensor colorSensor;
    protected static View relativeLayout;

    protected static float AmountOfRed;
    protected static float AmountOfBlue;

    private static double power = 0.25;
    private static int rotationSleep = 200;
    private static int stopSleep = 20;

    static double JewelArmServoUp = 0.85;
    static double JewelArmServoDown = 0.28;
    static double JewelArmServoMid = 0.57;

    static double JewelWhackerServoUp = 1;
    static double JewelWhackerServoDown = 0.16;

    static double JewelWhackerServoTowardSensor = 0.00;
    static double JewelWhackerServoAwaySensor = 0.4;


    private KnockOffJewel() throws Exception {
        throw new Exception();
    }

    public static void init(LinearOpMode opMode) throws InterruptedException {
        //Util.init(opMode); // uncomment if running individually

        int relativeLayoutId = opMode.hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", opMode.hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity) opMode.hardwareMap.appContext).findViewById(relativeLayoutId);

        // values is a reference to the hsvValues array.
        float[] hsvValues = new float[3];
        final float values[] = hsvValues;


        // Get a reference to our sensor object.

        colorSensor = opMode.hardwareMap.get(NormalizedColorSensor.class, "BallColorSensor");


        // If possible, turn the light on in the beginning (it might already be on anyway,
        // we just make sure it is if we can).
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight) colorSensor).enableLight(true);
        }
    }

    public static void KnockOffRedJewel(LinearOpMode opMode) throws InterruptedException {
        SetUp(opMode);

        if ((AmountOfRed) < AmountOfBlue) {
            Util.JewelWhackerServo.setPosition(JewelWhackerServoAwaySensor);
            Thread.sleep(500);
            Util.JewelArmServo.setPosition(JewelArmServoUp);
            Thread.sleep(400);
            //put up arm in drive off stone



        } else {

            Util.JewelWhackerServo.setPosition(JewelWhackerServoTowardSensor);
            Thread.sleep(500);
            Util.JewelArmServo.setPosition(JewelArmServoUp);
            Thread.sleep(400);
            //put up arm in drive off stone



        }
    }

    public static void KnockOffBlueJewel(LinearOpMode opMode) throws InterruptedException {
        SetUp(opMode);

        if ((AmountOfRed) < AmountOfBlue) {
            Util.JewelWhackerServo.setPosition(JewelWhackerServoTowardSensor);
            Thread.sleep(500);
            Util.JewelArmServo.setPosition(JewelArmServoUp);
            Thread.sleep(400);
            //put up arm in drive off stone



        }
        else {
            Util.JewelWhackerServo.setPosition(JewelWhackerServoAwaySensor);
            Thread.sleep(500);
            Util.JewelArmServo.setPosition(JewelArmServoUp);
            Thread.sleep(400);
            //put up arm in drive off stone


        }
    }

    protected static void SetUp(LinearOpMode opMode) throws InterruptedException {
        Util.JewelWhackerServo.setPosition(JewelWhackerServoDown);
        Thread.sleep(500);
        Util.JewelArmServo.setPosition(JewelArmServoMid);
        Thread.sleep(200);
        Util.JewelArmServo.setPosition(JewelArmServoDown);
        Thread.sleep(500); //DON'T CHANGE

        // Read the sensor
        NormalizedRGBA colors = colorSensor.getNormalizedColors();


        /** We also display a conversion of the colors to an equivalent Android color integer.
         * @see Color */

        float max = Math.max(Math.max(Math.max(colors.red, colors.green), colors.blue), colors.alpha);
        colors.red /= max;
        colors.green /= max;
        colors.blue /= max;
        int color = colors.toColor();


        opMode.telemetry.addLine("normalized color:  ")
                .addData("a", "%02x", Color.alpha(color))
                .addData("r", "%02x", Color.red(color))
                .addData("g", "%02x", Color.green(color))
                .addData("b", "%02x", Color.blue(color));
        opMode.telemetry.update();

        AmountOfRed = colors.red;
        AmountOfBlue = colors.blue;
    }
}

