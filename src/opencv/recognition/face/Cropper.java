package opencv.recognition.face;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import jjil.algorithm.RgbAvgGray;
import jjil.core.Rect;
import jjil.core.RgbImage;
import jjil.j2se.RgbImageJ2se;

public class Cropper {

    public static List<Rect> findFaces(BufferedImage bi, int minScale, int maxScale, String pathToHaar) {
        try {
            File hoarFile = new File(pathToHaar);
            InputStream is = new FileInputStream(hoarFile);
            Gray8DetectHaarMultiScale detectHaar = new Gray8DetectHaarMultiScale(is, minScale, maxScale);
            RgbImage im = RgbImageJ2se.toRgbImage(bi);
            RgbAvgGray toGray = new RgbAvgGray();
            toGray.push(im);
            List<Rect> results = detectHaar.pushAndReturn(toGray.getFront());
            return results;
        } catch (Throwable e) {
            e.printStackTrace();;
        }
        return null;
    }

    public static void main(String[] args) {
        
    }

}
