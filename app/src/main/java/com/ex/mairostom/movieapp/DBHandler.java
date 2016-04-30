package com.ex.mairostom.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import data_base.Favourite_info;

/**
 * Created by Mai Rostom on 4/13/2016.
 */

public class DBHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "favourite1";






    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_FAVOURITES_TABLE = "CREATE TABLE " + Favourite_info.Favourite.TABLE_NAME + " ("+
                Favourite_info.Favourite.OVERVIEW + " TEXT, "+
                Favourite_info.Favourite.POSTERID + " TEXT, " +
                Favourite_info.Favourite.POSTER_TITLE + " TEXT, " +
                Favourite_info.Favourite.REALASEdATE + " TEXT, "+
                Favourite_info.Favourite.VOTE_AVRAGE + " TEXT, "+
                Favourite_info.Favourite.POSTERPATH+" TEXT "+ " );";
        db.execSQL(CREATE_FAVOURITES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Favourite_info.Favourite.TABLE_NAME);
        // Create tables again
        onCreate(db);

    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    // Adding new favourites

    void addFavourite(Movie movie) {

        ContentValues values = new ContentValues();
        String poster_ID=String.valueOf(movie.getPosterID());
        String vote_avrage=String.valueOf(movie.getAvergeCount());
        values.put(Favourite_info.Favourite.POSTER_TITLE, movie.getPosterTitle());
        values.put(Favourite_info.Favourite.REALASEdATE, movie.gettReleaseDate());
        values.put(Favourite_info.Favourite.OVERVIEW, movie.getOverView());
        values.put(Favourite_info.Favourite.POSTERPATH, movie.getPosterPath());
        values.put(Favourite_info.Favourite.POSTERID,poster_ID);
        values.put(Favourite_info.Favourite.VOTE_AVRAGE,vote_avrage);
        SQLiteDatabase db = this.getWritableDatabase();
        // Inserting Row
        long roid=db.insert(Favourite_info.Favourite.TABLE_NAME, null, values);
        Log.v("mai",""+roid);

        db.close(); // Closing database connection
    }

    // Getting All Favourites
//    Favourite_info.Favourite.KEY_ID + " INTEGER PRIMARY KEY, " +0
//    Favourite_info.Favourite.POSTERID + " TEXT, " +1
//    Favourite_info.Favourite.TITLE + " TEXT, " +2
//    Favourite_info.Favourite.REALASEdATE + " TEXT, "+3
//    Favourite_info.Favourite.OVERVIEW + "TEXT, "+4
//    Favourite_info.Favourite.VOTE_AVRAGE + "TEXT, "+5
//    Favourite_info.Favourite.POSTERPATH+" TEXT "+ " );";6
    public ArrayList<Movie> getAllContacts() {
        ArrayList<Movie> movieList = new ArrayList<Movie>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Favourite_info.Favourite.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();

         movie.setPosterID(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Favourite_info.Favourite.POSTERID))));
                movie.setPosterTitle(cursor.getString(cursor.getColumnIndex(Favourite_info.Favourite.POSTER_TITLE)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(Favourite_info.Favourite.REALASEdATE)));
                movie.setOverView(cursor.getString(cursor.getColumnIndex(Favourite_info.Favourite.OVERVIEW)));
                movie.setAvergeCount(Double.valueOf(cursor.getString(cursor.getColumnIndex(Favourite_info.Favourite.VOTE_AVRAGE))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(Favourite_info.Favourite.POSTERPATH)));

                // Adding contact to list
                movieList.add(movie);
            } while (cursor.moveToNext());
        }

        // return contact list
        return movieList;
    }
    // Getting contacts Count
//    public int getFavouritesCount() {
//        int count=0;
//        String countQuery = "SELECT  * FROM " + TABLE_FAVOURITES;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        if(cursor != null && !cursor.isClosed()){
//            count = cursor.getCount();
//            cursor.close();
//        }
//        // return count
//        return count;
//    }
}

