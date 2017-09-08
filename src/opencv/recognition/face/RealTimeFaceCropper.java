package opencv.recognition.face;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import org.bytedeco.javacpp.opencv_imgcodecs;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

public class RealTimeFaceCropper {

    public static final String DIR_FOUND_FACES = "C:\\Users\\shipulin.mihail\\Desktop\\test\\foundfaces\\";
    public static final String DIR__CASCADE = "C:\\Users\\shipulin.mihail\\Desktop\\test\\cascade\\";
    public static final String FILE_NAME_FACE_FRONTAL = "haarcascade_frontalface_default.xml";
    public static final String FILE_NAME_EYE = "haarcascade_eye.xml";
    public static final String FILE_NAME_NOSE = "haarcascade_mcs_nose.xml";
    public static final String FILE_NAME_MOUTH = "haarcascade_mcs_mouth.xml";
    public static final int SAVED_IMG_SIZE = 100;
    public static final int K = 4;
    
    public static void main(String[] args) throws Exception {
        OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.setImageHeight(1024);
        grabber.setImageWidth(1280);
        //grabber.setVideoOption("tune", "zerolatency");
        grabber.start();
        Frame videoFrame;
        Mat videoMat;
        Mat videoMatGray;
        RectVector faces;
        RectVector EYEs;
        RectVector NOSEs;
        RectVector MOUTHes;
        CascadeClassifier face_cascade = new CascadeClassifier(DIR__CASCADE + FILE_NAME_FACE_FRONTAL);
        CascadeClassifier face_EYE = new CascadeClassifier(DIR__CASCADE + FILE_NAME_EYE);
        CascadeClassifier face_NOSE = new CascadeClassifier(DIR__CASCADE + FILE_NAME_NOSE);
        CascadeClassifier face_MOUTH = new CascadeClassifier(DIR__CASCADE + FILE_NAME_MOUTH);
        while (true) {
            videoFrame = grabber.grab();
            videoMat = converterToMat.convert(videoFrame);
            videoMatGray = new Mat();
            cvtColor(videoMat, videoMatGray, COLOR_BGRA2GRAY);
            equalizeHist(videoMatGray, videoMatGray);
            faces = new RectVector();
//            EYEs = new RectVector();
//            NOSEs = new RectVector();
            //MOUTHes = new RectVector();
             face_cascade.detectMultiScale(videoMatGray, faces);
            // face_EYE.detectMultiScale(videoMatGray, EYEs);
            // face_NOSE.detectMultiScale(videoMatGray, NOSEs);
            //face_MOUTH.detectMultiScale(videoMatGray, MOUTHes);
            putObj(faces, videoMatGray, videoMat, "FACE");
            //putObj(EYEs, videoMatGray, videoMat, "EYEs");
            //putObj(NOSEs, videoMatGray, videoMat, "NOSEs");
//            putObj(MOUTHes, videoMatGray, videoMat, "Eyes");
            imshow("faceZILLA", videoMat);

            char key = (char) waitKey(20);
            if (key == 27) {
                destroyAllWindows();
                break;
            }
        }
    }

    private static void putObj(RectVector faces, Mat videoMatGray, Mat videoMat, String text) {
        Rect face_i;
        Mat face;
        Rect rect = null;
       
        for (int i = 0; i < faces.size(); i++) {
            face_i = faces.get(i);
            if (face_i.size().height() > videoMatGray.size().height() / 5) {
                rect = new Rect(face_i.tl().x()+face_i.width()/(K*2), face_i.tl().y()+face_i.width()/K, face_i.width()*(K-1)/K, face_i.width()*(K-1)/K);
                face = new Mat(videoMatGray, rect);
                rectangle(videoMat, rect, new Scalar(0, 0, 255, 1));
                resize(face, face, new Size(SAVED_IMG_SIZE, SAVED_IMG_SIZE), 1.0, 1.0, INTER_CUBIC);
                opencv_imgcodecs.imwrite(DIR_FOUND_FACES + System.currentTimeMillis() + ".jpg", face);
            }
        }
    }
}
