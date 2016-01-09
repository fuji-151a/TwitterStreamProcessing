/**
 * SimpleKafkaConsumer.java
 *
 * @since 2015/12/24
 * <p/>
 * Copyright 2015 fuji-151a
 * All Rights Reserved.
 */
package fuji.twitter.stream.consumer;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.Decoder;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Simple Kafka Consumer class.
 * @author fuji-151a
 */
public class SimpleKafkaConsumer {

    /**
     * consumer.
     */
    private ConsumerConnector consumer;
    /**
     * topic name.
     */
    private String topic;

    /**
     * partition num.
     */
    private int partition;

    /**
     * Setting Constructor.
     * @param fileName config file.
     */
    public SimpleKafkaConsumer(final String fileName) {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(fileName));
            this.topic = prop.getProperty("topic");
            this.partition = Integer.valueOf(prop.getProperty("partition"));
            ConsumerConfig config = new ConsumerConfig(prop);
            consumer = Consumer.createJavaConsumerConnector(config);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * check config file.
     * @param prop Properties instance.
     */
    private void configCheck(final Properties prop) {
        Preconditions.checkNotNull(
                prop.getProperty("topic"),
                "Not set topic"
        );
        Preconditions.checkNotNull(
                prop.getProperty("group.id"),
                "Not set group.id"
        );
        Preconditions.checkNotNull(
                prop.getProperty("zookeeper.connect"),
                "Not set zookeeper.connect"
        );
        Preconditions.checkNotNull(
                prop.getProperty("partition"),
                "Not set partition num"
        );
    }

    /**
     * Consumer.
     * @return List型のKafkaStream.
     */
    public final List<KafkaStream<String, String>> consume() {
        Map<String, Integer> topicCountMap = ImmutableMap.of(topic, partition);
        Decoder<String> decoder = new StringDecoder(new VerifiableProperties());
        return consumer.createMessageStreams(
                topicCountMap, decoder, decoder).get(topic);
    }

}
