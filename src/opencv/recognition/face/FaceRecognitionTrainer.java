package opencv.recognition.face;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_face.createFisherFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;

public class FaceRecognitionTrainer {
    public static final String trainingDir = "C:\\Users\\shipulin.mihail\\Desktop\\test\\traindir\\";
    public static final String trainedCascade = "C:\\Users\\shipulin.mihail\\Desktop\\test\\trained_cascade.yml";
    public static void main(String[] args) {
        

        File root = new File(trainingDir);
        FilenameFilter imgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
            }
        };
        File[] imageFiles = root.listFiles(imgFilter);
        MatVector images = new MatVector(imageFiles.length);
        Mat labels = new Mat(imageFiles.length, 1, CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();
        int counter = 0;
        for (File image : imageFiles) {
            Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
            int label = Integer.parseInt(image.getName().split("\\-")[0]);
            images.put(counter, img);
            labelsBuf.put(counter, label);
            counter++;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yy_mm_ss_sss");
        
        System.out.println(sdf.format(new Date(System.currentTimeMillis()))+" start createFisherFaceRecognizer");
        FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
        // FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
        // FaceRecognizer faceRecognizer = createLBPHFaceRecognizer()
        System.out.println(sdf.format(new Date(System.currentTimeMillis()))+" end createFisherFaceRecognizer");
        System.out.println(sdf.format(new Date(System.currentTimeMillis()))+" start train");
        faceRecognizer.train(images, labels);
        System.out.println(sdf.format(new Date(System.currentTimeMillis()))+" end train");
        faceRecognizer.save(trainedCascade);
    }
}
