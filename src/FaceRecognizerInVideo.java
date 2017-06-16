
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
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;
import org.bytedeco.javacv.FrameGrabber;

/**
 * This is an example how to detect face in a video file with javacv
 * @author Vincent He (chinadragon0515@gmail.com)
 *
 */
public class FaceRecognizerInVideo {
    public static void main(String[] args) throws Exception {
        OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);

        grabber.setVideoOption("tune", "zerolatency");
        grabber.start();

        Frame videoFrame = null;
        Mat videoMat = new Mat();
        CascadeClassifier face_cascade = new CascadeClassifier("C:\\Users\\shipulin.mihail\\Desktop\\test\\haarcascade_frontalface_default.xml");
        FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
        faceRecognizer.load("C:\\Users\\shipulin.mihail\\Desktop\\test\\FaceDB.yml");
        while (true) {
            videoFrame = grabber.grab();
            videoMat = converterToMat.convert(videoFrame);
            Mat videoMatGray = new Mat();
            // Convert the current frame to grayscale:
            cvtColor(videoMat, videoMatGray, COLOR_BGRA2GRAY);
            equalizeHist(videoMatGray, videoMatGray);

            Point p = new Point();
            RectVector faces = new RectVector();
            // Find the faces in the frame:
            face_cascade.detectMultiScale(videoMatGray, faces);

            // At this point you have the position of the faces in
            // faces. Now we'll get the faces, make a prediction and
            // annotate it in the video. Cool or what?
            String text = "Didn't find face";
            DecimalFormat df = new DecimalFormat("#.00"); 
            for (int i = 0; i < faces.size(); i++) {
                Rect face_i = faces.get(i);

                Mat face = new Mat(videoMatGray, face_i);
                // If fisher face recognizer is used, the face need to be
                // resized.
                resize(face, face, new Size(100, 100),  1.0, 1.0, INTER_CUBIC);

                // Now perform the prediction, see how easy that is:
                IntPointer label = new IntPointer(1);
                DoublePointer confidence = new DoublePointer(1);
                faceRecognizer.predict(face, label, confidence);
                int prediction = label.get(0);
                if(prediction == 4){
                    text = "Mike ";
                } else if(prediction == 3){
                    text = "Jacob ";
                }
                // And finally write all we've found out to the original image!
                // First of all draw a green rectangle around the detected face:
                rectangle(videoMat, face_i, new Scalar(0, 255, 0, 1));

                // Create the text we will annotate the box with:
                String box_text = text+" "+df.format(confidence.get());
                // Calculate the position for annotated text (make sure we don't
                // put illegal values in there):
                int pos_x = Math.max(face_i.tl().x() - 10, 0);
                int pos_y = Math.max(face_i.tl().y() - 10, 0);
                // And now put it into the image:
                putText(videoMat, box_text, new Point(pos_x, pos_y),
                        FONT_HERSHEY_PLAIN, 2.0, new Scalar(0, 255, 0, 2.0));
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
