/**
 * KafkaProducer.java
 *
 * @since 2015/12/20
 * <p/>
 * Copyright 2015 fuji-151a
 * All Rights Reserved.
 */
package fuji_151a.twitter.stream.producer;

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
    public static final String DEFAULT_CONF = "tweet_producer.properties";
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
            propFileCheck(prop);
            producer = new KafkaProducer<String, String>(prop);
            topic = prop.getProperty("topic");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * propertiesFile Check.
     * @param prop : properties file
     * @return boolean
     */
    private boolean propFileCheck(final Properties prop) {
        if (prop.getProperty("topic") == null
                && prop.getProperty("zookeeper.connect") == null
                && prop.getProperty("metadata.broker.list") == null
                && prop.getProperty("serializer.class") == null) {
            System.out.println("---propertiesFile----");
            System.out.println("zookeeper.connect=zookeeperHost:Port");
            System.out.println("topic=TopicName");
            System.out.println("metadata.broker.list=brokerHost:Port");
            System.out.println("serializer.class="
                    + "kafka.serializer.StringEncoder");
            return false;
        }
        return true;
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
}
