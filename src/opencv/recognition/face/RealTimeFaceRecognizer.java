package opencv.recognition.face;

import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
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

public class RealTimeFaceRecognizer { 

    public static void main(String[] args) throws Exception {
        OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.setVideoOption("tune", "zerolatency");
        grabber.start();
        Frame videoFrame = null;
        Mat videoMat = new Mat();
        CascadeClassifier face_cascade = new CascadeClassifier("C:\\Users\\shipulin.mihail\\Desktop\\test\\haarcascade_frontalface_default.xml");
        FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
        faceRecognizer.load("C:\\Users\\shipulin.mihail\\Desktop\\test\\trained_cascade_1.yml");
        //faceRecognizer.setThreshold(1);
        Mat videoMatGray = null;
        Point p = null;
        RectVector faces = null;
        String text = "";
        DecimalFormat df = new DecimalFormat("#.00"); 
        Rect face_i = null;
        Mat face = null;
        IntPointer label = null;
        DoublePointer confidence = null;
        Size s1 = new Size(100,100);
        Size s2 = new Size(500,500);
        //faceRecognizer.setThreshold(1.0);
        while (true) {
            videoFrame = grabber.grab();
            videoMat = converterToMat.convert(videoFrame);
            videoMatGray = new Mat();
            cvtColor(videoMat, videoMatGray, COLOR_BGRA2GRAY);
            equalizeHist(videoMatGray, videoMatGray);
            p = new Point();
            faces = new RectVector();
            // Find the faces in the frame:
            face_cascade.detectMultiScale(videoMatGray, faces);
            face_cascade.detectMultiScale(videoMatGray, faces, CV_PI, CV_C, CV_C, s1, s2);
            // At this point you have the position of the faces in
            // faces. Now we'll get the faces, make a prediction and
            // annotate it in the video. Cool or what?               
            for (int i = 0; i < faces.size(); i++) {
                face_i = faces.get(i);
                if (true) {
                    //face_i.
                    face = new Mat(videoMatGray, face_i);
                    // If fisher face recognizer is used, the face need to be
                    // resized.
                    resize(face, face, new Size(200, 200),  1.0, 1.0, INTER_CUBIC);
                    // Now perform the prediction, see how easy that is:
                    label = new IntPointer(1);
                    confidence = new DoublePointer(1);
                        faceRecognizer.predict(face, label, confidence);
//                    int prediction = label.get(0);
//                    if(prediction == 1){
//                        text = "Anton ";
//                    } else if(prediction == 2){
//                        text = "Kost ";
//                    } else if(prediction == 3){
//                        text = "Mike ";
//                    } else if(prediction == 4){
//                        text = "Stoya ";
//                    }*/
                    // And finally write all we've found out to the original image!
                    // First of all draw a green rectangle around the detected face:
                        rectangle(videoMat, face_i, new Scalar(0, 255, 0, 1));
                        // Create the text we will annotate the box with:
                        //String box_text = text+" "+df.format(confidence.get());
                        // Calculate the position for annotated text (make sure we don't
                        // put illegal values in there):
                        int pos_x = Math.max(face_i.tl().x() - 10, 0);
                        int pos_y = Math.max(face_i.tl().y() - 10, 0);
                        // And now put it into the image:                
                        putText(videoMat,"l: "+ label.get(0), new Point(pos_x, pos_y), FONT_HERSHEY_SIMPLEX, 2.0, new Scalar(0, 255, 0, 2.0));
                        System.out.println("l: "+label.get(0)+" t:"+faceRecognizer.getThreshold() +" c: "+confidence.get());
                }
            }
            // Show the result:
            imshow("label: "+label.get(0), videoMat);

            char key = (char) waitKey(20);
            // Exit this loop on escape:
            if (key == 27) {
                destroyAllWindows();
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(RealTimeFaceRecognizer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
