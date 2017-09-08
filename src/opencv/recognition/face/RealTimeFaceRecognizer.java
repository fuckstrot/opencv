package opencv.recognition.face;

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

public class RealTimeFaceRecognizer {

    public static void main(String[] args) throws Exception {
        OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.setVideoOption("tune", "zerolatency");
        grabber.start();
        Frame videoFrame;
        Mat videoMat;
        CascadeClassifier face_cascade = new CascadeClassifier(RealTimeFaceCropper.DIR__CASCADE + RealTimeFaceCropper.FILE_NAME_FACE_FRONTAL);
        FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
        faceRecognizer.load(FaceRecognitionTrainer.trainedCascade);
        Mat videoMatGray;
        RectVector faces;
        Rect face_i;
        Mat face;
        IntPointer label;
        DoublePointer confidence;
        Rect rect = null;
        int k = RealTimeFaceCropper.K;
        while (true) {
            videoFrame = grabber.grab();
            videoMat = converterToMat.convert(videoFrame);
            videoMatGray = new Mat();
            cvtColor(videoMat, videoMatGray, COLOR_BGRA2GRAY);
            equalizeHist(videoMatGray, videoMatGray);
            faces = new RectVector();
            face_cascade.detectMultiScale(videoMatGray, faces);        
            for (int i = 0; i < faces.size(); i++) {
                face_i = faces.get(i);
                if (face_i.size().height() > videoMatGray.size().height() / 5) {
                    rect = new Rect(face_i.tl().x()+face_i.width()/(k*2), face_i.tl().y()+face_i.width()/k, face_i.width()*(k-1)/k, face_i.width()*(k-1)/k);
                    face = new Mat(videoMatGray, rect);
                    resize(face, face, new Size(RealTimeFaceCropper.SAVED_IMG_SIZE, RealTimeFaceCropper.SAVED_IMG_SIZE), 1.0, 1.0, INTER_CUBIC);
                    label = new IntPointer(1);
                    confidence = new DoublePointer(1);
                    faceRecognizer.predict(face, label, confidence);
                    rectangle(videoMat, face_i, new Scalar(0, 255, 0, 1));
                    int pos_x = Math.max(face_i.tl().x() - 10, 0);
                    int pos_y = Math.max(face_i.tl().y() - 10, 0);
                    putText(videoMat, "l: " + label.get(0), new Point(pos_x, pos_y), FONT_HERSHEY_SIMPLEX, 2.0, new Scalar(0, 255, 0, 2.0));
                }
            }
            imshow("label: ", videoMat);
            char key = (char) waitKey(20);
            if (key == 27) {
                destroyAllWindows();
                break;
            }
        }
    }
}
