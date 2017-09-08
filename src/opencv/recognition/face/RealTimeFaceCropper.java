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
    public static final String DIR__CASCADE = "C:\\Users\\shipulin.mihail\\Desktop\\test\\";
    public static final String FILE_NAME_FACE_FRONTAL = "haarcascade_frontalface_default.xml";
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
        CascadeClassifier face_cascade = new CascadeClassifier(DIR__CASCADE+FILE_NAME_FACE_FRONTAL);
        String text;
        Rect face_i;
        Mat face;
        int pos_x = 0;
        int pos_y = 0;
        while (true) {
            videoFrame = grabber.grab();
            videoMat = converterToMat.convert(videoFrame);
            videoMatGray = new Mat();
            cvtColor(videoMat, videoMatGray, COLOR_BGRA2GRAY);
            equalizeHist(videoMatGray, videoMatGray);   
            faces = new RectVector();
            face_cascade.detectMultiScale(videoMatGray, faces);
            text = "img";
            for (int i = 0; i < faces.size(); i++) {
                face_i = faces.get(i);
                if(face_i.size().height() > videoMatGray.size().height()/4){
                    face = new Mat(videoMatGray, face_i);
                    resize(face, face, new Size(100, 100),  1.0, 1.0, INTER_CUBIC);
                    rectangle(videoMat, 
                            new Point(face_i.tl().x(), face_i.tl().y()),
                            new Point(face_i.br().x(), face_i.br().y()), 
                            new Scalar(0, 0, 255, 1));                  
                    opencv_imgcodecs.imwrite(DIR_FOUND_FACES+System.currentTimeMillis()+".jpg", face);
                    pos_x = Math.max(face_i.tl().x() - 10, 0);
                    pos_y = Math.max(face_i.tl().y() - 10, 0);
                    putText(videoMat, text, new Point(pos_x, pos_y), FONT_HERSHEY_PLAIN, 3.0, new Scalar(0, 255, 255, 2.0));
                }
            }
            imshow("faceZILLA", videoMat);
            char key = (char) waitKey(20);
            if (key == 27) {
                destroyAllWindows();
                break;
            }
        }
    }

}
