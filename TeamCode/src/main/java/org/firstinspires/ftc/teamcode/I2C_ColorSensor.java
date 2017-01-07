package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

public class I2C_ColorSensor {

    private static byte[] redMSB, redLSB, blueLSB, blueMSB;
    private static I2cDevice color1, color2;
    protected static I2cDeviceSynch synch1, synch2;

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
        color1 = opMode.hardwareMap.i2cDevice.get("color1");
        synch1 = new I2cDeviceSynchImpl(color1, I2cAddr.create8bit(0x3c), false);
        synch1.engage();

        color2 = opMode.hardwareMap.i2cDevice.get("color2");
        synch2 = new I2cDeviceSynchImpl(color2, I2cAddr.create8bit(0x3e), false);
        synch2.engage();

        synch1.write8(3, 1);
        synch2.write8(3, 1);
    }

    public static boolean beaconIsBlue(I2cDeviceSynch synch) { return beaconIsBlue(synch, threshold); }

    public static boolean beaconIsBlue(I2cDeviceSynch synch, int t) {
        return normalizedBlue(synch) > normalizedRed(synch) && normalizedBlue(synch) > t;
    }

    public static boolean beaconIsRed(I2cDeviceSynch synch) { return beaconIsRed(synch, threshold); }

    public static boolean beaconIsRed(I2cDeviceSynch synch, int t) {
        return normalizedBlue(synch) < normalizedRed(synch) && normalizedRed(synch) > t;
    }

    public static int normalizedBlue(I2cDeviceSynch synch) {
        blueMSB = synch.read(0x1B, 1);
        blueLSB = synch.read(0x1A, 1);
        int blueMSBval = blueMSB[0] & 0xFF;
        int blueLSBval = blueLSB[0] & 0xFF;
        return (blueMSBval * 256) + blueLSBval;
    }

    public static int normalizedRed(I2cDeviceSynch synch) {
        redMSB = synch.read(0x17, 1);
        redLSB = synch.read(0x16, 1);
        int redMSBval = redMSB[0] & 0xFF;
        int redLSBval = redLSB[0] & 0xFF;
        return (redMSBval * 256) + redLSBval;
    }

    public static void setThreshold(int t) { threshold = t; }
}
