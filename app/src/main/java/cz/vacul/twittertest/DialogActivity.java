package cz.vacul.twittertest;

/**
 * Created by smuggler on 20.10.15.
 */

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        View cardView = findViewById(R.id.dialog_card_add_server);
        cardView.getLayoutParams().width = (int) (getResources().getDisplayMetrics().widthPixels * 0.7);
        cardView.requestLayout();
        View.OnClickListener dismissListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                setResult(RESULT_CANCELED);
                if (Build.VERSION.SDK_INT >= 21)
                    finishAfterTransition();
                else
                    finish();
            }
        };
        ((View) cardView.getParent()).setOnClickListener(dismissListener);
        findViewById(R.id.dialog_add_server_cancel).setOnClickListener(dismissListener);
        findViewById(R.id.dialog_add_server_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
        cardView.setOnClickListener(null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void add(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent i = new Intent();
        i.putExtra("text", ((EditText) findViewById(R.id.dialog_new_tweet)).getText().toString());
        setResult(RESULT_OK, i);
        if (Build.VERSION.SDK_INT >= 21)
            finishAfterTransition();
        else
            finish();
    }
}
