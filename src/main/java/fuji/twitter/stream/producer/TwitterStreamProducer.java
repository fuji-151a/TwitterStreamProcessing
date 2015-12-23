/**
 * TwitterStreamProducer.java
 *
 * @since 2015/12/23
 * <p/>
 * Copyright 2015 fuji-151a
 * All Rights Reserved.
 */
package fuji.twitter.stream.producer;

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
    /**
     * Twitter Stream.
     */
    private TwitterStream twitterStream;

    /**
     * consumer key.
     */
    private String consumerKey;

    /**
     * consumer secret.
     */
    private String consumerSecret;

    /**
     * access token.
     */
    private String accessToken;

    /**
     * secret token.
     */
    private String secretToken;

    /**
     * constructor.
     * @param fileName config file.
     */
    public TwitterStreamProducer(final String fileName) {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(fileName));
            configCheck(prop);
            this.consumerKey = prop.getProperty("consumerKey");
            this.consumerSecret = prop.getProperty("consumerSecret");
            this.accessToken = prop.getProperty("accessToken");
            this.secretToken = prop.getProperty("secretToken");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Set up application.
     * @param producer SimpleKafkaProducer
     */
    public final void setup(final SimpleKafkaProducer producer) {
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

    /**
     * check validator.
     * @param prop properties file.
     */
    private void configCheck(final Properties prop) {
        Preconditions.checkNotNull(
                prop.getProperty("consumerKey"),
                "Not found Consumer Key");
        Preconditions.checkNotNull(
                prop.getProperty("consumerSecret"),
                "Not found Consumer Secret");
        Preconditions.checkNotNull(
                prop.getProperty("accessToken"),
                "Not found Access Token");
        Preconditions.checkNotNull(
                prop.getProperty("secretToken"),
                "Not found secret Token");
    }

    /**
     * run twitterStream.
     * @param lang filter lang
     */
    public final void run(final String lang) {
        twitterStream.sample(lang);
    }
}
