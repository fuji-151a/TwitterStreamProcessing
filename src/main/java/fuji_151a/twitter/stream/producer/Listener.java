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

    /**
     * Filter Lang.
     */
    private static final String FILETER_LANG = "ja";

    // Tweetを受け取るたびにこのメソッドが呼び出される
    @Override
    public void onStatus(Status status) {
        if (FILETER_LANG.equals(status.getLang())) {
            String json = gson.toJson(status);
            System.out.println(json);
        }
    }

}

