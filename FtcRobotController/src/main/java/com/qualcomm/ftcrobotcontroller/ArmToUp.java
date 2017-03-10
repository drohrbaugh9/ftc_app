package com.qualcomm.ftcrobotcontroller;

public class ArmToUp  {

    public static void armMoveUp(double distance, double power) {

        double start = Util.arm.getCurrentPosition();
        Util.arm.setPower(power);
        while (Util.arm.getCurrentPosition() < (start + (distance * 0.98)));
        Util.setAllPowers(0);
    }

    public static void armMoveDown(double distance, double power) {

        double start = Util.arm.getCurrentPosition();
        Util.arm.setPower(-power);
        while (Util.arm.getCurrentPosition() > (start + (distance * 0.98)));
        Util.linearOpMode.telemetry.addData("arm pos", Util.arm.getCurrentPosition());
        Util.setAllPowers(0);
    }
}
