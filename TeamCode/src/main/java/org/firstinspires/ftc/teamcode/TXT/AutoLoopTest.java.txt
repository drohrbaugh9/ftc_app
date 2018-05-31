package org.firstinspires.ftc.teamcode;

/* despite the name of this class, it ended up being used in competition
 * the methods in this class are called by a LinearOpMode to execute a
   series of commands while running a loop in the background
 * the driveAndShoot method, which was used for the Velocity Vortex
   season, spins up the robot's shooter motors, drives forward a certain
   distance, and shoots two balls, all while running a PID loop in the
   background controlling the speed of the shooter motors
 * this class makes use of the enum in the AutoStates class in
   AutoStates.java
*/
public class AutoLoopTest {

    static double shooter1Power, shooter2Power;

    static AutoStates state = AutoStates.SHOOTER_SPIN_UP;

    static boolean firstTime = true, PIDon = false;

    public static void driveAndShoot(int driveDistance, int shotNumber) throws InterruptedException {

        state = AutoStates.SHOOTER_SPIN_UP;

        shooter1Power = 0; shooter2Power = 0;

        firstTime = true; PIDon = false;

        AutoUtil.onBeaconPower *= (13.6 / Util.getBatteryVoltage());

        //ShooterPID.realRPMtarget = 1110;
        //ShooterPID.calcuateTicsTarget(1100);

        long start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO, currentTime, oldTime = start - 10;

        while (state != AutoStates.END) {
            currentTime = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
            switch(state) {
                case SHOOTER_SPIN_UP:
                    if (firstTime) {

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        shooter1Power = FinalTeleOp.calculateShooterPower();
                        shooter2Power = shooter1Power + FinalTeleOp.SHOOTER2_OFFSET; // shooter 2 is slower than shooter 1
                        //Util.shooter1.setPower(shooter1Power);
                        //Util.shooter2.setPower(shooter2Power);

                        firstTime = false;
                    }
                    if ((currentTime - start) > 1200) {
                        state = AutoStates.DRIVE_01;
                        Util.setDriveModeFloat();
                        firstTime = true;
                    }
                    break;
                case DRIVE_01:
                    if (firstTime) {

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        Util.setAllPowers(0.1);

                        PIDon = true;
                        firstTime = false;
                    }
                    if ((currentTime - start) > 30) {
                        state = AutoStates.DRIVE_015;
                        firstTime = true;
                    }
                    break;
                case DRIVE_015:
                    if (firstTime) {

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        Util.setAllPowers(0.15);
                        firstTime = false;
                    }
                    if ((currentTime - start) > 75) {
                        state = AutoStates.DRIVE_FULL;
                        firstTime = true;
                    }
                    break;
                case DRIVE_FULL:
                    double startPos = 0;
                    if (firstTime) {

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        AutoUtil.resetGyroHeading(Util.gyro);
                        PID.resetDriveIntegral();
                        startPos = Util.right.getCurrentPosition();

                        firstTime = false;
                    }

                    PID.PIsetMotors(Util.gyro, 0.2);

                    if (Util.right.getCurrentPosition() > (startPos + (driveDistance * 0.98))) {
                        state = AutoStates.DRIVE_COAST;
                        firstTime = true;
                        Util.telemetry("drive time", (start - currentTime));
                    }
                    break;
                case DRIVE_COAST:
                    if (firstTime) {

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        Util.setAllPowers(0);
                        firstTime = false;
                    }
                    if ((currentTime - start) > 800) {
                        state = AutoStates.SHOOT_1;
                        firstTime = true;
                    }
                    break;
                case SHOOT_1:
                    if (firstTime) {

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        //Util.ballFeeder.setPosition(Util.SHOOT);
                        firstTime = false;
                    }
                    if ((currentTime - start) > 500) {
                        if (shotNumber == 1) state = AutoStates.SHOOTER_SPIN_DOWN;
                        else state = AutoStates.LOAD_2;
                        firstTime = true;
                    }
                    break;
                case LOAD_2:
                    if (firstTime) {

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        //Util.ballFeeder.setPosition(Util.LOAD);
                        firstTime = false;
                    }
                    if ((currentTime - start) > 1500) {
                        state = AutoStates.SHOOT_2;
                        firstTime = true;
                    }
                    break;
                case SHOOT_2:
                    if (firstTime) {

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        //Util.ballFeeder.setPosition(Util.SHOOT);
                        firstTime = false;
                    }
                    if ((currentTime - start) > 500) {
                        state = AutoStates.SHOOTER_SPIN_DOWN;
                        firstTime = true;
                    }
                    break;
                case SHOOTER_SPIN_DOWN:
                    if (firstTime) {

                        //Util.shooter1.setPower(0); Util.shooter2.setPower(0);

                        PIDon = false;
                        firstTime = false;
                        state = AutoStates.END;
                    }
                    break;
                case END:
                    break;
            }

            //ShooterPID.manageEncoderData(currentTime - oldTime);
            oldTime = currentTime;

            /*if (PIDon) {
                double[] powers = ShooterPID.PID_calculateShooterPower(shooter1Power, shooter2Power);
                shooter1Power = powers[0];
                shooter2Power = powers[1];
                Util.shooter1.setPower(shooter1Power);
                Util.shooter2.setPower(shooter2Power);
            }*/

            Thread.sleep(5);

            // oversample
            //Util.shooter1.getCurrentPosition();
            //Util.shooter2.getCurrentPosition();

            Thread.sleep(5);
        }

        Util.setDriveModeBrake();

        //Util.ballFeeder.setPosition(Util.LOAD);
    }

    public static void driveAndShootFasterTest(int driveDistance, int shotNumber) throws InterruptedException {

        state = AutoStates.SHOOTER_SPIN_UP;

        shooter1Power = 0; shooter2Power = 0;

        firstTime = true; PIDon = false;

        AutoUtil.onBeaconPower *= (13.6 / Util.getBatteryVoltage());

        //ShooterPID.realRPMtarget = 1030;
        //ShooterPID.calcuateTicsTarget(1030);

        long start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO, currentTime, oldTime = start - 10;

        while (state != AutoStates.END) {
            currentTime = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
            switch(state) {
                case SHOOTER_SPIN_UP:
                    if (firstTime) {

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        shooter1Power = FinalTeleOp.calculateShooterPower();
                        shooter2Power = shooter1Power + FinalTeleOp.SHOOTER2_OFFSET; // shooter 2 is slower than shooter 1
                        //Util.shooter1.setPower(shooter1Power);
                        //Util.shooter2.setPower(shooter2Power);

                        firstTime = false;
                    }
                    if ((currentTime - start) > 1200) {
                        state = AutoStates.DRIVE_01;
                        Util.setDriveModeFloat();
                        firstTime = true;
                    }
                    break;
                case DRIVE_01:
                    if (firstTime) {

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        Util.setAllPowers(0.1);

                        PIDon = true;
                        firstTime = false;
                    }
                    if ((currentTime - start) > 30) {
                        state = AutoStates.DRIVE_015;
                        firstTime = true;
                    }
                    break;
                case DRIVE_015:
                    if (firstTime) {

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        Util.setAllPowers(0.15);
                        firstTime = false;
                    }
                    if ((currentTime - start) > 75) {
                        state = AutoStates.DRIVE_FULL;
                        firstTime = true;
                    }
                    break;
                case DRIVE_FULL:
                    double startPos = 0;
                    if (firstTime) {

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        AutoUtil.resetGyroHeading(Util.gyro);
                        PID.resetDriveIntegral();
                        startPos = Util.right.getCurrentPosition();

                        firstTime = false;
                    }

                    PID.PIsetMotors(Util.gyro, 0.2);

                    if (Util.right.getCurrentPosition() > (startPos + (driveDistance * 0.98))) {
                        state = AutoStates.DRIVE_COAST;
                        firstTime = true;
                        Util.telemetry("drive time", (start - currentTime));
                    }
                    break;
                case DRIVE_COAST:
                    if (firstTime) {

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        Util.setAllPowers(0);
                        firstTime = false;
                    }
                    if ((currentTime - start) > 500) {
                        state = AutoStates.SHOOT_1;
                        firstTime = true;
                    }
                    break;
                case SHOOT_1:
                    if (firstTime) {

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        //Util.ballFeeder.setPosition(Util.SHOOT);
                        firstTime = false;
                    }
                    if ((currentTime - start) > 350) {
                        if (shotNumber == 1) state = AutoStates.SHOOTER_SPIN_DOWN;
                        else state = AutoStates.LOAD_2;
                        firstTime = true;
                    }
                    break;
                case LOAD_2:
                    if (firstTime) {

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        //Util.ballFeeder.setPosition(Util.LOAD);
                        firstTime = false;
                    }
                    if ((currentTime - start) > 600) {
                        state = AutoStates.SHOOT_2;
                        firstTime = true;
                    }
                    break;
                case SHOOT_2:

                    if (firstTime) {

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        //Util.ballFeeder.setPosition(Util.SHOOT);
                        firstTime = false;
                    }
                    if ((currentTime - start) > 350) {
                        state = AutoStates.SHOOTER_SPIN_DOWN;
                        firstTime = true;
                    }
                    break;
                case SHOOTER_SPIN_DOWN:
                    if (firstTime) {

                        //Util.shooter1.setPower(0); Util.shooter2.setPower(0);

                        PIDon = false;
                        firstTime = false;
                        state = AutoStates.END;
                    }
                    break;
                case END:
                    break;
            }

            //ShooterPID.manageEncoderData(currentTime - oldTime);
            oldTime = currentTime;

            /*if (PIDon) {
                double[] powers = ShooterPID.PID_calculateShooterPower(shooter1Power, shooter2Power);
                shooter1Power = powers[0];
                shooter2Power = powers[1];
                Util.shooter1.setPower(shooter1Power);
                Util.shooter2.setPower(shooter2Power);
            }*/

            Thread.sleep(5);

            // oversample
            //Util.shooter1.getCurrentPosition();
            //Util.shooter2.getCurrentPosition();

            Thread.sleep(5);
        }

        Util.setDriveModeBrake();

        //Util.ballFeeder.setPosition(Util.LOAD);
    }
}
