package cz.vacul.twittertest;

import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * Created by smuggler on 20.10.15.
 */
public interface TwitterCallback {
    void onFinish(TwitterException exception);
    void onUpdate(Status status);
}
