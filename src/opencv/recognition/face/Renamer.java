package opencv.recognition.face;

import java.io.File;

public class Renamer {
    public static void main(String[] args) {
        File inputFolder = new File("C:\\Users\\shipulin.mihail\\Desktop\\test\\Stoya\\");
        for (final File fileEntry : inputFolder.listFiles()) {
            System.out.println(fileEntry.getName());
            fileEntry.renameTo(new File("C:\\Users\\shipulin.mihail\\Desktop\\test\\Stoya\\4-"+fileEntry.getName()));
        }
    }
}
