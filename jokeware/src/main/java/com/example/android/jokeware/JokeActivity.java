package com.example.android.jokeware;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {

    private String mJokeText;
    private static final String JOKE_KEY = "joke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(JOKE_KEY)) {
            mJokeText = extras.getString(JOKE_KEY);
        }

        TextView jokeView = (TextView) findViewById(R.id.joke_tv);
        jokeView.setText(mJokeText);
    }
}
