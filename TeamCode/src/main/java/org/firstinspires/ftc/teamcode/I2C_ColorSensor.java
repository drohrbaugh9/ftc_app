package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

public class I2C_ColorSensor {

    private static byte[] redMSB, redLSB, blueLSB, blueMSB;
    private static I2cDevice colorFront, colorBack;
    protected static I2cDeviceSynch synchBack, synchFront;
    protected static I2cController backController, frontController;
    protected static I2cController.I2cPortReadyCallback backCallback, frontCallback;

    private static int threshold;
    protected static boolean enabled = true;

    final static int DEFAULT_THRESHOLD = 30;

    public I2C_ColorSensor(OpMode opmode) {
        init(opmode);
    }

    public I2C_ColorSensor(OpMode opmode, int t) {
        init(opmode);
        threshold = t;
    }

    public static void init(OpMode opMode) {
        setThreshold(DEFAULT_THRESHOLD);

        colorBack = opMode.hardwareMap.i2cDevice.get("colorBack");
        backController = colorBack.getI2cController();
        backCallback = backController.getI2cPortReadyCallback(colorBack.getPort());
        synchBack = new I2cDeviceSynchImpl(colorBack, I2cAddr.create8bit(0x3c), false);
        synchBack.engage();

        colorFront = opMode.hardwareMap.i2cDevice.get("colorFront");
        frontController = colorFront.getI2cController();
        frontCallback = frontController.getI2cPortReadyCallback(colorFront.getPort());
        synchFront = new I2cDeviceSynchImpl(colorFront, I2cAddr.create8bit(0x3e), false);
        synchFront.engage();

        //synchBack.write8(3, 1);
        //synchFront.write8(3, 1);
    }

    public static boolean beaconIsRedBlue() { return frontRed() || backBlue(); }

    public static boolean beaconIsRedRed() { return frontRed() && backRed(); }

    public static boolean beaconIsBlueRed() { return frontBlue() || backRed(); }

    public static boolean beaconIsBlueBlue() { return frontBlue() && backBlue(); }

    public static boolean frontRed() { return frontRed(threshold); }

    public static boolean frontRed(int t) {
        double red = normalizedRed(synchFront);
        double blue = normalizedBlue(synchFront);
        return (blue < red) && (red > t);
    }

    public static boolean frontBlue() { return frontBlue(threshold); }

    public static boolean frontBlue(int t) {
        double red = normalizedRed(synchFront);
        double blue = normalizedBlue(synchFront);
        return (red < blue) && (blue > t);
    }

    public static boolean backRed() { return backRed(threshold); }

    public static boolean backRed(int t) {
        double red = normalizedRed(synchBack);
        double blue = normalizedBlue(synchBack);
        return (blue < red) && (red > t);
    }

    public static boolean backBlue() { return backBlue(threshold); }

    public static boolean backBlue(int t) {
        double red = normalizedRed(synchBack);
        double blue = normalizedBlue(synchBack);
        return (red < blue) && (blue > t);
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

    public static int frontBlueVal() {
        return normalizedBlue(synchFront);
    }

    public static int frontRedVal() {
        return normalizedRed(synchFront);
    }

    public static int backBlueVal() {
        return normalizedBlue(synchBack);
    }

    public static int backRedVal() {
        return normalizedRed(synchBack);
    }

    public static double blueFrontRatio() {
        return (double)frontBlueVal() / (double)frontRedVal();
    }

    public static double redFrontRatio() {
        return (double)frontRedVal() / (double)frontBlueVal();
    }

    public static double blueBackRatio() {
        return (double)backBlueVal() / (double)backRedVal();
    }

    public static double redBackRatio() {
        return (double)backRedVal() / (double)backBlueVal();
    }

    public static void setThreshold(int t) { threshold = t; }

    public static void enable() {
        if (!enabled) {
            if (backCallback != null) {
                backController.registerForI2cPortReadyCallback(backCallback, colorBack.getPort());
            }
            if (frontCallback != null) {
                frontController.registerForI2cPortReadyCallback(frontCallback, colorFront.getPort());
            }

            synchBack.engage();
            synchFront.engage();

            enabled = true;
        }
    }

    public static void disable() {
        if (enabled) {
            backController.deregisterForPortReadyCallback(colorBack.getPort());
            frontController.deregisterForPortReadyCallback(colorFront.getPort());
            enabled = false;
        }

        synchBack.disengage();
        synchFront.disengage();
    }
}
