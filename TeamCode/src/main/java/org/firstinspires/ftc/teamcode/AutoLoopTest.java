package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

@Autonomous(name = "AutoLoopTest", group ="Test")
//@Disabled
public class AutoLoopTest extends LinearOpMode {

    // motors
    DcMotor rightBack, leftBack, rightFront, leftFront;
    DcMotor shooter1, shooter2;
    DcMotor[] driveMotors, shooterMotors;

    double shooter1Power, shooter2Power;

    GyroSensor gyro;

    AutoStates state = AutoStates.SHOOTER_SPIN_UP;

    boolean firstTime = true;

    public void runOpMode() throws InterruptedException {

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

        long start = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO, currentTime, oldTime = start - 10;

        while (state != AutoStates.END) {
            currentTime = System.nanoTime() / FinalTeleOp.MILLIS_PER_NANO;
            switch(state) {
                case SHOOTER_SPIN_UP:
                    if (firstTime) {
                        telemetry.addData("state", "SHOOTER_SPIN_UP");

                        shooter1Power = FinalTeleOp.calculateShooterPower();
                        shooter2Power = shooter1Power + FinalTeleOp.SHOOTER2_OFFSET; // shooter 2 is slower than shooter 1
                        shooter1.setPower(shooter1Power);
                        shooter2.setPower(shooter2Power);

                        firstTime = false;
                    }
                    if ((currentTime - start) > 500) {
                        state = AutoStates.DRIVE_START;
                        Util.setDrivePowersFloat();
                        firstTime = true;
                    }
                    break;
                case DRIVE_START:
                    double startPos = 0;
                    if (firstTime) {
                        telemetry.addData("state", "DRIVE_START");

                        AutoUtil.resetGyroHeading(gyro);
                        PID.resetDriveIntegral();
                        startPos = Util.rightBack.getCurrentPosition();

                        firstTime = false;
                    }

                    PID.PIsetMotors(gyro, 0.2);

                    if (Util.rightBack.getCurrentPosition() > (startPos + (1900 * 0.98))) {
                        state = AutoStates.SHOOTER_SPIN_DOWN;
                        firstTime = true;
                    }
                    break;
                case SHOOTER_SPIN_DOWN:
                    if (firstTime) {
                        telemetry.addData("state", "SHOOTER_SPIN_DOWN");

                        shooter1.setPower(0); shooter2.setPower(0);

                        firstTime = false;
                    }
                    if ((currentTime - start) > 2000) {
                        state = AutoStates.DRIVE_STOP;
                        firstTime = true;
                    }
                    break;
                case DRIVE_STOP:
                    if (firstTime) {
                        telemetry.addData("state", "DRIVE_STOP");

                        Util.setAllPowers(0);

                        state = AutoStates.END;
                        firstTime = false;
                    }
                    break;
                case END:
                    telemetry.addData("state", "END");
                    break;
            }

            if (state != AutoStates.SHOOTER_SPIN_DOWN && state != AutoStates.DRIVE_STOP && state != AutoStates.END) {

                ShooterPID.manageEncoderData(currentTime - oldTime);

                double[] powers = ShooterPID.PID_calculateShooterPower(shooter1Power, shooter2Power);
                shooter1Power = powers[0];
                shooter2Power = powers[1];
                shooter1.setPower(shooter1Power);
                shooter2.setPower(shooter2Power);

                oldTime = currentTime;
            }

            Thread.sleep(10);
        }

        shooter1.setPower(0); shooter2.setPower(0);
        Util.setAllPowers(0);

        start = System.nanoTime() / 1000000; currentTime = start;

        while ((currentTime - start) < 4000) {
            currentTime = System.nanoTime() / 1000000;

            telemetry.addData("elapsed time", currentTime - start);
            telemetry.update();

            Thread.sleep(10);
        }
    }
}
