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

    public TwitterStreamFetcher() {

    }

    public TwitterStreamFetcher(SimpleKafkaConsumer consumer, final String rootDir) {
        this.rootPath = rootDir;
        this.kc = consumer;
        this.filectrl = new FileController();
    }

    public void execute() throws Exception {
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
//                    String outputPath = rootPath + createAt.substring(0, 8) + "/";
                    filectrl.makeDateDir(rootPath, createAt.substring(0, 8));
                    filectrl.setDateDir(createAt.substring(0, 8));
                    flag = false;
                }
                String data = msg.message();
                File file = new File(rootPath + filectrl.getDirName(), filectrl.getFileName());
                filectrl.write(data, file);
            }
        }
    }
}
