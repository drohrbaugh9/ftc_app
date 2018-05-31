package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

public class I2C_ColorSensor {

    private byte[] redMSB, redLSB, greenLSB, greenMSB, blueLSB, blueMSB;
    private I2cDevice color;
    protected I2cDeviceSynch synch;
    protected I2cController controller;
    protected I2cController.I2cPortReadyCallback callback;

    protected boolean enabled = true;

    public I2C_ColorSensor() {}

    // get the sensor from the hardwareMap and set up the associated components
    public void get(OpMode opmode, String deviceName, int address) {
        color = opmode.hardwareMap.i2cDevice.get(deviceName);
        controller = color.getI2cController();
        callback = controller.getI2cPortReadyCallback(color.getPort());
        synch = new I2cDeviceSynchImpl(color, I2cAddr.create8bit(address), false);
        synch.engage();
    }

    // read the normalized red value
    public int normalizedRed() {
        redMSB = synch.read(0x17, 1);
        redLSB = synch.read(0x16, 1);
        int redMSBval = redMSB[0] & 0xFF;
        int redLSBval = redLSB[0] & 0xFF;
        return (redMSBval * 256) + redLSBval;
    }

    // read the normalized green value
    public int normalizedGreen() {
        greenMSB = synch.read(0x19, 1);
        greenLSB = synch.read(0x18, 1);
        int greenMSBval = greenMSB[0] & 0xFF;
        int greenLSBval = greenLSB[0] & 0xFF;
        return (greenMSBval * 256) + greenLSBval;
    }

    // read the normalized blue value
    public int normalizedBlue() {
        blueMSB = synch.read(0x1B, 1);
        blueLSB = synch.read(0x1A, 1);
        int blueMSBval = blueMSB[0] & 0xFF;
        int blueLSBval = blueLSB[0] & 0xFF;
        return (blueMSBval * 256) + blueLSBval;
    }

    public void LED_On() { synch.write8(3, 0); }

    public void LED_Off() { synch.write8(3, 1); }

    /* When the color sensors are enabled, they significantly slow down all the other sensors
     * These methods allow for enabling and disabling the color sensors on the fly
     */
    public void enable() {
        if (!enabled && callback != null) {
            controller.registerForI2cPortReadyCallback(callback, color.getPort());
            synch.engage();
            enabled = true;
        }
    }

    public void disable() {
        if (enabled) {
            controller.deregisterForPortReadyCallback(color.getPort());
            synch.disengage();
            enabled = false;
        }
    }
}
