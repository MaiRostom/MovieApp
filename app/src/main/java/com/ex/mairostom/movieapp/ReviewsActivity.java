package com.ex.mairostom.movieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReviewsActivity extends AppCompatActivity {
TextView txtReviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        txtReviews=(TextView)findViewById(R.id.txtReviews);
        txtReviews.setText(getIntent().getExtras().getString("REVIEWS"));

    }
}
