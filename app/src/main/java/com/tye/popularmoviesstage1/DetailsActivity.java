package com.tye.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mTextView = findViewById(R.id.tv_details);

        Intent intent = getIntent();

        if(intent != null){
            if(intent.hasExtra(Intent.EXTRA_TEXT)){
                String text = String.valueOf(intent.getIntExtra(Intent.EXTRA_TEXT, 0));
                mTextView.setText(text);
            }
        }

    }
}
