package opencv.recognition.face;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Cropper {
    public static void main(String[] args) {
        File inputFolder = new File("C:\\Users\\shipulin.mihail\\Desktop\\test\\traindir\\");
        Image src = null;
        BufferedImage dst = null;
        int tg = 100;
        for (final File fileEntry : inputFolder.listFiles()) {
            System.out.println(fileEntry.getName());
            try {
                src = ImageIO.read(fileEntry);
                if(src.getHeight(null)>tg){
                    int x = (src.getHeight(null) -tg)/2;
                    dst = new BufferedImage(tg, tg, BufferedImage.TYPE_INT_ARGB);
                    dst.getGraphics().drawImage(src, 0, 0, tg, tg, x, x, x + tg, x + tg, null);
                    ImageIO.write(dst, "png", new File("C:\\Users\\shipulin.mihail\\Desktop\\test\\cropped\\"+fileEntry.getName()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
