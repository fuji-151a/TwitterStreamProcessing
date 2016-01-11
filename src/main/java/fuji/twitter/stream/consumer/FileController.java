/**
 * FileController.java
 *
 * @since 2015/12/30
 * <p/>
 * Copyright 2015 fuji-151a
 * All Rights Reserved.
 */
package fuji.twitter.stream.consumer;

import com.google.gson.Gson;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * File Controller class.
 * @author fuji-151a
 */
public class FileController {

    /**
     * file extension.
     */
    private final String ext;

    /**
     * file size threshold.
     */
    private final long thr;

    private final Gson gson = new Gson();

    /**
     * store file name.
     */
    private String fileName;

    /**
     * store date dir.
     */
    private String dateDir;

    /**
     * default store file size threshold.
     */
    private static final long THRESHOLD = 104857600;

    /**
     * default file extension.
     */
    private static final String EXTENSION = ".txt";

    private static final String ENCODE = "UTF-8";

    /**
     * line sparator.
     */
    private static final String BR
            = System.getProperty("line.separator");

    /**
     * sub string number.
     */
    private static final int SUBSTRING_NUM = 8;

    /**
     * @{link} Constructor.
     */
    public FileController() {
        this(EXTENSION, THRESHOLD);
    }

    /**
     * @{link} Constructor set params.
     * @param extension file extension.
     * @param threshold file size threshold.
     */
    public FileController(final String extension, final long threshold) {
        this.ext = extension;
        this.thr = threshold;
    }

    /**
     * make dir.
     * @param parent parent dir
     * @param outputPath output dir
     */
    public final void makeDateDir(final String parent,
                                  final String outputPath) {
        File dir = new File(parent, outputPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * check file exist and write status.
     * @param file write file instance
     * @return boolean true or false
     */
    public final boolean checkBeforeWriteFile(final File file) {
        if (file.exists()) {
            if (file.isFile() && file.canWrite()) {
                return true;
            }
        }
        return false;
    }

    /**
     * write data to file.
     * @param msg data
     * @param file write file
     * @throws JSONException if miss json parsing.
     * @throws IOException if miss file I/O
     */
    public final void write(final String msg, final File file)
            throws JSONException, IOException {
        file.createNewFile();
        if (checkBeforeWriteFile(file)) {
            JSONObject jsonObject = new JSONObject(msg);
            DateConverter dc
                    = new DateConverter(jsonObject.getString("createdAt"));
            String date = dc.convertDate();
            if (file.length() > thr) {
                this.fileName = date + ext;
                return;
            } else if (!dateDir.equals(date.substring(0, SUBSTRING_NUM))) {
                this.fileName = date + ext;
                String dir = date.substring(0, SUBSTRING_NUM);
                String rootDir = file.getParent().substring(
                        0,
                        file.getParent().length() - SUBSTRING_NUM);
                makeDateDir(rootDir, dir);
                this.dateDir = date.substring(0, SUBSTRING_NUM);
                return;
            } else {
                try (FileOutputStream fo = new FileOutputStream(file, true);
                     OutputStreamWriter ow
                             = new OutputStreamWriter(fo, "UTF-8")) {
                    ow.write(msg + BR);
                } catch (IOException e) {
                    throw new IOException();
                }
            }
        }
    }

    public final void logWrite(final String msg, final File file, final boolean bool) throws IOException {
        try (FileOutputStream fo = new FileOutputStream(file, bool);
             OutputStreamWriter ow
                     = new OutputStreamWriter(fo, ENCODE)) {
            ow.write(msg);
        }
    }

    /**
     * set date dir.
     * @param dirname date dir name
     */
    public final void setDateDir(final String dirname) {
        this.dateDir = dirname;
    }

    /**
     * set file name.
     * @param name file name.
     */
    public final void setFileName(final String name) {
        this.fileName = name + ".txt";
    }

    /**
     * get file name.
     * @return filename.
     */
    public final String getFileName() {
        return this.fileName;
    }

    /**
     * get date dir.
     * @return date dir
     */
    public final String getDirName() {
        return this.dateDir;
    }
}
