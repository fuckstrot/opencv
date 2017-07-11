package opencv.recognition.face;

import java.io.File;
import java.util.Date;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_face.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import org.bytedeco.javacpp.opencv_imgcodecs;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;

/**
 * This is an example how to detect face in a video file with javacv
 * @author Vincent He (chinadragon0515@gmail.com)
 *
 */
public class FaceRecognizerInVideoFile {

    public static void main(String[] args) throws Exception {
        OpenCVFrameConverter.ToIplImage converterToIplImage = new OpenCVFrameConverter.ToIplImage();
        OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
        CascadeClassifier face_cascade = new CascadeClassifier("C:\\Users\\shipulin.mihail\\Desktop\\test\\haarcascade_frontalface_default.xml");
        File f = new File("C:\\Users\\shipulin.mihail\\Desktop\\test\\video.mp4");
        System.out.println("f "+f.exists());
        //FrameGrabber  grabber = new FFmpegFrameGrabber (f);
        OpenCVFrameGrabber grabber =  OpenCVFrameGrabber.createDefault(f);
        grabber.start();


        Frame videoFrame = null;
        Mat videoMat = new Mat();
        Mat videoMatGray = null;
        Point p = null;
        RectVector faces = null;
        Rect face_i = null;
        Mat face = null;
        while (true) {
            videoFrame = grabber.grab();
            videoMat = converterToMat.convert(videoFrame);
            videoMatGray = new Mat();
            // Convert the current frame to grayscale:
            cvtColor(videoMat, videoMatGray, COLOR_BGRA2GRAY);
            equalizeHist(videoMatGray, videoMatGray);
            p = new Point();
            faces = new RectVector();
            // Find the faces in the frame:
            face_cascade.detectMultiScale(videoMatGray, faces);
            // At this point you have the position of the faces in
            // faces. Now we'll get the faces, make a prediction and
            // annotate it in the video. Cool or what?
            for (int i = 0; i < faces.size(); i++) {
                face_i = faces.get(i);
                face = new Mat(videoMatGray, face_i);
                resize(face, face, new Size(300, 300), 1.0, 1.0, INTER_CUBIC);
                opencv_imgcodecs.imwrite("C:\\Users\\shipulin.mihail\\Desktop\\test\\foundfaces\\"
                        +System.currentTimeMillis()+".jpg", face);
            }
        }
    }

}
