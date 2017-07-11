package opencv.recognition.face;


import java.io.File;
import java.text.DecimalFormat;

import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.DoublePointer;

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
import org.bytedeco.javacv.FrameGrabber;

/**
 * This is an example how to detect face in a video file with javacv
 * @author Vincent He (chinadragon0515@gmail.com)
 *
 */
public class RealTimeFaceCropper {
    public static void main(String[] args) throws Exception {
        OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);

        grabber.setVideoOption("tune", "zerolatency");
        grabber.start();

        Frame videoFrame = null;
        Mat videoMat = new Mat(); 
        Mat videoMatGray = null;
        Point p = null;
        RectVector faces = null;
        CascadeClassifier face_cascade = new CascadeClassifier("C:\\Users\\shipulin.mihail\\Desktop\\test\\haarcascade_frontalface_default.xml");
        String text = "";
        Rect face_i = null;
        Mat face = null;
        int pos_x = 0;
        int pos_y = 0;
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
                if(face_i.size().height() > videoMatGray.size().height()/4){
                    face = new Mat(videoMatGray, face_i);
                    resize(face, face, new Size(200, 200),  1.0, 1.0, INTER_CUBIC);
                    rectangle(videoMat, face_i, new Scalar(0, 255, 0, 1));
                    // Create the text we will annotate the box with:
                    text = "img_"+System.currentTimeMillis();
                    opencv_imgcodecs.imwrite("C:\\Users\\shipulin.mihail\\Desktop\\test\\foundfaces\\"
                            +text+".jpg", face);
                    // Calculate the position for annotated text (make sure we don't
                    // put illegal values in there):
                    pos_x = Math.max(face_i.tl().x() - 10, 0);
                    pos_y = Math.max(face_i.tl().y() - 10, 0);
                    // And now put it into the image:
                    putText(videoMat, text, new Point(pos_x, pos_y), FONT_HERSHEY_PLAIN, 3.0, new Scalar(0, 255, 0, 2.0));
                }
            }
            // Show the result:
            imshow("face_recognizer", videoMat);

            char key = (char) waitKey(20);
            // Exit this loop on escape:
            if (key == 27) {
                destroyAllWindows();
                break;
            }
        }
    }

}
