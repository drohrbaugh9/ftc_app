package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

public final class ShooterPID {

    private static final float shooterKp = 0.001f;
    private static final float shooterKi = 0.0f;
    private static double shooterIntegral1 = 0, shooterIntegral2 = 0;

    private ShooterPID() throws Exception { throw new Exception(); }

    public static double[] PI_Shooter(long tics1, long tics2, double tics_target, double power1, double power2) {
        double error1 = tics_target - tics1, error2 = tics_target - tics2;
        shooterIntegral1 += error1; shooterIntegral2 += error2;
        double adjust1 = shooterKp * error1 + shooterKi * shooterIntegral1;
        double adjust2 = shooterKp * error2 + shooterKi * shooterIntegral2;
        double[] toReturn = {Range.clip(power1 + adjust1, -1, 1), Range.clip(power2 + adjust2, -1, 1)};
        return toReturn;
    }

    public static void resetShooterIntegrals() { shooterIntegral1 = 0; shooterIntegral2 = 0; }
}
