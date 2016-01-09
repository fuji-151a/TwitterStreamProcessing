package fuji.twitter.stream.consumer;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * FileControllerTest.java
 *
 * @since 2015/12/31
 * <p/>
 * Copyright 2015 fuji-151a
 * All Rights Reserved.
 */
public class FileControllerTest {

    private final FileController fctrl = new FileController();

    @Test
    public void testMakeDateDir() throws Exception {
//        String testDir = "./build/tmp/test_dir";
        String testDir = "test_dir";
        String pare = "./build/tmp/";
        fctrl.makeDateDir(pare, testDir);
        File dir = new File(pare, testDir);
        assertThat(dir.exists(), is(true));
    }

    @Test
    public void testCheckBeforeWriteFileTrue() throws Exception {
        String testFile = "./build/tmp/test_file.txt";
        File file = new File(testFile);
        file.createNewFile();
        boolean actual = fctrl.checkBeforeWriteFile(file);
        assertThat(actual, is(true));
    }

    @Test
    public void testCheckBeforeWriteFileFalse() throws Exception {
        String testFile = "./build/tmp/test_file2.txt";
        File file = new File(testFile);
        boolean actual = fctrl.checkBeforeWriteFile(file);
        assertThat(actual, is(false));
    }

    @Test
    public void testWrite() throws Exception {
        String testFile = "./build/tmp/test_file3.txt";
        File file = new File(testFile);
        file.createNewFile();
        System.out.println(file.getParent());
        fctrl.setDateDir("20151231");
        String testData = "{\"createdAt\": \"Dec 31, 2015 1:00:11 AM\"}";
        fctrl.write(testData, file);
    }
}