package org.firstinspires.ftc.teamcode.RelicRecovery;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

/**
 * Created by lulzbot on 1/17/18.
 */

public class CameraView {
    VideoCapture camera = new VideoCapture();

    public void open(){
        camera.open(0);
    }
    public boolean checkOpened(){
        if(!camera.isOpened()){
            return false;
        }else{
            return true;
        }

    }


    public Mat getImage() {
        Mat image = new Mat();
        camera.read(image);
        return image;
    }

    public void returnImage(){
        Imgcodecs.imwrite("MyImage.jpg",getImage());
    }
}

