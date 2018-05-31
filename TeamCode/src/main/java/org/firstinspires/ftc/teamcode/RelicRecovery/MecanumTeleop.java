//NOTE: table thingy up = 0.5 and down = 0.2

package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.*;

import org.firstinspires.ftc.teamcode.Util;

import static org.firstinspires.ftc.teamcode.RelicRecovery.MecanumTeleop.glyphIntakeStatus.HASTWO;
import static org.firstinspires.ftc.teamcode.RelicRecovery.MecanumTeleop.glyphIntakeStatus.INTAKEOFF;
import static org.firstinspires.ftc.teamcode.RelicRecovery.MecanumTeleop.glyphIntakeStatus.SERVOUP;


@TeleOp(name = "TeleOp", group = "TeleOp")
//@Disabled
public class MecanumTeleop extends OpMode {

    DcMotor rightFront, leftFront, leftBack, rightBack;
    DcMotor intake1, intake2;
    DcMotor trayLift;
    DcMotor relic;


    Servo rightTilt, leftTilt;
    Servo JewelArmServo;
    Servo JewelWhackerServo;
    Servo relicArmServo, relicGrabberServo;
    Servo tableBackstopServo;
    Servo distanceSensorArm;

    //DigitalChannel trayTouch; //future touchsensor

    double ScalingInitialPower = 0.05;
    double GamepadDeadZone = 0.05;
    double FAST_MAX_POWER = 1;
    double SLOW_MAX_POWER = 0.8;
    double MAX_POWER;
    boolean FastGear = true;
    boolean triggerCanbeRead = true;

    double VelocityLeftFront;
    double VelocityRightFront;
    double VelocityLeftBack;
    double VelocityRightBack;

    int intakeStatus = 0;
    boolean intakeChanged = false;
    private final double INTAKE_POWER = 0.95;

    boolean dPadWasPressed = false;

    boolean canTurnIntakeOff = true;

//    double initialLeftStickY;
//    double initialLeftStickX;
//    double initialRightStickX;

    double leftStickY;
    double leftStickX;
    double rightStickX;

    double G2leftStickY;

    //tray conditions for new tray
    boolean tilitingTray = false;
    boolean raisingTray = true;

    int untiltedPos = -5; //giving margin at the bottom
    int tiltedPos = -470;
    int liftUp = -1525;
    int liftDown = -70; //giving margin at the bottom
    int horizontalPos = -60;

    boolean traySensor = false;
    boolean presets = false, grippersClosed = false;
    long lastbuttontime = System.nanoTime();

    // tilting up means dumping the glyph
    boolean trayCanGoUp = true, trayCanGoDown = false, trayCanTiltUp = true, trayCanTiltDown = false;

    boolean rightStickActive = false, leftStickActive = false;

    boolean aButtonIsPressed = false;
    boolean bButtonIsPressed = false;
    boolean yButtonIsPressed = false;
    boolean xButtonIsPressed = false;

    boolean aButtonCanBePressed = true;
    boolean bButtonCanBePressed = true;
    boolean yButtonCanBePressed = true;
    boolean xButtonCanBePressed = true;

    boolean topPreset = false;
    boolean bottomPreset = false;
    boolean tiltPreset = false;

    //whacker things
    static double JewelArmServoUp = 0.85;
    static double JewelWhackerServoUp = 0.9;

    //Swich to relic mode by holding both bumpers

    boolean RelicMode = false;
    boolean pad2bumpersCanbeRead = true;

    boolean G2DpadUPIsPressed = false;
    boolean G2DpadDOWNIsPressed = false;

    boolean G2DpadUPCanBePressed = true;
    boolean G2DpadDOWNCanBePressed = false;

    boolean relicCanExtend = true;
    boolean relicCanRetract = false;

    boolean ExtendingRelic = true;


    double RelicArmUp = 0.81;
    double RelicArmDown = 0.26;
    double RelicArmNearDown = 0.28;
    double GrabberClosed = 0.53;
    double GrabberOpen = 0.31;

    double tableBackstopServoDown = 0.57;
    double tableBackstopServoUp = 0.73;

    double currentRelicArmPos;

    static DistanceSensor glyphCounterDistance;
    static com.qualcomm.robotcore.hardware.ColorSensor glyphCounterColor;

    enum glyphIntakeStatus {
        HASTWO, INTAKEOFF, SERVOUP
    }

    static glyphIntakeStatus glyphCountStatus;

    long initialTime;
    long elapsedTime;

    int counter = 0;

    public void init() {

        trayLift = hardwareMap.dcMotor.get("lift");
        trayLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        relic = hardwareMap.dcMotor.get("relic");
        relicArmServo = hardwareMap.servo.get("relicArm");
        relicArmServo.setPosition(0.07);
        relicGrabberServo = hardwareMap.servo.get("relicGrabber");
        relicGrabberServo.setPosition(GrabberClosed);

        rightBack = hardwareMap.dcMotor.get("rightBack");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftFront = hardwareMap.dcMotor.get("leftFront");

        intake1 = hardwareMap.dcMotor.get("intake_left");
        intake2 = hardwareMap.dcMotor.get("intake_right");

        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // trayTouch = hardwareMap.digitalChannel.get("trayTouch");
        //TODO:set init positions for table
        rightTilt = hardwareMap.servo.get("rightTiltServo");
        leftTilt = hardwareMap.servo.get("leftTiltServo");
//        leftTilt.setPosition(LeftServoDegreesToServoPos(-16));
//        rightTilt.setPosition(RightServoDegreesToServoPos(-16));

        tableBackstopServo = hardwareMap.servo.get("tableBackstopServo");
        tableBackstopServo.setPosition(tableBackstopServoDown);

        JewelArmServo = hardwareMap.servo.get("JewelArmServo");
        JewelArmServo.setPosition(JewelArmServoUp);
        JewelWhackerServo = hardwareMap.servo.get("JewelWhackerServo");
        JewelWhackerServo.setPosition(JewelWhackerServoUp);

        distanceSensorArm.setPosition(Util.distanceSensorArmUp);

        rightBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.REVERSE);

        trayLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        trayLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        glyphCounterColor = hardwareMap.colorSensor.get("glyphCounterDistanceColor");
        glyphCounterDistance = hardwareMap.get(DistanceSensor.class,"glyphCounterDistanceColor");

        // eventually calls code that throws Null Pointer
        // added solution: initialize motorsWithEncoders array
        // TODO: make sure these motors are appropriate for Tele-Op
        Util.motorsWithEncoders = new DcMotor[]{rightFront, rightBack, leftFront, leftBack, trayLift, relic};
        IntakeControl.teleOpinit();


        initialTime = 0;
        elapsedTime = 0;

        glyphCountStatus = HASTWO;

        getGlyphs.glyphCount = 0;

//        rightFront.setMode(DcMotor.RunMode.RESET_ENCODERS);
//        leftFront.setMode(DcMotor.RunMode.RESET_ENCODERS);
//        rightBack.setMode(DcMotor.RunMode.RESET_ENCODERS);
//        leftBack.setMode(DcMotor.RunMode.RESET_ENCODERS);
    }

    public double ScalingFunctionPositive(double x) {
        double y = (Math.pow(x, 2) - (0.05 * x) + ScalingInitialPower);
        return y;
    }

    public double ScalingFunctionNegative(double x) {
        double y = ((-(Math.pow(x, 2))) - (0.05 * x) - ScalingInitialPower);
        return y;
    }

    public double ScalingFunctionPositiveCubic(double x) {
        double y = (Math.pow(x, 3) - (0.05 * x) + 0.05);
        return y;
    }

    public double ScalingFunctionNegativeCubic(double x) {
        double y = (Math.pow(x, 3) - (0.05 * x) - 0.05);
        return y;
    }

    public static double RightServoDegreesToServoPos(double degrees) {
        double y = (((4.8* Math.pow(10, -3)) * degrees) + 0.368);
        return y;
    }

    public static double LeftServoDegreesToServoPos(double degrees) {
        double y = (((-4.8* Math.pow(10, -3)) * degrees) + 0.672);
        return y;
    }

    public void loop() {

        useDPad();

        if ((gamepad1.right_trigger > 0.3 || gamepad1.left_trigger > 0.3) & triggerCanbeRead) {
            FastGear = !FastGear;
            triggerCanbeRead = false;
        }
        if (!triggerCanbeRead & (gamepad1.right_trigger < 0.2 & gamepad1.left_trigger < 0.2)) {
            triggerCanbeRead = true;
        }

        if (!dPadWasPressed) {
            if (FastGear) {
                MAX_POWER = FAST_MAX_POWER;
            } else {
                MAX_POWER = SLOW_MAX_POWER; //may have to increase slow max power if continue to use cubic
            }

            leftStickY = -gamepad1.left_stick_y * MAX_POWER;
            leftStickX = gamepad1.left_stick_x * MAX_POWER;
            rightStickX = gamepad1.right_stick_x * MAX_POWER * 0.8; //lowered rotation speed - needs testing

//            initialLeftStickY = leftStickY;
//            initialLeftStickX = leftStickX;
//            initialRightStickX = rightStickX;

//            if ((Math.abs(-gamepad1.left_stick_y) - Math.abs(initialLeftStickY)) > 0.3) {
//                leftStickY = -gamepad1.left_stick_y * 0.5;
//                initialLeftStickY *= 2;



            if ((leftStickY >= -GamepadDeadZone) & (leftStickY <= GamepadDeadZone)) { //if stick is in dead zone set value equal to zero
                leftStickY = 0;
            } else if ((leftStickY >= GamepadDeadZone) & (leftStickY <= 1)) { //if stick is pos use scaling with pos y-int
//                leftStickY = ScalingFunctionPositive(leftStickY);
                leftStickY = ScalingFunctionPositiveCubic(leftStickY);
            } else if ((leftStickY <= -GamepadDeadZone) & (leftStickY >= -1)) { //if stick is neg us scaling with neg y-int
                //leftStickY = ScalingFunctionNegative(leftStickY);
                leftStickY = ScalingFunctionNegativeCubic(leftStickY);
            }

            if ((leftStickX >= -GamepadDeadZone) & (leftStickX <= GamepadDeadZone)) {
                leftStickX = 0;
            } else if ((leftStickX >= GamepadDeadZone) & (leftStickX <= 1)) {
                //leftStickX = ScalingFunctionPositive(leftStickX);
                leftStickX = ScalingFunctionPositiveCubic(leftStickX);
            } else if ((leftStickX <= -GamepadDeadZone) & (leftStickX >= -1)) {
                //leftStickX = ScalingFunctionNegative(leftStickX);
                leftStickX = ScalingFunctionNegativeCubic(leftStickX);

            }

            if ((rightStickX >= -GamepadDeadZone) & (rightStickX <= GamepadDeadZone)) {
                rightStickX = 0;
            } else if ((rightStickX >= GamepadDeadZone) & (rightStickX <= 1)) {
                //rightStickX = ScalingFunctionPositive(rightStickX);
                rightStickX = ScalingFunctionPositiveCubic(rightStickX);
            } else if ((rightStickX <= -GamepadDeadZone) & (rightStickX >= -1)) {
//                rightStickX = ScalingFunctionNegative(rightStickX);
                rightStickX = ScalingFunctionNegativeCubic(rightStickX);
            }



        /*
        if the left stick is in the middle and the right stick is not rotate

        if the left stick x and the left stick y and the right stick in the middle then set all powers to zero.

        if the left stick x or the left stick y is greater than 0.05 (down and right on the gamepad) then use the cubic function to scale the power

        if the left stick x or the left stick y is less than 0.05 (up and left on the gamepad) then use the cubic function to scale the power
         */

            if ((leftStickY == 0) & (leftStickX == 0) & (rightStickX != 0)) {

                VelocityLeftFront = rightStickX;
                VelocityRightFront = -rightStickX;
                VelocityLeftBack = rightStickX;
                VelocityRightBack = -rightStickX;

            } else if ((leftStickY == 0) & (leftStickX == 0) & (rightStickX == 0)) {

                VelocityLeftFront = 0;
                VelocityRightFront = 0;
                VelocityLeftBack = 0;
                VelocityRightBack = 0;


            } else {

                double turningConstant = 0;

                double FrontBack = leftStickY;
                double strafe = leftStickX;
                double rotate = rightStickX * turningConstant;

                VelocityLeftFront = FrontBack + rotate + strafe;
                VelocityRightFront = FrontBack - rotate - strafe;
                VelocityLeftBack = FrontBack + rotate - strafe;
                VelocityRightBack = FrontBack - rotate + strafe;

                //scaling so velocity doesn't go over 1

                double max = Math.abs(VelocityLeftFront);
                if (Math.abs(VelocityRightFront) > max) {
                    max = Math.abs(VelocityRightFront);
                }
                if (Math.abs(VelocityLeftBack) > max) {
                    max = Math.abs(VelocityLeftBack);
                }
                if (Math.abs(VelocityRightBack) > max) {
                    max = Math.abs(VelocityRightBack);
                }
                if (max > 1) {
                    VelocityLeftFront /= max; // velocity = velocity/max (will return 1)
                    VelocityRightFront /= max;
                    VelocityLeftBack /= max;
                    VelocityRightBack /= max;
                }
            }

            /*double r = Math.hypot(leftStickX, leftStickY);//switch left to right and vice versa if you want right to control direction
            double robotAngle = Math.atan2(leftStickY, leftStickX) - Math.PI / 4; //leftStickY is neg
            double rightX = rightStickX;

            VelocityLeftFront = r * Math.cos(robotAngle) - rightX;
            VelocityRightFront = r * Math.sin(robotAngle) + rightX;
            VelocityLeftBack = r * Math.sin(robotAngle) - rightX;
            VelocityRightBack = r * Math.cos(robotAngle) + rightX;*/


            leftFront.setPower(VelocityLeftFront);
            rightFront.setPower(VelocityRightFront);
            leftBack.setPower(VelocityLeftBack);
            rightBack.setPower(VelocityRightBack);

        }

        try {
            handleIntake();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //handleGrippers();

        //Swich to relic mode by holding both bumpers
        if (gamepad2.right_bumper && gamepad2.left_bumper && pad2bumpersCanbeRead) {
            RelicMode = !RelicMode;
            pad2bumpersCanbeRead = false;
        }
        if (!pad2bumpersCanbeRead && (!gamepad2.right_bumper || !gamepad2.left_bumper)){
            pad2bumpersCanbeRead = true;
        }

        if (RelicMode){
            handleRelic();
        }

        else {
            try {
                handleTray();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //handlePusher();

//        if (gamepad2.dpad_up) {
//            pusher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            pusher.setPower(0.2);
//        } else if (gamepad2.dpad_down) {
//            pusher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            pusher.setPower(-0.2);
//        } else {
//            pusher.setPower(0);
//        }



        /*if (!aPressed && gamepad1.a) {
            if (servoPos == 0.1) {
                servoPos = 0.9;
                servo.setPosition(servoPos);
            }
            else if (servoPos == 0.9) {
                servoPos = 0.1;
                servo.setPosition(servoPos);
            }
            aPressed = true;
        }
        else if (aPressed && !gamepad1.a) aPressed = false;*/
    }

    private void useDPad() {
        if (gamepad1.dpad_up) {
            leftBack.setPower(0.2);
            rightBack.setPower(0.2);
            leftFront.setPower(0.2);
            rightFront.setPower(0.2);
            dPadWasPressed = true;
        } else if (gamepad1.dpad_down) {
            leftBack.setPower(-0.2);
            rightBack.setPower(-0.2);
            leftFront.setPower(-0.2);
            rightFront.setPower(-0.2);
            dPadWasPressed = true;
        } else if (gamepad1.dpad_left) {
            leftBack.setPower(0.3);
            rightBack.setPower(-0.3);
            leftFront.setPower(-0.3);
            rightFront.setPower(0.3);
            dPadWasPressed = true;
        } else if (gamepad1.dpad_right) {
            leftBack.setPower(-0.3);
            rightBack.setPower(0.3);
            leftFront.setPower(0.3);
            rightFront.setPower(-0.3);
            dPadWasPressed = true;
        } else if (dPadWasPressed && !gamepad1.dpad_up && !gamepad1.dpad_down && !gamepad1.dpad_left && !gamepad1.dpad_right) {
            leftBack.setPower(0);
            rightBack.setPower(0);
            leftFront.setPower(0);
            rightFront.setPower(0);
            dPadWasPressed = false;
        }
    }


    //Intake Cases

    private static final int INTAKE_OFF = 0, INTAKE = 1, OUTTAKE = 2;

    private void handleIntake() throws InterruptedException {
        if (gamepad1.left_bumper && !intakeChanged) {
            /* if the intake is off, outtake
             * if the intake is intaking, turn it off
             * if the intake is outtaking, turn it off
             */
            switch (intakeStatus) {
                case INTAKE_OFF:
                    outtake();
                    break;
                case INTAKE:
                case OUTTAKE:
                    intakeOff();
                    break;
            }
            intakeChanged = true;
        }
        if (gamepad1.right_bumper && !intakeChanged) {
            /* if the intake is off, intake
             * if the intake is intaking, do nothing
             * if the intake is outtaking, intake
             */
            switch (intakeStatus) {
                case INTAKE_OFF:
                    intake();
                    break;
                case INTAKE:
                case OUTTAKE:
                    intakeOff();
                    break;
                //case OUTTAKE: break;
            }
            intakeChanged = true;
        }
        // wait until the user releases all intake-related buttons before allowing the user to change the intake again
        else if (!gamepad1.right_bumper && !gamepad1.left_bumper) {
            intakeChanged = false;
            /*if (intakeStatus == OUTTAKE) {
                intakeOff();
            }*/
        }

        IntakeControl.ManageGlyphCounterDataTeleOp();

        //glyphCountStates
        if (getGlyphs.glyphCount >= 2) {
            switch (glyphCountStatus) {
                case HASTWO:
                    initialTime = System.nanoTime();
                    glyphCountStatus = INTAKEOFF;
                    break;
                case INTAKEOFF:
                    elapsedTime = System.nanoTime() - initialTime;
                    if (elapsedTime > 800000000L) {
                        intakeOff();
                        getGlyphs.glyphCount = 0;
                        //glyphCountStatus = SERVOUP;
                    }
                    break;
//                case SERVOUP:
//                    elapsedTime = System.nanoTime() - initialTime;
//                    if (elapsedTime > 700000000L) {
//                        tableBackstopServo.setPosition(tableBackstopServoUp);
//                        getGlyphs.glyphCount = 0;
//                    }
//                    break;
            }
        }
        else {
            glyphCountStatus = HASTWO;
        }



    }

    // Intake methods. The three following methods standardize intaking, outtaking, and neither
    private void intake() {
        if (Math.abs(trayLift.getCurrentPosition()) < 500) {
//            gripper1.setPosition(0.95);
//            gripper2.setPosition(0.15);
//            grippersClosed = false;
            this.intake1.setPower(-INTAKE_POWER);
            this.intake2.setPower(INTAKE_POWER);
            intakeStatus = INTAKE;
        }
    }

    private void outtake() {
        if (Math.abs(trayLift.getCurrentPosition()) < 500) {
//            gripper1.setPosition(0.95);
//            gripper2.setPosition(0.15);
//            grippersClosed = false;
        }
        this.intake1.setPower(INTAKE_POWER);
        this.intake2.setPower(-INTAKE_POWER);
        intakeStatus = OUTTAKE;
    }

    private void intakeOff() {
        this.intake1.setPower(0);
        this.intake2.setPower(0);
        intakeStatus = INTAKE_OFF;
    }

    private void handleRelic() {
        /*TODO: may need to move relic servo some based on how far the arm is extended
        it tends to be a touch high if the arm is not extended fully*/
        telemetry.addLine("RELIC MODE");
        telemetry.update();

        telemetry.addData("Relic Arm pos" , currentRelicArmPos);

        G2DpadUPIsPressed = gamepad2.dpad_up;

        if (G2DpadUPIsPressed) {
            G2DpadUPIsPressed = true;
            G2DpadDOWNIsPressed = false;
        } else {
            G2DpadDOWNIsPressed = gamepad2.dpad_down;
            if (G2DpadDOWNIsPressed) {
                G2DpadDOWNIsPressed = true;
                G2DpadUPIsPressed = false;
            }
        }

        //making sure each button press is read once
        if (!G2DpadUPIsPressed) {
            G2DpadUPCanBePressed = true;
        }
        if (!G2DpadDOWNIsPressed) {
            G2DpadDOWNCanBePressed = true;
        }


        if (G2DpadUPIsPressed && G2DpadUPCanBePressed) {
            currentRelicArmPos = currentRelicArmPos + 0.02;
            relicArmServo.setPosition(currentRelicArmPos);
            telemetry.addData("Moving Arm up!" , currentRelicArmPos);
            telemetry.update();
            G2DpadUPCanBePressed = false;
        } else if (G2DpadDOWNIsPressed && G2DpadDOWNCanBePressed) {
            currentRelicArmPos = currentRelicArmPos - 0.02;
            relicArmServo.setPosition(currentRelicArmPos);
            telemetry.addData("Moving Arm Down!" , currentRelicArmPos);
            telemetry.update();
            G2DpadDOWNCanBePressed = false;
        }


        int relicPos = relic.getCurrentPosition();

        relicCanExtend = true;
        relicCanRetract = true;

        if (relicPos < 35 || relicPos > 2230) { //this is for the new slide thing (old one is 4445)
            if (relicPos < 35) {
                relicCanRetract = false;
                if (!ExtendingRelic) {
                    relic.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    relic.setPower(0);
                }
            }
            if (relicPos > 2230) {
                relicCanExtend = false;
                if (ExtendingRelic) {
                    relic.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    relic.setPower(0);
                }
            }

        } else {
            relic.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }


        G2leftStickY = -gamepad2.left_stick_y;

        if ((G2leftStickY >= -GamepadDeadZone) & (G2leftStickY <= GamepadDeadZone)) { //if stick is in dead zone set value equal to zero
            G2leftStickY = 0;
        } else if ((G2leftStickY >= GamepadDeadZone) & (G2leftStickY <= 1)) { //if stick is pos use scaling with pos y-int
            //                leftStickY = ScalingFunctionPositive(leftStickY);
            G2leftStickY = ScalingFunctionPositiveCubic(G2leftStickY);
        } else if ((G2leftStickY <= -GamepadDeadZone) & (G2leftStickY >= -1)) { //if stick is neg us scaling with neg y-int
            //leftStickY = ScalingFunctionNegative(leftStickY);
            G2leftStickY = ScalingFunctionNegativeCubic(G2leftStickY);
        }

        if (Math.abs(gamepad2.left_stick_y) > GamepadDeadZone) {
            relic.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            if (G2leftStickY > 0 && !relicCanExtend){
                G2leftStickY = 0;
            }
            else if (G2leftStickY <= 0 && !relicCanRetract){
                G2leftStickY = 0;
            }

            if (G2leftStickY > 0){
                ExtendingRelic = true;
            }
            else {
                ExtendingRelic = false;
            }
            leftStickActive = true;
        }
        else {
            G2leftStickY = 0;
            leftStickActive = false;
        }

        if (gamepad2.left_stick_y > 0){
            G2leftStickY = G2leftStickY * 0.6;
        }

        relic.setPower(G2leftStickY);

        //Relic buttons
        aButtonIsPressed = gamepad2.a; //green button

        if (aButtonIsPressed) {
            aButtonIsPressed = true;
            bButtonIsPressed = false;
            yButtonIsPressed = false;
            xButtonIsPressed = false;
        } else {
            bButtonIsPressed = gamepad2.b; //red button
            if (bButtonIsPressed) {
                bButtonIsPressed = true;
                yButtonIsPressed = false;
                aButtonIsPressed = false;
                xButtonIsPressed = false;
            } else {
                yButtonIsPressed = gamepad2.y; //yellow button
                if (yButtonIsPressed) {
                    yButtonIsPressed = true;
                    bButtonIsPressed = false;
                    aButtonIsPressed = false;
                    xButtonIsPressed = false;
                } else {
                    xButtonIsPressed = gamepad2.x; //blue button
                    if (xButtonIsPressed) {
                        yButtonIsPressed = false;
                        bButtonIsPressed = false;
                        aButtonIsPressed = false;
                        xButtonIsPressed = true;
                    }
                }
            }
        }

        //making sure each button press is read once
        if (!aButtonIsPressed) {
            aButtonCanBePressed = true;
        }

        if (!bButtonIsPressed) {
            bButtonCanBePressed = true;
        }

        if (!yButtonIsPressed) {
            yButtonCanBePressed = true;
        }

        if (!xButtonIsPressed) {
            xButtonCanBePressed = true;
        }


        if (aButtonIsPressed && aButtonCanBePressed){
            currentRelicArmPos = RelicArmDown;
            relicArmServo.setPosition(currentRelicArmPos);
            aButtonCanBePressed = false;
        }
        if (bButtonIsPressed && bButtonCanBePressed){
            currentRelicArmPos = RelicArmNearDown;
            relicArmServo.setPosition(currentRelicArmPos);
            bButtonCanBePressed = false;
        }
        if (yButtonIsPressed && yButtonCanBePressed){
            currentRelicArmPos = RelicArmUp;
            relicArmServo.setPosition(currentRelicArmPos);
            yButtonCanBePressed = false;
        }
        if (xButtonIsPressed && xButtonCanBePressed){
            currentRelicArmPos = RelicArmNearDown;
            relicArmServo.setPosition(RelicArmNearDown);
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            relicArmServo.setPosition(RelicArmNearDown + 0.06);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            relicArmServo.setPosition(RelicArmNearDown);
            xButtonCanBePressed = false;
        }
        if (gamepad2.right_trigger > 0.1){
            relicGrabberServo.setPosition(GrabberClosed);
        }
        if (gamepad2.left_trigger > 0.1){
            relicGrabberServo.setPosition(GrabberOpen);
        }
//        extend slides fully - run motor to pos
//                    set arm servo to down pos
//                    release relic
//                            pull up arm
//                    retract slides
    }

    //use to move relic arm up or down
//    private void useRelicDpad() {
//        telemetry.addData("Relic Arm pos" , currentRelicArmPos);
//        telemetry.update();
//        if (gamepad2.dpad_up) {
//            currentRelicArmPos = currentRelicArmPos + 0.05;
//            telemetry.addData("Moving Arm up!" , currentRelicArmPos);
//            telemetry.update();
//        } else if (gamepad1.dpad_down) {
//            currentRelicArmPos = currentRelicArmPos - 0.05;
//            telemetry.addData("Moving Arm Down!" , currentRelicArmPos);
//            telemetry.update();
//        }
//        relicArmServo.setPosition(currentRelicArmPos);
//    }

    private void handleTray() throws InterruptedException {

        int trayVerticalPos = trayLift.getCurrentPosition();
        //double trayTiltPos = rightTilt.getPosition();

        // reset variables
        trayCanGoUp = true;
        trayCanGoDown = true;
        trayCanTiltUp = true;
        trayCanTiltDown = true;//traySensor = false;
//
//        if (trayTouch.getState() == false) {
//            traySensor = true;
//        } else {
//            traySensor = false;
//        }

        if (trayVerticalPos < -600 || (rightTilt.getPosition() > RightServoDegreesToServoPos(30))) {
            if (canTurnIntakeOff) {
                intake1.setPower(0);
                intake2.setPower(0);
                intakeStatus = INTAKE_OFF;
            }
            canTurnIntakeOff = false;
        } else {
            canTurnIntakeOff = true;
        }

        //Absolute max for the table to go up so we don't break things
        if (trayVerticalPos > -5 || trayVerticalPos < -1305) { // || traySensor use later
            if (trayVerticalPos > -5) {
                trayCanGoDown = false;
                if (!raisingTray) {
                    trayLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                    trayLift.setPower(0);
                }
            }
            if (trayVerticalPos < -1305) {
                trayCanGoUp = false;
                if (raisingTray) {
                    trayLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    trayLift.setPower(0);
                }
            }
            //else if (traySensor) trayCanGoDown = false;

        } else {
            trayLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }


        //manual raise
        if (Math.abs(gamepad2.right_stick_y) > 0.1) {
            double rightstick = -gamepad2.right_stick_y;
            if (rightstick > 0 && !trayCanGoDown) {
                rightstick = 0;
            } else if (rightstick <= 0 && !trayCanGoUp) {
                rightstick = 0;
            }
//            trayTilt.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            trayTilt.setTargetPosition(horizontalPos);
//            trayTilt.setPower(0.3);
            // trayTilt.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            trayLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            trayLift.setPower(rightstick / 2); // divided by 2 temp for testing
            trayLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //sets motor to brake so table doesn't slide
            presets = false;
            rightStickActive = true;
        } else {
            rightStickActive = false;
        }
        //manual tilt
        if (Math.abs(gamepad2.left_stick_y) > 0.1) {
            double leftstick = -gamepad2.left_stick_y;
            if (leftstick > 0 && !trayCanTiltDown) leftstick = 0;
            else if (leftstick <= 0 && !trayCanTiltUp) leftstick = 0;
            rightTilt.setPosition(leftstick);
            presets = false;
            leftStickActive = true;
        } else {
            leftStickActive = false;
        }

        //Table Presets

        aButtonIsPressed = gamepad2.a; //green button

        if (aButtonIsPressed) {
            aButtonIsPressed = true;
            bButtonIsPressed = false;
            yButtonIsPressed = false;
        } else {
            bButtonIsPressed = gamepad2.b; //red button
            if (bButtonIsPressed) {
                bButtonIsPressed = true;
                yButtonIsPressed = false;
                aButtonIsPressed = false;
            } else {
                yButtonIsPressed = gamepad2.y; //yellow button
                if (yButtonIsPressed) {
                    yButtonIsPressed = true;
                    bButtonIsPressed = false;
                    aButtonIsPressed = false;

                }
            }
        }

        //making sure each button press is read once
        if (!aButtonIsPressed) {
            aButtonCanBePressed = true;
        }

        if (!bButtonIsPressed) {
            bButtonCanBePressed = true;
        }

        if (!yButtonIsPressed) {
            yButtonCanBePressed = true;
        }

        //bottom preset
        if (aButtonIsPressed && aButtonCanBePressed) { //green button
            if (!leftStickActive && !rightStickActive) {
                tableBackstopServo.setPosition(tableBackstopServoDown);
                trayLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                trayLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                lastbuttontime = System.nanoTime();
                leftTilt.setPosition(LeftServoDegreesToServoPos(-16));
                rightTilt.setPosition(RightServoDegreesToServoPos(-16));
                tilitingTray = false;
                if (trayLift.getCurrentPosition() < -40) {
                    trayLift.setPower(0.55);
                    Thread.sleep(200);
                    trayLift.setPower(0);
                    raisingTray = true;
                }
                presets = true;
            }
            aButtonCanBePressed = false;
        }

        //top preset
        if (bButtonIsPressed && bButtonCanBePressed) { //red button
            if (!rightStickActive && (trayLift.getCurrentPosition() > -1510)) {
                lastbuttontime = System.nanoTime();
                tableBackstopServo.setPosition(tableBackstopServoUp);
                leftTilt.setPosition(LeftServoDegreesToServoPos(-16));
                rightTilt.setPosition(RightServoDegreesToServoPos(-16));
                trayLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                trayLift.setTargetPosition(liftUp);
                trayLift.setPower(1);
                raisingTray = true;
                presets = true;
            }
            bButtonCanBePressed = false;
        }

        //tilt preset
        if (yButtonIsPressed && yButtonCanBePressed) { //yellow button
            if (!leftStickActive) {
                lastbuttontime = System.nanoTime();
                tableBackstopServo.setPosition(tableBackstopServoDown);
                leftTilt.setPosition(LeftServoDegreesToServoPos(92));
                rightTilt.setPosition(RightServoDegreesToServoPos(92));
                getGlyphs.glyphCount = 0;
                tilitingTray = true;
                presets = true;
            }
            yButtonCanBePressed = false;
        }


        if (presets && (System.nanoTime() - lastbuttontime) > 3000000000L) {
            //trayLift.setPower(0);
            //telemetry.addData("tray time status", "Tray presets have been stopped because the motor has run for more than 3 seconds");
        }

        if (!presets && !rightStickActive) {
            trayLift.setPower(0);
        }
        if (!presets && !leftStickActive) {
            //trayTilt.setPower(0);
        }
    }
}
//    // gripper methods
//    private void handleGrippers(){
//        if(gamepad2.left_bumper){
//            gripper1.setPosition(0.95);
//            gripper2.setPosition(0.15);
//            grippersClosed = false;
//        }
//        else if(gamepad2.right_bumper){
//            gripper1.setPosition(0.75);
//            gripper2.setPosition(0.35);
//            grippersClosed = true;
//        }
//    }




/*
gamepad1:
    sticks: drive
    bumpers: intake
gamepad2:

    stick1: table manual
    stick2: relic
    a table down
    b table top
    y table tilt
    bay: table presets
 */

