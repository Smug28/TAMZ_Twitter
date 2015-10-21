package cz.vacul.twittertest;

import android.os.AsyncTask;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by smuggler on 20.10.15.
 */
public class TweetTask extends AsyncTask<String, Status, TwitterException> {
    private TwitterCallback callback = null;
    private Twitter twitter;

    public TweetTask(Twitter twitter){
        super();
        this.twitter = twitter;
    }

    public TweetTask(Twitter twitter, TwitterCallback callback){
        super();
        this.twitter = twitter;
        this.callback = callback;
    }
    
    @Override
    protected TwitterException doInBackground(String... strings) {
        String latestStatus = strings[0];
        try {
            twitter4j.Status status = twitter.updateStatus(latestStatus);
            publishProgress(status);
        } catch (TwitterException e) {
            return e;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(twitter4j.Status... values) {
        if (callback != null)
            callback.onUpdate(values[0]);
    }

    @Override
    protected void onPostExecute(TwitterException exception) {
        if (callback != null)
            callback.onFinish(exception);
    }
}
