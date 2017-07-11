package opencv.recognition.face;

import datatype.Coordinates;
import datatype.Person;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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

public class RealTimeFaceRecognizer extends Application{
    private static Set<Person> persons = new HashSet<Person>();
    private static double deltaK = 0.1;     
    private static double deltaX ;     
    private static double deltaY ;  
    private static int width = 640;  
    private static double higth = 480; 
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        }); 
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static Person findPerson(Rect face_i,String[] args){
        if(null!=persons && !persons.isEmpty()){
            Iterator<Person> iter = persons.iterator();
            Person person = null;
            while (iter.hasNext()) {
                person = iter.next();
                if((System.currentTimeMillis()-person.getLastSeen())>200 && person.index <100) {
                    iter.remove();
                } else if((System.currentTimeMillis()-person.getLastSeen())>2000 && person.index >100){
                    launch(args);
                } else if(face_i.tl().x() >= (person.getCoordinatePair().getX() - deltaX) &&
                   face_i.tl().x() <=  (person.getCoordinatePair().getX() + deltaX) &&
                   face_i.tl().y() >= (person.getCoordinatePair().getY() - deltaY) &&
                   face_i.tl().y() <=  (person.getCoordinatePair().getY() + deltaY)){
                   person.setCoordinatePair(new Coordinates(face_i.tl().x(), face_i.tl().y()));
                   person.setLastSeen(System.currentTimeMillis());
                   person.index++;
                   return person;
                }
            }
        }
        return null;
    }
    
    public static void main(String[] args) throws Exception {
        deltaX = width * deltaK;
        deltaY = higth * deltaK;
        
        OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);

        grabber.setVideoOption("tune", "zerolatency");
        grabber.start();

        Frame videoFrame = null;
        Mat videoMat = new Mat();
        CascadeClassifier face_cascade = new CascadeClassifier("C:\\Users\\shipulin.mihail\\Desktop\\test\\haarcascade_frontalface_default.xml");
        FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
        faceRecognizer.load("C:\\Users\\shipulin.mihail\\Desktop\\test\\trained_cascade16_06_17_55_35_035.yml");
        Mat videoMatGray = null;
        Point p = null;
        RectVector faces = null;
        String text = "";
        DecimalFormat df = new DecimalFormat("#.00"); 
        Rect face_i = null;
        Mat face = null;
        IntPointer label = null;
        DoublePointer confidence = null;
        Person person = null;
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
                if (checkSize(face_i, videoMatGray)) {
                    //face_i.
                    face = new Mat(videoMatGray, face_i);
                    // If fisher face recognizer is used, the face need to be
                    // resized.
                    resize(face, face, new Size(200, 200),  1.0, 1.0, INTER_CUBIC);
                    // Now perform the prediction, see how easy that is:
                    label = new IntPointer(1);
                    confidence = new DoublePointer(1);
                    
                    /*faceRecognizer.predict(face, label, confidence);
                    int prediction = label.get(0);
                    if(prediction == 1){
                        text = "Anton ";
                    } else if(prediction == 2){
                        text = "Kost ";
                    } else if(prediction == 3){
                        text = "Mike ";
                    } else if(prediction == 4){
                        text = "Stoya ";
                    }*/
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
                    
                    person = findPerson(face_i, args);                 
                    if(null == person ){
                        person = new Person();
                        person.setId(persons.size()+1);
                        person.setCoordinatePair(new Coordinates(face_i.tl().x(), face_i.tl().y()));
                        person.setLabel(String.valueOf(persons.size()+1));
                        person.setLastSeen(System.currentTimeMillis());
                        persons.add(person);
                    }
                    putText(videoMat,person.getLabel()+" "+person.index, new Point(pos_x, pos_y), FONT_HERSHEY_PLAIN, 3.0, new Scalar(0, 255, 0, 2.0));
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
    public static boolean checkSize(Rect face_i, Mat videoMatGray){
        return face_i.size().height() > videoMatGray.size().height()/5;
    }
}
