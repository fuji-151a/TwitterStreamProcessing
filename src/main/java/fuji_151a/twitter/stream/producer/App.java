/**
 * App.java
 *
 * @since 2015/11/01
 */
package fuji_151a.twitter.stream.producer;


import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;

/**
 * Main Application.
 * @author fuji-151a
 */
public class App {
    public static void main(String[] args) {

        // Define Access Token.
        String consumer_key = "hoge";
        String consumer_secret = "hoge";
        String access_token = "hoge";
        String secret_token = "hoge";
        AccessToken accessToken = new AccessToken(access_token, secret_token);

        // Create Twitter Stream Instance
        TwitterStreamFactory factory = new TwitterStreamFactory();
        TwitterStream twitterStream = factory.getInstance();
        twitterStream.setOAuthConsumer(consumer_key, consumer_secret);
        twitterStream.setOAuthAccessToken(accessToken);
        twitterStream.addListener(new Listener());

        // get tweet stream.
        twitterStream.sample();
    }
}
