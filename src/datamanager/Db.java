package datamanager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Db {
    FileOutputStream fout;
    ObjectOutputStream oos;
    
    public Db() {
        try {
            fout = new FileOutputStream("C:\\Users\\shipulin.mihail\\Desktop\\test\\db\\data.ser");
            oos = new ObjectOutputStream(fout);
        } catch (Exception ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void save(Object o){
        try {
            oos.writeObject(o);
        } catch (Exception ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}
