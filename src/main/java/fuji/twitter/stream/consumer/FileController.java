/**
 * FileController.java
 *
 * @since 2015/12/30
 * <p/>
 * Copyright 2015 fuji-151a
 * All Rights Reserved.
 */
package fuji.twitter.stream.consumer;

import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 *
 * @author fuji-151a
 */
public class FileController {

    private final String ext;
    private final long thr;

    private String fileName;

    private String dateDir;

    /** File Sizeの閾値. */
    private static final long THRESHOLD = 104857600;
    private static final String EXTENSION = ".txt";

    public FileController() {
        this(EXTENSION, THRESHOLD);
    }
    public FileController(final String extension, final long threshold) {
        this.ext = extension;
        this.thr = threshold;
    }

    /** 改行コード. */
    private static String BR
            = System.getProperty("line.separator");

    public void makeDateDir(String outputPath) {
        File dir = new File(outputPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public boolean checkBeforeWriteFile(final File file) {
        if (file.exists()) {
            if (file.isFile() && file.canWrite()) {
                return true;
            }
        }
        return false;
    }

    // Testしづらい修正の余地大幅にあり．
    public void write(final String msg, final File file) throws JSONException, IOException {
        file.createNewFile();
        if (checkBeforeWriteFile(file)) {
            JSONObject jsonObject = new JSONObject(msg);
            DateConverter dc = new DateConverter(jsonObject.getString("createdAt"));
            String date = dc.convertDate();
            if (file.length() > thr) {
                this.fileName = date + ext;
                return;
            } else if (!dateDir.equals(date.substring(0, 8))) {
                String dir = fileName.substring(0, 8);
                makeDateDir(dir);
                dateDir = date.substring(0, 8);
                return;
            } else {
                try (FileOutputStream fo = new FileOutputStream(file, true);
                     OutputStreamWriter ow = new OutputStreamWriter(fo, "UTF-8")) {
                    ow.write(msg + BR);
                } catch (IOException e) {
                    throw new IOException();
                }
            }
        }
    }

    public void setDateDir(final String dirname) {
        this.dateDir = dirname;
    }

    public void setFileName(final String name) {
        this.fileName = name + ".txt";
    }

    public String getFileName() {
        return this.fileName;
    }
}
