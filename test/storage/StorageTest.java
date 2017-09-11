package storage;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author shipulin.mihail
 */
public class StorageTest {
    
    public StorageTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of save method, of class Storage.
     */
    @Test
    public void testSave() {
        System.out.println("save");
        String key = "testkey";
        String data = "testdata";
        Storage instance = new Storage();
        instance.save(key, data);
        String s = testRead();
        assertEquals("testdata", s);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

  
    public String testRead() {
        System.out.println("read");
        String key = "testkey";
        Storage instance = new Storage();
        String data = instance.read(key);
        return data;
    }

    /**
     * Test of close method, of class Storage.
     */
    @Test
    public void testClose() {
        System.out.println("close");
        Storage instance = new Storage();
        instance.close();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
        
    }
    
}
