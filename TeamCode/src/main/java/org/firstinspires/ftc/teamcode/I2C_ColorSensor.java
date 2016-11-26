package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

public class I2C_ColorSensor {

    private static byte[] redMSB, redLSB, blueLSB, blueMSB;
    private static I2cDevice color;
    private static I2cDeviceSynch colorCreader;

    private static int threshold;

    final static int DEFAULT_THRESHOLD = 30;

    public I2C_ColorSensor(OpMode opmode) {
        init(opmode);
        setThreshold(DEFAULT_THRESHOLD);
    }

    public I2C_ColorSensor(OpMode opmode, int t) {
        init(opmode);
        threshold = t;
    }

    public static void init(OpMode opMode) {
        color = opMode.hardwareMap.i2cDevice.get("color");
        colorCreader = new I2cDeviceSynchImpl(color, I2cAddr.create8bit(0x3c), false);
        colorCreader.engage();

        colorCreader.write8(3, 1);
    }

    public static boolean beaconIsBlue() {
        return beaconIsBlue(threshold);
    }

    public static boolean beaconIsBlue(int t) {
        return normalizedBlue() > normalizedRed() && normalizedBlue() > t;
    }

    public static boolean beaconIsRed() {
        return beaconIsRed(threshold);
    }

    public static boolean beaconIsRed(int t) {
        return normalizedBlue() < normalizedRed() && normalizedRed() > t;
    }

    public static int normalizedBlue() {
        blueMSB = colorCreader.read(0x1B, 1);
        blueLSB = colorCreader.read(0x1A, 1);
        int blueMSBval = blueMSB[0] & 0xFF;
        int blueLSBval = blueLSB[0] & 0xFF;
        return (blueMSBval * 256) + blueLSBval;
    }

    public static int normalizedRed() {
        redMSB = colorCreader.read(0x17, 1);
        redLSB = colorCreader.read(0x16, 1);
        int redMSBval = redMSB[0] & 0xFF;
        int redLSBval = redLSB[0] & 0xFF;
        return (redMSBval * 256) + redLSBval;
    }

    public static void setThreshold(int t) { threshold = t; }
}
