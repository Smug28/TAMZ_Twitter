package cz.vacul.twittertest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.List;

import twitter4j.Status;

/**
 * Created by smuggler on 20.10.15.
 */
public class TweetAdapter extends ArrayAdapter<Status> {
    private Context context;
    private int layoutResourceId;
    private List<Status> data = null;

    public TweetAdapter(Context context, int layoutResourceId, List<Status> data){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }


    @Override
    public void add(Status object) {
        this.data.add(0, object);
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TweetHolder holder = null;

        if (row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new TweetHolder();
            holder.author = (TextView) row.findViewById(R.id.author);
            holder.text = (TextView) row.findViewById(R.id.tweet_text);
            holder.avatar = (ImageView) row.findViewById(R.id.avatar);
            row.setTag(holder);
        }
        else {
            holder = (TweetHolder) row.getTag();
        }

        Status tweet = data.get(position);
        holder.text.setText(tweet.getText());
        holder.author.setText(tweet.getUser().getName());
        Ion.with(holder.avatar).load(tweet.getUser().getProfileImageURLHttps());

        return row;
    }

    static class TweetHolder {
        TextView author;
        TextView text;
        ImageView avatar;
    }
}
