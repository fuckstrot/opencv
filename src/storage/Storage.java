package storage;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import java.io.File;

public class Storage {

    public String PATH = "C:\\Users\\shipulin.mihail\\Desktop\\test\\storage";
    public Environment myDbEnvironment = null;
    Database myDatabase = null;
    EnvironmentConfig envConfig = null;
    DatabaseConfig dbConfig = null;

    public Storage() {
        try {
            envConfig = new EnvironmentConfig();
            envConfig.setAllowCreate(true);
            myDbEnvironment = new Environment(new File(PATH), envConfig);
            // Open the database, creating one if it does not exist
            dbConfig = new DatabaseConfig();
            dbConfig.setAllowCreate(true);
            myDatabase = myDbEnvironment.openDatabase(null, "Faces", dbConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(String key, String data) {
        try {
            DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
            DatabaseEntry theData = new DatabaseEntry(data.getBytes("UTF-8"));
            myDatabase.put(null, theKey, theData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String read(String key) {
        try {
            // Create two DatabaseEntry instances:
            // theKey is used to perform the search
            // theData will hold the value associated to the key, if found
            DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
            DatabaseEntry theData = new DatabaseEntry();
            // Call get() to query the database
            if (myDatabase.get(null, theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                // Translate theData into a String.
                byte[] retData = theData.getData();
                String foundData = new String(retData, "UTF-8");
                System.out.println("key: '" + key + "' data: '"
                        + foundData + "'.");
                return foundData;
            } else {
                System.out.println("No record found with key '" + key + "'.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void close() {
        try {
            if (myDatabase != null) {
                myDatabase.close();
            }
            if (myDbEnvironment != null) {
                myDbEnvironment.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
