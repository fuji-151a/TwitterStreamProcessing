/**
 * LogCounter.java
 *
 * @since 2016/01/11
 * <p/>
 * Copyright 2016 fuji-151a
 * All Rights Reserved.
 */
package fuji.twitter.stream.counter;

import com.google.gson.Gson;
import fuji.twitter.stream.consumer.FileController;
import fuji.twitter.stream.consumer.SimpleKafkaConsumer;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.joda.time.DateTime;
import twitter4j.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fuji-151a
 */
public class LogCounter {

    private SimpleKafkaConsumer kc;

    private String storePath;

    private final Gson gson = new Gson();

    private final FileController fcl = new FileController();

    /**
     * line sparator.
     */
    private static final String BR
            = System.getProperty("line.separator");

    private static final String ENCODE = "UTF-8";

    private static final String EXTENSION = ".json";

    public LogCounter() { }

    public LogCounter(final SimpleKafkaConsumer consumer,
                      final String storeRootPath) {
        this.kc = consumer;
        this.storePath = storeRootPath;
    }

    public final void execute() throws Exception {
        List<KafkaStream<String, String>> streams = kc.consume();
        int count = 0;
        int secCnt = 0;
        long currentTime = 0L;
        String prevTimestamp = "";
        for (KafkaStream<String, String> stream : streams) {
            for (MessageAndMetadata<String, String> msg : stream) {
                JSONObject jsonObject =
                        new JSONObject(msg.message());
                Date date = new Date(jsonObject.getString("createdAt"));
                DateTime dateTime = new DateTime(date);
                String timestamp = dateTime.toString("yyyyMMddHHmm");
                if (count == 0) {
                    count++;
                    prevTimestamp = timestamp;
                } else if (!prevTimestamp.equals(timestamp)) {
                    File file = new File(storePath, dateTime.toString("yyyyMMdd") + EXTENSION);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("timestamp", timestamp);
                    map.put("log_num", count);
                    fcl.logWrite(gson.toJson(map) + BR, file, true);
                    map.clear();
                    count = 1;
                    prevTimestamp = timestamp;
                } else {
                    count++;
                }
                if (dateTime.getMillis() == currentTime + 1000) {
                    File file = new File(storePath, "data" + EXTENSION);
                    Map<String, Object> mapsec = new HashMap<String, Object>();
                    mapsec.put("timestamp", timestamp);
                    mapsec.put("log_num", secCnt);
                    fcl.logWrite(gson.toJson(mapsec) + BR, file, false);
                    mapsec.clear();
                    secCnt = 1;
                } else {
                    secCnt++;
                }
                currentTime = dateTime.getMillis();
            }
        }
    }
}
