package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.ArrayList;

@TeleOp(name="MenusTest", group="Test")
//@Disabled

public class MenusTest extends OpMode {

    ArrayList<String> COLORS;
    ArrayList<String> PARTICLES;
    ArrayList<String> PARKS;
    int DELAYS = 0; final int DELAY_MAX = 20;

    String[] menuItems = {"COLOR", "balls", " park", "delay"};
    String S = ": ";

    int activeItem = 0, ACTIVE_ITEM_MAX = 3;

    public void init() {
        initializeArrays();
        displayMenu();
    }

    public void loop() {
        if (gamepad1.dpad_down) {
            menuItems[activeItem].toLowerCase();
            activeItem++;
            if (activeItem > ACTIVE_ITEM_MAX) activeItem = 0;
            if (activeItem < 0) activeItem = ACTIVE_ITEM_MAX;
            menuItems[activeItem].toUpperCase();
        } else if (gamepad1.dpad_up) {
            menuItems[activeItem].toLowerCase();
            activeItem--;
            if (activeItem < 0) activeItem = ACTIVE_ITEM_MAX;
            if (activeItem > ACTIVE_ITEM_MAX) activeItem = 0;
            menuItems[activeItem].toUpperCase();
        } else if (gamepad1.dpad_right) {
            if (activeItem == 3) {
                DELAYS++;
                if (DELAYS > DELAY_MAX) DELAYS = 0;
                if (DELAYS < 0) DELAYS = DELAY_MAX;
            } else if (activeItem == 0) {
                COLORS.add(COLORS.remove(0));
            } else if (activeItem == 1) {
                PARTICLES.add(PARTICLES.remove(0));
            } else if (activeItem == 2) {
                PARKS.add(PARTICLES.remove(0));
            }
        } else if (gamepad1.dpad_left) {
            if (activeItem == 3) {
                DELAYS--;
                if (DELAYS < 0) DELAYS = DELAY_MAX;
                if (DELAYS > DELAY_MAX) DELAYS = 0;
            } else if (activeItem == 0) {
                COLORS.add(0, COLORS.remove(COLORS.size() - 1));
            } else if (activeItem == 1) {
                PARTICLES.add(0, PARTICLES.remove(PARTICLES.size() - 1));
            } else if (activeItem == 2) {
                PARKS.add(0, PARKS.remove(PARKS.size() - 1));
            }
        }

        displayMenu();
    }

    private void displayMenu() {
        telemetry.addData("color", menuItems[0] + S + COLORS.get(0));
        telemetry.addData("balls", menuItems[1] + S + PARTICLES.get(0));
        telemetry.addData("park", menuItems[2] + S + PARKS.get(0));
        telemetry.addData("delay", menuItems[3] + S + DELAYS);
        telemetry.update();
    }

    private void initializeArrays() {
        COLORS.add("RED"); COLORS.add("BLUE");

        PARTICLES.add("1"); PARTICLES.add("2"); PARTICLES.add("3");

        PARKS.add("CORNER"); PARKS.add("CENTER");
    }
}