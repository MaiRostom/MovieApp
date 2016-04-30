package com.ex.mairostom.movieapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private Movie movie;
    TextView txtFirstText, txtSecondText, txtDescription,txtTitle;
    ImageView imageView;
    ListView listView;
    Button btnReviews;
    public ArrayList<Trailer> mData;
    private LayoutInflater inflater;
    TrailerAdapter trailerAdapter;
    DBHandler db;
    Button btnFavourites;
    ArrayAdapter mFavourites;
    ScrollView scrollView;

    public DetailActivityFragment() {
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        //  Bundle bundle = getActivity().getIntent().getExtras();
        db = new DBHandler(getContext());
        Bundle args = getArguments();
        if (args == null)
            args = getActivity().getIntent().getExtras();
        movie = args.getParcelable("film");
        String title = movie.getPosterTitle();


        //getActivity().getActionBar().setTitle("Your Title");
        txtTitle=(TextView)v.findViewById(R.id.txttitle);
        txtFirstText = (TextView) v.findViewById(R.id.txtfirstText);
        txtSecondText = (TextView) v.findViewById(R.id.txtRating);
        txtDescription = (TextView) v.findViewById(R.id.txtDescription);
        imageView = (ImageView) v.findViewById(R.id.txtimage);
        btnReviews = (Button) v.findViewById(R.id.btnReviews);
        btnFavourites = (Button) v.findViewById(R.id.btnFavourite);
        ArrayList<Movie> movies1 = new ArrayList<Movie>();
        movies1 = db.getAllContacts();
        boolean x1 = true;
        for (int i = 0; i <= movies1.size() - 1; i++) {
            if (movie.getPosterID()==movies1.get(i).getPosterID()) {
                x1=false;

            }
        }
        if(x1==false){
            btnFavourites.setEnabled(false);
            btnFavourites.setBackgroundColor(Color.parseColor("#F0E68C"));
            btnFavourites.setText("IS A Favourite Movie");
        }

        scrollView = (ScrollView) v.findViewById(R.id.scrollviewid);
        txtTitle.setText(title);
        txtDescription.setText(movie.getOverView());
        txtDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Description.class).putExtra("desc", movie.getOverView());
                startActivity(intent);
            }
        });

        txtFirstText.setText(movie.gettReleaseDate());
        txtSecondText.setText(String.valueOf(movie.getAvergeCount()));
        btnReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMovieReviews getMovieReviews = new GetMovieReviews(movie.getPosterID() + "");
                getMovieReviews.execute();
            }
        });
        listView = (ListView) v.findViewById(R.id.listview_forecast);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = mData.get(position).key;

                Uri uri = Uri.parse("http://www.youtube.com/watch?v=" + key);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        btnFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                if (movies.size() == 0 || x == true) {
                    db.addFavourite(movie);
                btnFavourites.setBackgroundColor(Color.parseColor("#F0E68C"));
                    btnFavourites.setEnabled(false);
                btnFavourites.setText("IS A Favourite Movie");

//                }

            }
        });
        String posterUrl = "https://image.tmdb.org/t/p/w185" + movie.getPosterPath();
        Log.e("url", posterUrl);
        //get(0)
        Picasso.with(getActivity()).load(posterUrl).into(imageView);
        // String title= getActivity().getIntent().getExtras().getString("text");
        //Toast.makeText(getActivity(), movie.getPosterTitle() + "Y", Toast.LENGTH_LONG).show();
        GetTrailer getTrailer = new GetTrailer(movie.getPosterID() + "");

        getTrailer.execute();
        return v;
    }


    public class TrailerAdapter extends BaseAdapter {
        private Context mContext;

        public TrailerAdapter(Context c, ArrayList<Trailer> data) {
            mContext = c;
            mData = data;
            //initialization bta3o m4 b3mlo b new  l2a b3mlha initailze b system
            if(mContext!=null)
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return mData.size();
            //  return  1;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {


            if (convertView == null) {
                //We must create a View:
                //(()getContext()).getLayoutInflater().
                //contentView m2sod beha alview aly ana wa2f 3lah..al7aga aly fe aset image hy7otha fe convert view
                convertView = inflater.inflate(R.layout.list_item_trailer, null, false);
            }
            //Here we can do changes to the convertView, such as set a text on a TextView
            //or an image on an ImageView.

            TextView textView = (TextView) convertView.findViewById(R.id.list_item_trailer_textview);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.btnVideo);
            textView.setText(mData.get(position).name);

            return convertView;
        }

    }


    public class GetMovieReviews extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = GetMovieReviews.class.getSimpleName();

        String id;

        public GetMovieReviews(String id) {
            this.id = id;

        }

        private String getReviewsJson(String JsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.

            final String CONTENT = "content";

            JSONObject reviewsJson = new JSONObject(JsonStr);
            JSONArray reviewsArray = reviewsJson.getJSONArray("results");

            String resultStrs = "";
            for (int i = 0; i < reviewsArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
//da movie kamel
                JSONObject reviewsObject = reviewsArray.getJSONObject(i);
                resultStrs = reviewsObject.getString("content");
            }

            return resultStrs;

        }

        @Override
        protected String doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
//            if (params.length == 0) {
//                return null;
//            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String REVIEWS_BASE_URL =
                        "http://api.themoviedb.org/3/movie/" + id + "/reviews";//?api_key="+BuildConfig.api_key;


                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(REVIEWS_BASE_URL).buildUpon()

                        .appendQueryParameter(APPID_PARAM, BuildConfig.api_key)
                        .build();

                URL url = new URL(builtUri.toString());

                // Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, forecastJsonStr);

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getReviewsJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Intent intent = new Intent(getActivity(), ReviewsActivity.class).putExtra("REVIEWS", result);
                startActivity(intent);
            }
//            Toast.makeText(getActivity(),"the content is"+result,Toast.LENGTH_LONG).show();
            // New data is back from the server.  Hooray!
        }
    }

    public class GetTrailer extends AsyncTask<String, Void, ArrayList<Trailer>> {
        private final String LOG_TAG = Trailer.class.getSimpleName();
        String id;

        public GetTrailer(String id) {
            this.id = id;
        }

        private ArrayList<Trailer> getMoviesJson(String JsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String KEY = "key";
            final String NAME = "name";
            final String SITE = "site";
            final String TYPE = "type";
            final String RESULTSPOSTERS = "results";


            JSONObject trailerJson = new JSONObject(JsonStr);
            JSONArray resultArray = trailerJson.getJSONArray(RESULTSPOSTERS);

            ArrayList<Trailer> resultStrs = new ArrayList<>();
            for (int i = 0; i < resultArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String key;
                String name;
                String site;
                String type;
//da movie kamel
                JSONObject trailerObject = resultArray.getJSONObject(i);
                key = trailerObject.getString(KEY);
                name = trailerObject.getString(NAME);
                site = trailerObject.getString(SITE);
                type = trailerObject.getString(TYPE);

                resultStrs.add(new Trailer(key, name, site, type));

            }

            return resultStrs;

        }

        @Override
        protected ArrayList<Trailer> doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
//            if (params.length == 0) {
//                return null;
//            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String REVIEWS_BASE_URL =
                        "http://api.themoviedb.org/3/movie/" + id + "/videos";//?api_key="+BuildConfig.api_key;


                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(REVIEWS_BASE_URL).buildUpon()

                        .appendQueryParameter(APPID_PARAM, BuildConfig.api_key)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.d(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, forecastJsonStr);

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            try {
                return getMoviesJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> result) {
            if (result != null) {
                trailerAdapter = new TrailerAdapter(getActivity(), result);
                listView.setAdapter(trailerAdapter);

            }
        }
    }


}

