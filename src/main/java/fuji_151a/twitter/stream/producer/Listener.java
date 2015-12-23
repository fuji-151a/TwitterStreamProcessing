/**
 * Listener.java
 *
 * @since 2015/12/20
 * <p/>
 * Copyright 2015 fuji-151a
 * All Rights Reserved.
 */
package fuji_151a.twitter.stream.producer;

import com.google.gson.Gson;
import twitter4j.Status;
import twitter4j.StatusAdapter;

/**
 * Define action when app get tweet.
 * @author fuji-151a
 */
public class Listener extends StatusAdapter {
    /**
     * Gson Instance.
     */
    private final Gson gson = new Gson();

    private final SimpleKafkaProducer skp;


    public Listener(SimpleKafkaProducer _skp) {
        this.skp = _skp;
    }

    // Tweetを受け取るたびにこのメソッドが呼び出される
    @Override
    public void onStatus(Status status) {
        skp.produce(gson.toJson(status));
    }

}

