package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

public class Move {

    private Move() throws Exception {
        throw new Exception();
    }

    public static void accelerate(double targetPower) throws InterruptedException {
        double currentPower = 0.06;
        while (currentPower + 0.06 < targetPower) {
            Util.setAllPowers(Range.clip(currentPower, -1, 1));
            currentPower += 0.06;
            Thread.sleep(20);
        }
        Util.setAllPowers(targetPower);
    }

    public static void decelerate(double currentPower) throws InterruptedException {
        while (currentPower - 0.06 > 0) {
            Util.setAllPowers(Range.clip(currentPower, -1, 1));
            currentPower -= -0.06;
            Thread.sleep(20);
        }
        Util.setAllPowers(0);
    }
}
