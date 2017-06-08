
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.IplImage;


import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_calib3d.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

public class FaceRecognizerInVideo {

    public static void main(String[] args) throws Exception, org.bytedeco.javacv.FrameRecorder.Exception {
        // TODO Auto-generated method stub
        
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.start();
        
        OpenCVFrameConverter.ToIplImage frameToIplImage =  new OpenCVFrameConverter.ToIplImage();
        IplImage grabbedImage = frameToIplImage.convert(grabber.grab());
        
        IplImage imgGray = cvCreateImage(cvGetSize(grabbedImage), IPL_DEPTH_8U, 1);
       // IplImage imgHsv = cvCreateImage(cvGetSize(grabbedImage), IPL_DEPTH_8U, 3);
        CanvasFrame canvasFrame = new CanvasFrame("Cam");
//        CanvasFrame canvasFrame1 = new CanvasFrame("GrayImage");
//        CanvasFrame canvasFrame2 = new CanvasFrame("HSV Image");
        canvasFrame.setCanvasSize(grabbedImage.width(), grabbedImage.height());
//        canvasFrame1.setCanvasSize(grabbedImage.width(), grabbedImage.height());
//        canvasFrame2.setCanvasSize(grabbedImage.width(), grabbedImage.height());
        grabbedImage =  frameToIplImage.convert(grabber.grab());
        canvasFrame.showImage(frameToIplImage.convert(grabbedImage));
        //cvSave("test"+System.currentTimeMillis(), grabbedImage);
        //cvReleaseImage(imgHsv);
        boolean stop = false;
        while(!stop){
            grabbedImage = frameToIplImage.convert(grabber.grab());
            canvasFrame.showImage(frameToIplImage.convert(grabbedImage));
            opencv_imgcodecs.cvSaveImage("captured\\capture_"+System.currentTimeMillis()+".jpg", grabbedImage);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(FaceRecognizerInVideo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        /*while (canvasFrame.isVisible() && (grabbedImage =  frameToIplImage.convert(grabber.grab())) != null) {
            cvCvtColor(grabbedImage, imgGray, CV_BGR2GRAY);
            cvCvtColor(grabbedImage, imgHsv, CV_BGR2HSV);
            cvSave("test"+System.currentTimeMillis()+".jpg", imgHsv);
            cvReleaseImage(imgHsv);
            canvasFrame.showImage(frameToIplImage.convert(grabbedImage));
//            canvasFrame1.showImage(frameToIplImage.convert(imgGray));
           //canvasFrame2.showImage(frameToIplImage.convert(imgHsv));
        }*/
        grabber.stop();
        canvasFrame.dispose();
    }
}
