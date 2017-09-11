package opencv.recognition.face;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;

public class Mp4Grabber {

    public static void main(String[] args) throws FrameRecorder.Exception, org.bytedeco.javacv.FrameGrabber.Exception {
            FrameGrabber grabber = new FFmpegFrameGrabber("video_captured\\11_09_17_19_36_39.mp4");
            //grabber.setImageWidth(1280);
            //grabber.setImageHeight(1024);
            //grabber.setFormat("mp4");
            grabber.start();
            // A really nice hardware accelerated component for our preview...
            CanvasFrame cFrame = new CanvasFrame("Capture Preview", CanvasFrame.getDefaultGamma() / grabber.getGamma());
            Frame capturedFrame = null;
            // While we are capturing...
            while ((capturedFrame = grabber.grab()) != null) {
                if (cFrame.isVisible()) {
                    // Show our frame in the preview
                    cFrame.showImage(capturedFrame);
                }
            }
            cFrame.dispose();
            grabber.stop();
        }
}
