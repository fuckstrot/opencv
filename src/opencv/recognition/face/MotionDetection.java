package opencv.recognition.face;

import java.io.IOException;
import java.nio.file.Paths;
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

public class MotionDetection {

    public static void main(String[] args) throws Exception, org.bytedeco.javacv.FrameRecorder.Exception {
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        //grabber.setImageWidth(800);
        //grabber.setImageHeight(800);
        grabber.start();

        IplImage frame = converter.convert(grabber.grab());
        IplImage image = null;
        IplImage prevImage = null;
        IplImage diff = null;
        
        CanvasFrame canvasFrame = new CanvasFrame("Some Title");
        canvasFrame.setCanvasSize(frame.width(), frame.height());
        CvMemStorage storage = CvMemStorage.create();
        SimpleDateFormat sdf= new SimpleDateFormat("DD_MM_yy_mm_ss");
        while (canvasFrame.isVisible() && (frame = converter.convert(grabber.grab())) != null) {
            cvClearMemStorage(storage);
            cvSmooth(frame, frame, CV_GAUSSIAN, 9, 9, 2, 2);
            if (image == null) {
                image = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
                cvCvtColor(frame, image, CV_RGB2GRAY);
            } else {
                prevImage = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
                prevImage = image;
                image = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
                cvCvtColor(frame, image, CV_RGB2GRAY);
            }
            if (diff == null) {
                diff = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
            }
            if (prevImage != null) {
                // perform ABS difference
                cvAbsDiff(image, prevImage, diff);
                // do some threshold for wipe away useless details
                cvThreshold(diff, diff, 64, 255, CV_THRESH_BINARY);
                canvasFrame.showImage(converter.convert(diff));
                
                // recognize contours
                CvSeq contour = new CvSeq(null);
                cvFindContours(diff, storage, contour, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
                while (contour != null && !contour.isNull()) {
                    if (contour.elem_size() > 0) {
                        log("contour.elem_size() > 0");
                        CvBox2D box = cvMinAreaRect2(contour, storage);
                        // test intersection
                        if (box != null) {
                            log("box != null");
                            CvPoint2D32f center = box.center();
                            CvSize2D32f size = box.size();
                            //if(size.height()>5f && size.width()>5f){
                                log("center: "+center.toString()+" size: "+size.toString());
                                // opencv_imgcodecs.cvSaveImage("motion_detected\\diff_"+sdf.format(new Date(System.currentTimeMillis()))+".jpg", diff);
                                opencv_imgcodecs.cvSaveImage("motion_detected\\origin_"+sdf.format(new Date(System.currentTimeMillis()))+".jpg", 
                                converter.convert(grabber.grab()));
                           // }
//                            for (int i = 0; i < sa.length; i++) {
//                                if ((Math.abs(center.x - (sa[i].offsetX + sa[i].width / 2))) < ((size.width / 2) + (sa[i].width / 2)) &&
//                                    (Math.abs(center.y - (sa[i].offsetY + sa[i].height / 2))) < ((size.height / 2) + (sa[i].height / 2))) {
//                                    if (!alarmedZones.containsKey(i)) {
//                                        alarmedZones.put(i, true);
//                                        activeAlarms.put(i, 1);
//                                    } else {
//                                        activeAlarms.remove(i);
//                                        activeAlarms.put(i, 1);
//                                    }
//                                    System.out.println("Motion Detected in the area no: " + i +
//                                            " Located at points: (" + sa[i].x + ", " + sa[i].y+ ") -"
//                                            + " (" + (sa[i].x +sa[i].width) + ", "
//                                            + (sa[i].y+sa[i].height) + ")");
//                                }
//                            }
                        }
                    }
                    contour = contour.h_next();
                }
            }
        }
        grabber.stop();
        canvasFrame.dispose();
    }
    private static void log(String text){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH.mm.ss");
            text = sdf.format(new Date(System.currentTimeMillis())) +": "+text+System.getProperty("line.separator");
            System.out.println(text);
            //java.nio.file.Files.write(Paths.get("logs/motion.txt"), text.getBytes(), StandardCharsets.UTF_8, APPEND, CREATE);
            java.nio.file.Files.write(Paths.get("logs/motion.txt"), text.getBytes(), java.nio.file.StandardOpenOption.APPEND);
            //java.nio.file.Files.write(Paths.get("logs/motion.txt"), text.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(MotionDetection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
