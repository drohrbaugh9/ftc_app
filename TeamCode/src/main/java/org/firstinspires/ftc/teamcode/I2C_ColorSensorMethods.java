package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

public final class I2C_ColorSensorMethods {

    static byte[] redMSB, redLSB, blueLSB, blueMSB;
    static I2cDevice colorC;
    static I2cDeviceSynch colorCreader;

    static final int DEFAULT_THRESHOLD = 30;

    private I2C_ColorSensorMethods() throws Exception {
        throw new Exception();
    }

    public static void init(OpMode opMode) {
        colorC = opMode.hardwareMap.i2cDevice.get("color");
        colorCreader = new I2cDeviceSynchImpl(colorC, I2cAddr.create8bit(0x3c), false);
        colorCreader.engage();

        colorCreader.write8(3, 1);
    }

    public static boolean beaconIsBlue() {
        return beaconIsBlue(DEFAULT_THRESHOLD);
    }

    public static boolean beaconIsBlue(int threshold) {
        return normalizedBlue() > normalizedRed() && normalizedBlue() > threshold;
    }

    public static boolean beaconIsRed() {
        return beaconIsRed(DEFAULT_THRESHOLD);
    }

    public static boolean beaconIsRed(int threshold) {
        return normalizedBlue() < normalizedRed() && normalizedRed() > threshold;
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
}
