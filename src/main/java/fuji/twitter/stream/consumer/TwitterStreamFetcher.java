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
 * Twitter data fetcher class.
 * @author fuji-151a
 */
public class TwitterStreamFetcher {

    /**
     * output dir root path.
     */
    private String rootPath;

    /**
     * Simple Kafka Consumer instance.
     */
    private SimpleKafkaConsumer kc;

    /**
     * FileController instance.
     */
    private FileController filectrl;

    /**
     * sub string number.
     */
    private static final int SUBSTRING_NUM = 8;

    /**
     * Constructor.
     */
    public TwitterStreamFetcher() { }

    /**
     * setting constructor.
     * @param consumer SimpleKafkaConsumer
     * @param rootDir store root path
     */
    public TwitterStreamFetcher(
            final SimpleKafkaConsumer consumer,
            final String rootDir
    ) {
        this.rootPath = rootDir;
        this.kc = consumer;
        this.filectrl = new FileController();
    }

    /**
     * execute.
     * @throws Exception IO or JSON
     */
    public final void execute() throws Exception {
        List<KafkaStream<String, String>> streams = kc.consume();
        boolean flag = true;
        for (KafkaStream<String, String> stream : streams) {
            for (MessageAndMetadata<String, String> msg : stream) {
                if (flag) {
                    JSONObject jsonObject =
                            new JSONObject(msg.message());
                    DateConverter dc = new DateConverter(
                            jsonObject.getString("createdAt"));
                    String createAt =
                            dc.convertDate();
                    filectrl.setFileName(createAt);
                    String yyyyMMdd = createAt.substring(0, SUBSTRING_NUM);
                    filectrl.makeDateDir(rootPath, yyyyMMdd);
                    filectrl.setDateDir(yyyyMMdd);
                    flag = false;
                }
                String data = msg.message();
                File file = new File(
                        rootPath + filectrl.getDirName(),
                        filectrl.getFileName());
                filectrl.write(data, file);
            }
        }
    }
}
