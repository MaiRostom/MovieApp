package com.ex.mairostom.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity  {

    public boolean two_ui=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivityFragment mainActivityFragment;

        if(savedInstanceState == null) {
            mainActivityFragment = new MainActivityFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.one_fragment,
                            mainActivityFragment, "myFragment").commit();
        }else{
            mainActivityFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag("myFragment");
        }

        mainActivityFragment.setConnectListerner(new MyConnectListener());
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        FrameLayout fragment_two = (FrameLayout) findViewById(R.id.twofragment);
        if(fragment_two !=null){
            two_ui=true;
            two_ui=true;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private class MyConnectListener implements Two_ui{

        @Override
        public void getConnetListener(Movie movie) {
            if (two_ui){
                DetailActivityFragment detailActivityFragment=new DetailActivityFragment();
                Bundle bundle=new Bundle();
                bundle.putParcelable("film",movie);
                detailActivityFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.twofragment,detailActivityFragment).commit();

            }else {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                //intent.putExtra("text",mData.get(position).getPosterTitle());
                intent.putExtra("film",movie);
                startActivity(intent);
            }
        }
    }
}
