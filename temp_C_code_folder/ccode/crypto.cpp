

#include "opencv2/objdetect.hpp"
#include "opencv2/videoio.hpp"
#include "opencv2/highgui.hpp"
#include "opencv2/imgproc.hpp"

#include <cstring>
#include <iostream>
#include <fstream>
#include <ctime>

template <typename T> void printarr(T* arr, const int n) {
    for(int ii=0;ii<n;++ii) {
        std::cout << arr[ii] << "  " << std::flush;
    }
    std::cout << std::endl;
}

void smoother1d(const int n, float* arr) {

    const float alpha1 = 0.05;
    const float alpha0 = 1.0-alpha1;

    for(int ii=1;ii<n;++ii) {
        arr[ii] = alpha1 * arr[ii] + alpha0 * arr[ii-1];
    }
    for(int ii=n-2;ii>=0;--ii) {
        arr[ii] = alpha1 * arr[ii] + alpha0 * arr[ii+1];
    }

}


int processImage(cv::Mat& myimg, int* cols, const int framectr) {

    const bool writeplot = true;

    // parameters
    const float meanthreshold = 100.0;
    const int numsmooths = 4;
    const int imx = myimg.size().width;
    const int imy = myimg.size().height;

    std::cout << "imx : " << imx << std::endl;
    std::cout << "imy : " << imy << std::endl;

    std::string window_name2("My First Video 2");
    cv::namedWindow(window_name2,CV_WINDOW_NORMAL);
    cv::imshow(window_name2,myimg);


    // Crop the image
    cv::Rect roi;
    roi.x = 0;
    roi.y = 200;
    roi.width = imy-200;
    roi.height = imx;
    myimg = myimg(roi);

    std::string window_name("My First Video");
    cv::namedWindow(window_name,CV_WINDOW_NORMAL);
    cv::imshow(window_name,myimg);

    cv::waitKey(0);

    // Split the BGR image into three components
    cv::Mat mybgr[3];
    cv::split(myimg,mybgr);

    // Allocate memory for later use
    cv::Mat rt, gt, bt;
    cv::Mat temp,ct1,ct2,ct3,ct;

    // Apply thresholds
    cv::threshold(mybgr[2], rt, 100, 255, cv::THRESH_BINARY);
    cv::threshold(mybgr[1], gt, 70, 255, cv::THRESH_BINARY_INV);
    cv::threshold(mybgr[0], bt, 70, 255, cv::THRESH_BINARY_INV);
    cv::bitwise_and(gt,bt,temp);
    cv::bitwise_and(temp,rt,ct1);

    cv::threshold(mybgr[2], rt, 80, 255, cv::THRESH_BINARY);
    cv::threshold(mybgr[1], gt, 20, 255, cv::THRESH_BINARY_INV);
    cv::threshold(mybgr[0], bt, 20, 255, cv::THRESH_BINARY_INV);
    cv::bitwise_and(gt,bt,temp);
    cv::bitwise_and(temp,rt,ct2);

    cv::threshold(mybgr[2], rt, 170, 255, cv::THRESH_BINARY);
    cv::threshold(mybgr[1], gt, 150, 255, cv::THRESH_BINARY_INV);
    cv::threshold(mybgr[0], bt, 150, 255, cv::THRESH_BINARY_INV);
    cv::bitwise_and(gt,bt,temp);
    cv::bitwise_and(temp,rt,ct3);

    // Find the union of all thresholds
    cv::bitwise_or(ct1,ct2,temp);
    cv::bitwise_or(temp,ct3,ct);
        
    // Sum all columns
    cv::Mat colsum;
    cv::reduce(ct, colsum, 0, CV_REDUCE_AVG);

    // Convert column sums to floats
    cv::Mat colsum16;
    ct.convertTo(colsum16, CV_32F);
    float *ps = colsum16.ptr<float>();

    char buf[100];
    sprintf(buf,"output/smooth%06d.dat",framectr);
    std::string plotname(buf);

    std::ofstream fout(plotname,std::ios::binary);
    if(writeplot) {
        fout.write((char *) &numsmooths, sizeof(int));
        fout.write((char *) &imx,sizeof(int));
        fout.write((char *) ps, imx*sizeof(float));
    }
    for(int ss=0;ss<numsmooths;++ss) {
        smoother1d(imx,ps);  
        if(writeplot) fout.write((char *) ps, imx*sizeof(float));
    }
    
    
    
    
    // find peaks  
    // first, check the first pixel, and set the state
    int state = 0;
    int colctr = 0;
    if(ps[0]>ps[1]) {
        if(ps[0]>meanthreshold) {
            cols[colctr++] = 0;
        }
        state = -1;
    }
    else if(ps[0]<ps[1]) {
        state = 1;
    }
    // now we check every other pixel
    for(int ii=1;ii<imx;++ii) {
        if(ps[ii]>meanthreshold) {

            if(ps[ii]>ps[ii-1]) {
                state = 1;        
            }
            else if(ps[ii]<ps[ii-1]) {
                if(state>=0) {        //we were flat or rising
                    state = -1;      // now we are falling
                    cols[colctr++] = ii-1; //we found a peak
                }
            }
            else {
                state = 0;  // we are level
            }
        }
    }
    if(ps[imx-1]>meanthreshold && ps[imx-1]>ps[imx-2]) {
        cols[colctr++] = imx-1;
    }

    if(writeplot) {
        fout.write((char *) &colctr, sizeof(int));
        fout.write((char *) cols, colctr * sizeof(int));
    }
    fout.close();


    return colctr;


}


int main() {



    cv::VideoCapture cap("VID_20180108_235359658.mp4"); 

    // if not success, exit program
    if (cap.isOpened() == false) {
        std::cout << "Cannot open the video file" << std::endl;
        return -1;
    }

    //get the frames rate of the video
    double fps = cap.get(cv::CAP_PROP_FPS); 
    std::cout << "Frames per seconds : " << fps << std::endl;

    std::string window_name("My First Video");

    cv::namedWindow(window_name,CV_WINDOW_NORMAL);
    // cv::namedWindow(window_name, cv::WINDOW_NORMAL); //create a window

    int cols[24];
    int framectr = 0;
    while (framectr<1e6) {
        cv::Mat frame;
        const bool bSuccess = cap.read(frame); // read a new frame from video 
        //Breaking the while loop at the end of the video
        if (bSuccess == false)  {
            std::cout << "Found the end of the video" << std::endl;
            break;
        }

        // cv::transpose(frame, frame);
        // cv::flip(frame,frame, 0);

        const time_t tic = clock();
        const int numcols = processImage(frame,cols,framectr);
        const time_t toc = clock();
        const double cputime = double(toc - tic)/double(CLOCKS_PER_SEC);
        std::cout << "cputime " << cputime << std::endl;

        const int imy = frame.size().height;
        for(int ii=0;ii<numcols;++ii) {
            cv::Point pt1(cols[ii],imy); 
            cv::Point pt2(cols[ii],0);
            cv::line(frame,pt1,pt2,cv::Scalar( 255, 0, 0 ),8);
        }

        //show the frame in the created window
        cv::resize(frame, frame, cv::Size(), 0.5, 0.5);
        imshow(window_name, frame);
        cv::waitKey(1);


        framectr++;

    }

    return 0;
}