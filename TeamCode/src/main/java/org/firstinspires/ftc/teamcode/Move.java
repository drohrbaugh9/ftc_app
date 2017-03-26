package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

public class Move {

    private Move() throws Exception {
        throw new Exception();
    }

    public static void accelerateForward(double targetPower) throws InterruptedException {
        double currentPower = 0.06;
        while ((currentPower + 0.06) < targetPower) {
            Util.setAllPowers(Range.clip(currentPower, -1, 1));
            currentPower += 0.06;
            Thread.sleep(20);
        }
        Util.setAllPowers(targetPower);
    }

    public static void decelerateForward(double currentPower) throws InterruptedException {
        currentPower -= 0.06;
        while ((currentPower - 0.06) > 0) {
            Util.setAllPowers(Range.clip(currentPower, -1, 1));
            currentPower -= 0.06;
            Thread.sleep(100);
        }
        Util.setAllPowers(0);
    }

    public static void accelerateBackward(double targetPower) throws InterruptedException {
        targetPower = Math.abs(targetPower);
        double currentPower = -0.06;
        while ((currentPower - 0.06) > (-targetPower)) {
            Util.setAllPowers(Range.clip(currentPower, -1, 1));
            currentPower -= 0.06;
            Thread.sleep(20);
        }
        Util.setAllPowers(-targetPower);
    }

    public static void decelerateBackward(double currentPower) throws InterruptedException {
        currentPower = -Math.abs(currentPower) + 0.06;
        while ((currentPower + 0.06) < 0) {
            Util.setAllPowers(Range.clip(currentPower, -1, 1));
            currentPower += 0.06;
            Thread.sleep(100);
        }
        Util.setAllPowers(0);
    }

    public static void startForward(double power, int dist, boolean stop) throws InterruptedException {
        int pos = Util.rightBack.getCurrentPosition();
        accelerateForward(power);
        while ((Util.rightBack.getCurrentPosition() - pos) < dist) Thread.sleep(100);
        if (stop) decelerateForward(power);
    }

    public static void startBackward(double power, int dist, boolean stop) throws InterruptedException {
        int pos = Util.rightBack.getCurrentPosition();
        accelerateBackward(power);
        while ((pos - Util.rightBack.getCurrentPosition()) < dist) Thread.sleep(100);
        if (stop) decelerateBackward(power);
    }
}
