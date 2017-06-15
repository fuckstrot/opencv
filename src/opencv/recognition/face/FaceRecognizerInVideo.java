
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private static CvHaarClassifierCascade classifier = null;
    private static final String CASCADE_FILE = "./haarcascade_frontalface_default.xml";
    static int captureWidth = 1000;
    static int captureHeight = 1000;
    private OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
    public static void main(String[] args) throws Exception, org.bytedeco.javacv.FrameRecorder.Exception { 
        //classifier = new CvHaarClassifierCascade(cvLoad(CASCADE_FILE));
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.setImageWidth(captureWidth);
        grabber.setImageHeight(captureHeight);
        grabber.setImageMode(FrameGrabber.ImageMode.GRAY);
        double frameRate = grabber.getFrameRate();
        long wait = (long) (1000 / (frameRate == 0 ? 10 : frameRate));
        grabber.start();      
        OpenCVFrameConverter.ToIplImage frameToIplImage =  new OpenCVFrameConverter.ToIplImage();
        IplImage grabbedImage = frameToIplImage.convert(grabber.grab());       
        IplImage imgGray = cvCreateImage(cvGetSize(grabbedImage), IPL_DEPTH_8U, 1);
        CanvasFrame canvasFrame = new CanvasFrame("Cam");
        canvasFrame.setCanvasSize(grabbedImage.width(), grabbedImage.height());
        grabbedImage =  frameToIplImage.convert(grabber.grab());
        canvasFrame.showImage(frameToIplImage.convert(grabbedImage));
        boolean stop = false;
        SimpleDateFormat sdf = new SimpleDateFormat("DD_MM_yy_mm_ss");
        while(!stop){
            grabbedImage = frameToIplImage.convert(grabber.grab());
            canvasFrame.showImage(frameToIplImage.convert(grabbedImage));
            opencv_imgcodecs.cvSaveImage("captured\\"+sdf.format(new Date(System.currentTimeMillis()))+".jpg", grabbedImage);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(FaceRecognizerInVideo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        grabber.stop();
        canvasFrame.dispose();
    }
}
