/**
 * TwitterStreamProducer.java
 *
 * @since 2015/12/23
 * <p/>
 * Copyright 2015 fuji-151a
 * All Rights Reserved.
 */
package fuji_151a.twitter.stream.producer;

import com.google.common.base.Preconditions;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author fuji-151a
 */
public class TwitterStreamProducer {
    private TwitterStream twitterStream;
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String secretToken;

    public TwitterStreamProducer(final String fileName) {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(fileName));
            configCheck(prop);
            consumerKey = prop.getProperty("consumerKey");
            consumerSecret = prop.getProperty("consumerSecret");
            accessToken = prop.getProperty("accessToken");
            secretToken = prop.getProperty("secretToken");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public void setup(SimpleKafkaProducer producer) {
        // SetUp AccessToken
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(secretToken)
                .build();
        // Create Twitter Stream Instance
        TwitterStreamFactory factory = new TwitterStreamFactory(conf);
        twitterStream = factory.getInstance();
        twitterStream.addListener(new Listener(producer));
    }

    private void configCheck(Properties prop) {
        Preconditions.checkNotNull(prop.getProperty("consumerKey"), "Not found Consumer Key");
        Preconditions.checkNotNull(prop.getProperty("consumerSecret"), "Not found Consumer Secret");
        Preconditions.checkNotNull(prop.getProperty("accessToken"), "Not found Access Token");
        Preconditions.checkNotNull(prop.getProperty("secretToken"), "Not found secret Token");
    }

    public void run(final String lang) {
        twitterStream.sample(lang);
    }
}
