package org.firstinspires.ftc.teamcode;

//@Autonomous(name = "AutoLoopTest", group ="Test")
//@Disabled

//public class AutoLoopTest extends LinearOpMode {
public class AutoLoopTest {

    // motors
    //DcMotor rightBack, leftBack, rightFront, leftFront;
    //static DcMotor shooter1, shooter2;
    //DcMotor[] driveMotors, shooterMotors;

    /*int driveDistance = 1900;
    int shotNumber = 2;*/

    static double shooter1Power, shooter2Power;

    //static GyroSensor gyro;

    static AutoStates state = AutoStates.SHOOTER_SPIN_UP;

    static boolean firstTime = true, PIDon = false;

    /*public void runOpMode() throws InterruptedException {

        Util.colorSensors = true; Util.otherSensors = true; Util.servos = true;
        Util.init(this);

        I2C_ColorSensor.disable();

        this.rightBack = Util.rightBack; this.leftBack = Util.leftBack;
        this.rightFront = Util.rightFront; this.leftFront = Util.leftFront;

        driveMotors = new DcMotor[4]; driveMotors[0] = this.rightBack; driveMotors[1] = this.leftBack; driveMotors[2] = this.rightFront; driveMotors[3] = this.leftFront;

        this.shooter1 = Util.shooter1; this.shooter2 = Util.shooter2;

        shooterMotors = new DcMotor[2]; shooterMotors[0] = this.shooter1; shooterMotors[1] = this.shooter2;

        this.gyro = Util.gyro;

        Util.resetEncoders(this, driveMotors);
        Util.resetEncoders(this, shooterMotors);

        ShooterPID.init();

        waitForStart();
    }*/

    public static void driveAndShoot(int driveDistance, int shotNumber) throws InterruptedException {

        state = AutoStates.SHOOTER_SPIN_UP;

        shooter1Power = 0; shooter2Power = 0;

        firstTime = true; PIDon = false;

        AutoUtil.onBeaconPower *= (13.6 / Util.getBatteryVoltage());

        ShooterPID.realRPMtarget = 1080;
        ShooterPID.calcuateTicsTarget(1080);

        long start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO, currentTime, oldTime = start - 10;

        while (state != AutoStates.END) {
            currentTime = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
            switch(state) {
                case SHOOTER_SPIN_UP:
                    if (firstTime) {
                        //telemetry.addData("state", "SHOOTER_SPIN_UP");

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        shooter1Power = FinalTeleOp.calculateShooterPower();
                        shooter2Power = shooter1Power + FinalTeleOp.SHOOTER2_OFFSET; // shooter 2 is slower than shooter 1
                        Util.shooter1.setPower(shooter1Power); // + 0.01);
                        Util.shooter2.setPower(shooter2Power); // + 0.01);

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
                        //telemetry.addData("state", "DRIVE_01");

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
                        //telemetry.addData("state", "DRIVE_015");

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
                        //telemetry.addData("state", "DRIVE_FULL");

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        AutoUtil.resetGyroHeading(Util.gyro);
                        PID.resetDriveIntegral();
                        startPos = Util.rightBack.getCurrentPosition();

                        firstTime = false;
                    }

                    PID.PIsetMotors(Util.gyro, 0.2);

                    if (Util.rightBack.getCurrentPosition() > (startPos + (driveDistance * 0.98))) {
                        state = AutoStates.DRIVE_COAST;
                        firstTime = true;
                        Util.telemetry("drive time", (start - currentTime));
                    }
                    break;
                case DRIVE_COAST:
                    if (firstTime) {
                        //telemetry.addData("state", "DRIVE_COAST");

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
                        //telemetry.addData("state", "SHOOT_1");

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        Util.ballFeeder.setPosition(Util.SHOOT);
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
                        //telemetry.addData("state", "LOAD_2");

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        Util.ballFeeder.setPosition(Util.LOAD);
                        firstTime = false;
                    }
                    if ((currentTime - start) > 1500) {
                        state = AutoStates.SHOOT_2;
                        firstTime = true;
                    }
                    break;
                case SHOOT_2:
                    if (firstTime) {
                        //telemetry.addData("state", "SHOOT_2");

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        Util.ballFeeder.setPosition(Util.SHOOT);
                        firstTime = false;
                    }
                    if ((currentTime - start) > 500) {
                        state = AutoStates.SHOOTER_SPIN_DOWN;
                        firstTime = true;
                    }
                    break;
                case SHOOTER_SPIN_DOWN:
                    if (firstTime) {
                        //telemetry.addData("state", "SHOOTER_SPIN_DOWN");

                        Util.shooter1.setPower(0); Util.shooter2.setPower(0);

                        PIDon = false;
                        firstTime = false;
                        state = AutoStates.END;
                    }
                    break;
                case END:
                    //telemetry.addData("state", "END");
                    break;
            }

            ShooterPID.manageEncoderData(currentTime - oldTime);
            oldTime = currentTime;

            if (PIDon) {
                double[] powers = ShooterPID.PID_calculateShooterPower(shooter1Power, shooter2Power);
                shooter1Power = powers[0];
                shooter2Power = powers[1];
                Util.shooter1.setPower(shooter1Power); // + 0.005);
                Util.shooter2.setPower(shooter2Power); // + 0.005);
            }

            Thread.sleep(5);

            // oversample
            Util.shooter1.getCurrentPosition();
            Util.shooter2.getCurrentPosition();

            Thread.sleep(5);
        }

        Util.setDriveModeBrake();

        Util.ballFeeder.setPosition(Util.LOAD);

        /*telemetry.update();

        while(opModeIsActive()) Thread.sleep(100);*/
    }

    public static void driveAndShootFasterTest(int driveDistance, int shotNumber) throws InterruptedException {

        state = AutoStates.SHOOTER_SPIN_UP;

        shooter1Power = 0; shooter2Power = 0;

        firstTime = true; PIDon = false;

        AutoUtil.onBeaconPower *= (13.6 / Util.getBatteryVoltage());

        ShooterPID.realRPMtarget = 1030;
        ShooterPID.calcuateTicsTarget(1030);

        long start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO, currentTime, oldTime = start - 10;

        while (state != AutoStates.END) {
            currentTime = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
            switch(state) {
                case SHOOTER_SPIN_UP:
                    if (firstTime) {
                        //telemetry.addData("state", "SHOOTER_SPIN_UP");

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        shooter1Power = FinalTeleOp.calculateShooterPower();
                        shooter2Power = shooter1Power + FinalTeleOp.SHOOTER2_OFFSET; // shooter 2 is slower than shooter 1
                        Util.shooter1.setPower(shooter1Power); // + 0.01);
                        Util.shooter2.setPower(shooter2Power); // + 0.01);

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
                        //telemetry.addData("state", "DRIVE_01");

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
                        //telemetry.addData("state", "DRIVE_015");

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
                        //telemetry.addData("state", "DRIVE_FULL");

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        AutoUtil.resetGyroHeading(Util.gyro);
                        PID.resetDriveIntegral();
                        startPos = Util.rightBack.getCurrentPosition();

                        firstTime = false;
                    }

                    PID.PIsetMotors(Util.gyro, 0.2);

                    if (Util.rightBack.getCurrentPosition() > (startPos + (driveDistance * 0.98))) {
                        state = AutoStates.DRIVE_COAST;
                        firstTime = true;
                        Util.telemetry("drive time", (start - currentTime));
                    }
                    break;
                case DRIVE_COAST:
                    if (firstTime) {
                        //telemetry.addData("state", "DRIVE_COAST");

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
                        //telemetry.addData("state", "SHOOT_1");

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        Util.ballFeeder.setPosition(Util.SHOOT);
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
                        //telemetry.addData("state", "LOAD_2");

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        Util.ballFeeder.setPosition(Util.LOAD);
                        firstTime = false;
                    }
                    if ((currentTime - start) > 600) {
                        state = AutoStates.SHOOT_2;
                        firstTime = true;
                    }
                    break;
                case SHOOT_2:
                    if (firstTime) {
                        //telemetry.addData("state", "SHOOT_2");

                        start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
                        Util.ballFeeder.setPosition(Util.SHOOT);
                        firstTime = false;
                    }
                    if ((currentTime - start) > 350) {
                        state = AutoStates.SHOOTER_SPIN_DOWN;
                        firstTime = true;
                    }
                    break;
                case SHOOTER_SPIN_DOWN:
                    if (firstTime) {
                        //telemetry.addData("state", "SHOOTER_SPIN_DOWN");

                        Util.shooter1.setPower(0); Util.shooter2.setPower(0);

                        PIDon = false;
                        firstTime = false;
                        state = AutoStates.END;
                    }
                    break;
                case END:
                    //telemetry.addData("state", "END");
                    break;
            }

            ShooterPID.manageEncoderData(currentTime - oldTime);
            oldTime = currentTime;

            if (PIDon) {
                double[] powers = ShooterPID.PID_calculateShooterPower(shooter1Power, shooter2Power);
                shooter1Power = powers[0];
                shooter2Power = powers[1];
                Util.shooter1.setPower(shooter1Power); // + 0.005);
                Util.shooter2.setPower(shooter2Power); // + 0.005);
            }

            Thread.sleep(5);

            // oversample
            Util.shooter1.getCurrentPosition();
            Util.shooter2.getCurrentPosition();

            Thread.sleep(5);
        }

        Util.setDriveModeBrake();

        Util.ballFeeder.setPosition(Util.LOAD);

        /*telemetry.update();

        while(opModeIsActive()) Thread.sleep(100);*/
    }
}
