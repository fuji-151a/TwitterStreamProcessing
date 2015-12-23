/**
 * KafkaProducer.java
 *
 * @since 2015/12/20
 * <p/>
 * Copyright 2015 fuji-151a
 * All Rights Reserved.
 */
package fuji_151a.twitter.stream.producer;

import com.google.common.base.Preconditions;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

/**
 * Kafka Producer.
 * @author fuji-151a
 */
public class SimpleKafkaProducer {

    private KafkaProducer<String, String> producer;
    private String topic;
    private final Random rnd = new Random();
    public static final String DEFAULT_CONF = "producerConfig.properties";
    private static final int RANDOM_NUM = 255;

    public SimpleKafkaProducer() {
        this(DEFAULT_CONF);
    }

    /**
     * コンストラクタ.Kafkaの設定ファイルを読み込む.
     * @param fileName : propertiesファイル名.
     */
    public SimpleKafkaProducer(final String fileName) {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(fileName));
            configCheck(prop);
            producer = new KafkaProducer<String, String>(prop);
            topic = prop.getProperty("topic");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void configCheck(final Properties prop) {
        Preconditions.checkNotNull(prop.getProperty("topic"), "Not Set topic");
        Preconditions.checkNotNull(prop.getProperty("bootstrap.servers"), "Not Set bootstrap.servers");
        Preconditions.checkNotNull(prop.getProperty("key.serializer"), "Not Set key.serializer");
        Preconditions.checkNotNull(prop.getProperty("value.serializer"), "Not Set value.serializer");
    }

    /**
     * produceする機能.
     * @param logData : 流したいデータ
     */
    public final void produce(final String logData) {
        String partitionerKey = "192.168.2." + rnd.nextInt(RANDOM_NUM);
        ProducerRecord<String, String> data
                = new ProducerRecord<String, String>(topic, partitionerKey, logData);
        producer.send(data);
    }

    public final void close() {
        producer.close();
    }
}
