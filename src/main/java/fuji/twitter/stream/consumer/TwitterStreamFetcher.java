/**
 * TwitterStreamFetcher.java
 *
 * @since 2015/12/26
 * <p/>
 * Copyright 2015 fuji-151a
 * All Rights Reserved.
 */
package fuji.twitter.stream.consumer;

import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.joda.time.DateTime;
import twitter4j.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

/**
 *
 * @author fuji-151a
 */
public class TwitterStreamFetcher {

    /** output fileName. */
    private String fileName;

    /** output dir root path. */
    private String rootPath;

    /** output dir path. */
    private String outputDir;

    /** String dateDir. */
    private String dateDir;

    private SimpleKafkaConsumer kc;

    /** File Sizeの閾値. */
    private static final long THRESHOLD = 104857600;

    /** 改行コード. */
    private static String BR
            = System.getProperty("line.separator");

    private static final String EXTENSION = ".txt";


    /**
     * KafkaからTwitterのデータをFetchする.
     * @param args 1:confFile
     * @throws Exception FileIO
     */
    public static void main(final String[] args)
            throws Exception {
        if (args.length < 2) {
            System.out.println("Please set ConfFile");
            System.out.println("Please set outputdir");
            System.exit(1);
        }
        SimpleKafkaConsumer kc = new SimpleKafkaConsumer(args[0]);
        TwitterStreamFetcher tsf = new TwitterStreamFetcher();
        List<KafkaStream<String, String>> streams = kc.consume();
        boolean flag = true;
        tsf.setOutputRootDir(args[1]);
        tsf.setFileName(tsf.getToday() + EXTENSION);
        for (KafkaStream<String, String> stream : streams) {
            for (MessageAndMetadata<String, String> msg : stream) {
                if (flag) {
                    JSONObject jsonObject =
                            new JSONObject(msg.message());
                    String createAt =
                            tsf.convertDate(jsonObject.getString("createdAt"));
                    tsf.setFileName(createAt + ".txt");
                    tsf.makeDir(createAt);
                    flag = false;
                }
                String data = msg.message();
                tsf.write(data);
            }
        }
    }

    public TwitterStreamFetcher() {

    }

    public TwitterStreamFetcher(SimpleKafkaConsumer consumer, final String rootDir) {
        this.rootPath = rootDir;
        this.kc = consumer;
        setFileName(getToday() + EXTENSION);
    }

    public void run() throws Exception {
        List<KafkaStream<String, String>> streams = kc.consume();
        boolean flag = true;
        FileController filectrl = new FileController(EXTENSION, THRESHOLD);
        for (KafkaStream<String, String> stream : streams) {
            for (MessageAndMetadata<String, String> msg : stream) {
                if (flag) {
                    JSONObject jsonObject =
                            new JSONObject(msg.message());
                    String createAt =
                            convertDate(jsonObject.getString("createdAt"));
                    setFileName(createAt);
                    String outputPath = rootPath + createAt.substring(0, 8) + "/";
                    filectrl.makeDateDir(outputPath);
                    setOutputDir(outputPath);
                    setDateDir(createAt.substring(0, 8));
                    flag = false;
                }
                String data = msg.message();
                File file = new File(fileName);
                write(data);
            }
        }
    }

    /**
     * file書き込み.
     * @param msg 書き込みデータ.
     * @throws Exception ファイルの読み込み失敗.
     */
    private void write(final String msg) throws Exception {
        String output = outputDir + fileName;
        File file = new File(output);
        FileOutputStream fo = new FileOutputStream(file, true);
        file.createNewFile();
        if (checkBeforeWriteFile(file)) {
            JSONObject jsonObject = new JSONObject(msg);
            String date = convertDate(jsonObject.getString("createdAt"));
            if (file.length() > THRESHOLD) {
                this.fileName = date + EXTENSION;
                return;
            } else if (!dateDir.equals(date.substring(0, 8))) {
                makeDir(date);
                return;
            }
            OutputStreamWriter ow = new OutputStreamWriter(fo, "UTF-8");
            ow.write(msg + BR);
            ow.close();
            fo.close();
        }
    }

    /**
     * ファイルの存在とファイル書き込み，ファイルかどうかの確かめ.
     * @param file file.
     * @return boolean
     */
    private boolean checkBeforeWriteFile(final File file) {
        if (file.exists()) {
            if (file.isFile() && file.canWrite()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Directoryの作成.
     * @param date twitterデータの日付
     */
    private void makeDir(final String date) {
        String outputPath = rootPath + date.substring(0, 8) + "/";
        File dir = new File(outputPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        setOutputDir(outputPath);
        setDateDir(date.substring(0, 8));
    }

    /**
     * File NameのSetter.
     * @param fName FileName.
     */
    private void setFileName(final String fName) {
        this.fileName = fName;
    }

    /**
     * OutputDir root のSetter.
     * @param dir outputDir.
     */
    private void setOutputRootDir(final String dir) {
        this.rootPath = dir;
    }

    /**
     * outputDirのsetter.
     * @param dir outputdir
     */
    private void setOutputDir(final String dir) {
        this.outputDir = dir;
    }

    /**
     * dateDirのsetter.
     * @param dir 日付．
     */
    private void setDateDir(final String dir) {
        this.dateDir = dir;
    }

    /**
     * 本日の日付をyyyyMMddHHmmの形式で作成.
     * @return 本日の日付.
     */
    private String getToday() {
        Date date = new Date();
        DateTime dt = new DateTime(date);
        return dt.toString("yyyyMMddHHmm");
    }

    /**
     * 指定した日付フォーマットに変換.
     * @param date created_atの値
     * @return yyyyMMddHHmmの形式
     */
    private String convertDate(final String date) {
        Date d = new Date(date);
        DateTime dt = new DateTime(d);
        return dt.toString("yyyyMMddHHmm");
    }
}
