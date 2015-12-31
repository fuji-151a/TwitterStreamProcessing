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
import twitter4j.JSONObject;

import java.io.File;
import java.util.List;

/**
 *
 * @author fuji-151a
 */
public class TwitterStreamFetcher {

    /** output dir root path. */
    private String rootPath;

    private SimpleKafkaConsumer kc;
    private FileController filectrl;


//    public static void main(final String[] args)
//            throws Exception {
//        if (args.length < 2) {
//            System.out.println("Please set ConfFile");
//            System.out.println("Please set outputdir");
//            System.exit(1);
//        }
//        SimpleKafkaConsumer kc = new SimpleKafkaConsumer(args[0]);
//        TwitterStreamFetcher tsf = new TwitterStreamFetcher();
//        List<KafkaStream<String, String>> streams = kc.consume();
//        boolean flag = true;
//        tsf.setOutputRootDir(args[1]);
//        tsf.setFileName(tsf.getToday() + EXTENSION);
//        for (KafkaStream<String, String> stream : streams) {
//            for (MessageAndMetadata<String, String> msg : stream) {
//                if (flag) {
//                    JSONObject jsonObject =
//                            new JSONObject(msg.message());
//                    String createAt =
//                            tsf.convertDate(jsonObject.getString("createdAt"));
//                    tsf.setFileName(createAt + ".txt");
//                    tsf.makeDir(createAt);
//                    flag = false;
//                }
//                String data = msg.message();
//                tsf.write(data);
//            }
//        }
//    }

    public TwitterStreamFetcher() {

    }

    public TwitterStreamFetcher(SimpleKafkaConsumer consumer, final String rootDir) {
        this.rootPath = rootDir;
        this.kc = consumer;
        this.filectrl = new FileController();
    }

    public void run() throws Exception {
        List<KafkaStream<String, String>> streams = kc.consume();
        boolean flag = true;
        for (KafkaStream<String, String> stream : streams) {
            for (MessageAndMetadata<String, String> msg : stream) {
                if (flag) {
                    JSONObject jsonObject =
                            new JSONObject(msg.message());
                    DateConverter dc = new DateConverter(jsonObject.getString("createdAt"));
                    String createAt =
                            dc.convertDate();
                    filectrl.setFileName(createAt);
                    String outputPath = rootPath + createAt.substring(0, 8) + "/";
                    filectrl.makeDateDir(outputPath);
                    filectrl.setDateDir(createAt.substring(0, 8));
                    flag = false;
                }
                String data = msg.message();
                File file = new File(filectrl.getFileName(), rootPath);
                filectrl.write(data, file);
            }
        }
    }
}
