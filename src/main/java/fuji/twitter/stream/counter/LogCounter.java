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
import twitter4j.JSONException;
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

    public static final int RANGE_MS = 300;

    public LogCounter() { }

    public LogCounter(final SimpleKafkaConsumer consumer,
                      final String storeRootPath) {
        this.kc = consumer;
        this.storePath = storeRootPath;
    }

    public final void execute() throws JSONException {
        List<KafkaStream<String, String>> streams = kc.consume();
        Map<Long, Integer> cnt = new HashMap<>();
        long tmp = 0;
        for (KafkaStream<String, String> stream : streams) {
            for (MessageAndMetadata<String, String> msg : stream) {
                JSONObject jsonObject =
                        new JSONObject(msg.message());
                Date date = new Date(jsonObject.getString("createdAt"));
                DateTime dateTime = new DateTime(date);
                long startUnixtime = dateTime.getMillis() / 1000L;
                long endUnixtime = getEndTime(startUnixtime);
                if (endUnixtime >= tmp) {
                    if (cnt.containsKey(endUnixtime)) {
                        cnt.put(endUnixtime, cnt.get(endUnixtime) + 1);
                    } else {
                        cnt.put(endUnixtime, 1);
                    }
                }
                if (cnt.size() != 1) {
                    long key = endUnixtime - RANGE_MS;
                    Date d = new Date(key * 1000);
                    DateTime dt = new DateTime(d);
                    System.out.println(dt.toString("yyyyMMddHHmm") + "\t" + cnt.get(key));
                    cnt.remove(key);
                }
                tmp = endUnixtime;
            }
        }
    }

    private long getEndTime(final long unixtime) {
        long plusTime = RANGE_MS - unixtime % RANGE_MS;
        return unixtime + plusTime;
    }
}
