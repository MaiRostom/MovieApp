package com.ex.mairostom.movieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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
public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {
    ImageAdapter imageAdapter;
    private GridView gridview;
    private LayoutInflater inflater;
    public ArrayList<Movie> mData;
    private Two_ui connectListener;
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onStart() {
        super.onStart();
       updatePosters();

    }
    public void updatePosters() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString("settings","popularity");

            MoviesPosters moviesPosters = new MoviesPosters();
            moviesPosters.execute(location);

    }
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updatePosters();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        gridview = (GridView) v.findViewById(R.id.gridview);
        gridview.setOnItemClickListener(this);
        return v;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //long forecast = imageAdapter.getItemId(position);
                //keda hyrg3 almovie kolo

                //Movie movie = (Movie) imageAdapter.getItem(position);
        //ArrayList<Movie> movie = getActivity().getIntent().getExtras().getParcelable("film");

        connectListener.getConnetListener(mData.get(position));

    }
    public void setConnectListerner(Two_ui lisener_two_ui1) {
        connectListener = lisener_two_ui1;
    }
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c , ArrayList<Movie> data) {
            mContext = c;
            mData = data;
            //initialization bta3o m4 b3mlo b new  l2a b3mlha initailze b system
if(mContext!=null)
            inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


            if( convertView == null ) {
                //We must create a View:
                //(()getContext()).getLayoutInflater().
                //contentView m2sod beha alview aly ana wa2f 3lah..al7aga aly fe aset image hy7otha fe convert view
                convertView = inflater.inflate(R.layout.setimage, parent, false);
            }
            //Here we can do changes to the convertView, such as set a text on a TextView
            //or an image on an ImageView.

            ImageView imageView=(ImageView)convertView.findViewById(R.id.image1);

            //Add image from image path of this object to the image view
            String posterUrl = "https://image.tmdb.org/t/p/w185" + mData.get(position).getPosterPath();
            Log.e("url", posterUrl);
            //get(0)
            Picasso.with(mContext).load(posterUrl).into(imageView);

            return convertView;
        }

    }
    public class MoviesPosters extends AsyncTask<String, Void, ArrayList<Movie>> {

        private final String LOG_TAG = MoviesPosters.class.getSimpleName();

        private ArrayList<Movie> getMoviesJson(String JsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String POPULAR = "popular";
            final String TOPRELATED="top_related";
            final String RESULTSPOSTERS="results";

            final String POSTERPATH="poster_path";
            final String OVERVIEW="overview";
            final String RELEASEDATE="release_date";
            final String ID="id";
            final String TITLE="title";
            final String VOTEAVERAGE="vote_average";


            JSONObject forecastJson = new JSONObject(JsonStr);
            JSONArray postersArray = forecastJson.getJSONArray(RESULTSPOSTERS);

            ArrayList<Movie>resultStrs=new ArrayList<>();
            for (int i = 0; i < postersArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String posterPath;
                int movieID;
                String posterTitle;
                String releaseDate;
                double avergeCount;
                String overView;
//da movie kamel
                JSONObject movieObject=postersArray.getJSONObject(i);
                posterPath=movieObject.getString(POSTERPATH);
                movieID=movieObject.getInt(ID);
                posterTitle=movieObject.getString(TITLE);
                releaseDate=movieObject.getString(RELEASEDATE);
                avergeCount=movieObject.getDouble(VOTEAVERAGE);
                overView=movieObject.getString(OVERVIEW);
                resultStrs.add(new Movie(posterPath,movieID,posterTitle,releaseDate,avergeCount,overView));
                Log.e(LOG_TAG, "title" + posterTitle);
            }
            Log.e(LOG_TAG, "resultStrs"+ resultStrs.size());

            return resultStrs;

        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params[0].equals("favourites")) {
                DBHandler dbHandler = new DBHandler(getContext());
                mData = dbHandler.getAllContacts();
                return mData;

            } else {

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
                    final String FORECAST_BASE_URL =

                            "https://api.themoviedb.org/3/discover/movie?";
                    ///movie/{id}/reviews
                    final String APPID_PARAM = "api_key";
                    final String TYPE = "sort_by";
                    Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                            .appendQueryParameter(TYPE, params[0] + ".desc")
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

            }
            return null;
        }
        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            if (result != null) {

                imageAdapter=new ImageAdapter(getActivity(), result);
                gridview.setAdapter(imageAdapter);

                // New data is back from the server.  Hooray!
            }
        }
    }
}
