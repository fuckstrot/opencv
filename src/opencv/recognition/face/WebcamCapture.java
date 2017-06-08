package opencv.recognition.face;

import com.sun.prism.impl.shape.BasicRoundRectRep;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder.Exception;
import org.bytedeco.javacv.OpenCVFrameGrabber;

public class WebcamCapture {
    final private static int WEBCAM_DEVICE_INDEX = 0;

    public static void main(String[] args) throws Exception, org.bytedeco.javacv.FrameGrabber.Exception {
        int captureWidth = 400;
        int captureHeight = 400;
        // The available FrameGrabber classes include OpenCVFrameGrabber (opencv_videoio),
        // DC1394FrameGrabber, FlyCaptureFrameGrabber, OpenKinectFrameGrabber,
        // PS3EyeFrameGrabber, VideoInputFrameGrabber, and FFmpegFrameGrabber.
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(WEBCAM_DEVICE_INDEX);
        grabber.setImageWidth(captureWidth);
        grabber.setImageHeight(captureHeight);
        grabber.setImageMode(FrameGrabber.ImageMode.GRAY);
        grabber.setVideoOption("tune", "zerolatency");
        // grabber.set
        grabber.start();
        // A really nice hardware accelerated component for our preview...
        CanvasFrame cFrame = new CanvasFrame("Суперопределитель", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        Frame capturedFrame = null;
        while ((capturedFrame = grabber.grab()) != null) {
            if (cFrame.isVisible()) {
                // Show our frame in the preview
                cFrame.showImage(capturedFrame);
                //capturedFrame.
                //Core.rectangle(contoursFrame, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), (255, 0, 0, 255), 3); 
            }
        }
        cFrame.dispose();
        grabber.stop();
    }
}
