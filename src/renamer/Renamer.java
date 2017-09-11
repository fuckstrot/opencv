package renamer;

import java.io.File;

public class Renamer {
    public static void main(String[] args) {
        rename();
//        File inputFolder = new File("C:\\Users\\shipulin.mihail\\Desktop\\lfw-funneled\\lfw_funneled\\");
//        for (final File fileEntry : inputFolder.listFiles()) {
//            System.out.println(fileEntry.getName()+" "+fileEntry.isDirectory());
//            //fileEntry.renameTo(new File("C:\\Users\\shipulin.mihail\\Desktop\\test\\2 Mike\\2-"+fileEntry.getName()));
//        }
    }
    
    public static void rename(){
        File inputFolder = new File("C:\\Users\\shipulin.mihail\\Desktop\\test\\8 Nastya\\");
        for (final File fileEntry : inputFolder.listFiles()) {
            System.out.println(fileEntry.getName());
            fileEntry.renameTo(new File("C:\\Users\\shipulin.mihail\\Desktop\\test\\8 Nastya\\8-"+fileEntry.getName()));
        }
    }
}
