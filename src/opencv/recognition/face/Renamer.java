package opencv.recognition.face;

import java.io.File;

public class Renamer {
    public static void main(String[] args) {
        File inputFolder = new File("C:\\Users\\shipulin.mihail\\Desktop\\test\\Mike\\");
        for (final File fileEntry : inputFolder.listFiles()) {
            System.out.println(fileEntry.getName());
            fileEntry.renameTo(new File("C:\\Users\\shipulin.mihail\\Desktop\\test\\Mike\\4-"+fileEntry.getName()));
        }
    }
}
