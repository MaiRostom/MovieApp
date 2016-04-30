package com.ex.mairostom.movieapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Mai Rostom on 4/27/2016.
 */
public class Description extends AppCompatActivity{
    TextView txtFullDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);
        txtFullDescription=(TextView)findViewById(R.id.txtFullDesc);
       String description=getIntent().getExtras().getString("desc");
        txtFullDescription.setText(description);

}}
