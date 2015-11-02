package com.kru13.twittertest;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

import cz.vacul.twittertest.DialogActivity;
import cz.vacul.twittertest.R;
import cz.vacul.twittertest.TweetAdapter;
import cz.vacul.twittertest.TweetTask;
import cz.vacul.twittertest.TweetsFragment;
import cz.vacul.twittertest.TwitterCallback;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends AppCompatActivity implements TweetsFragment.OnFragmentInteractionListener {

    private Twitter twitter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ucet - https://twitter.com/tamz2_projekty

        configureTwitter();
        setContentView(R.layout.activity_main);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Compose new tweet
                startActivityForResult(new Intent(getContext(), DialogActivity.class), 666);
                //getImage();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public Twitter getTwitter() {
        return twitter;
    }


    //http://twitter4j.org/en/configuration.html
    public void configureTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("oEJEAxEPjNgWb0O35QNVaA")
                .setOAuthConsumerSecret("2TyiPmQMpnYHPE3S8ITkIQWld5fjk6jQ5eGfTsG8kg")
                .setOAuthAccessToken("927024486-4X07W3nTicx2SG0dTccqsNzraAyT1G8Ffc4VvNqN")
                .setOAuthAccessTokenSecret("neehbYt9lBY6o29UdcMLsZ1Zs9vVLPPncOpivLoyXtA");
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    //http://twitter4j.org/en/code-examples.html
    public void sendTweet(String text) {
        new TweetTask(twitter, new TwitterCallback() {
            @Override
            public void onUpdate(Status s) {
                TweetAdapter a = (TweetAdapter) ((ListView) findViewById(R.id.timeline).findViewById(android.R.id.list)).getAdapter();
                a.add(s);
            }

            @Override
            public void onFinish(TwitterException exception) {
                if (exception != null) {
                    Toast.makeText(getContext(), exception.getErrorMessage(), Toast.LENGTH_LONG).show();
                    exception.printStackTrace();
                }
            }
        }).execute(text);
    }

    private Context getContext() {
        return this;
    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 666 && resultCode == RESULT_OK) {
            sendTweet(data.getStringExtra("text"));
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            updateImage(data.getData());
        }
    }

    public void getImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public static String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    public void updateImage(Uri data){
        try {
            //String selectedImagePath = getPath(selectedImageUri);
            File img = new File(getRealPathFromURI_API19(this, data));
            new AsyncTask<File, Void, Void>() {
                @Override
                protected Void doInBackground(File... files) {
                    try {
                        twitter.updateProfileImage(files[0]);
                        twitter.updateProfileBanner(files[0]);
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute(img);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
