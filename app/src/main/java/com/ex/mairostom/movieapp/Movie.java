package com.ex.mairostom.movieapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mai Rostom on 3/25/2016.
 */
public class Movie implements Parcelable{
    String posterPath;
    int posterID;
    String posterTitle;
    String releaseDate;
    double avergeCount;
    String overView;

    public Movie() {
        this.posterPath = "";
        this.posterID = 0;
        this.posterTitle = "";
        this.releaseDate = "";
        this.avergeCount = 0.0;
        this.overView = "";
    }

    public Movie(String posterPath, int posterID, String posterTitle, String releaseDate, double avergeCount, String overView) {
        this.posterPath = posterPath;
        this.posterID = posterID;
        this.posterTitle = posterTitle;
        this.releaseDate = releaseDate;
        this.avergeCount = avergeCount;
        this.overView = overView;
    }
    // Parcelling part
    public Movie(Parcel in){
        this.posterPath=in.readString();
        this.posterID=in.readInt();
        this.posterTitle=in.readString();
        this.releaseDate=in.readString();
        this.avergeCount=in.readDouble();
        this.overView=in.readString();
    }
    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getPosterID() {
        return posterID;
    }

    public void setPosterID(int posterID) {
        this.posterID = posterID;
    }

    public String getPosterTitle() {
        return posterTitle;
    }

    public void setPosterTitle(String posterTitle) {
        this.posterTitle = posterTitle;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String gettReleaseDate() {
        return releaseDate;
    }
    public double getAvergeCount() {
        return avergeCount;
    }

    public void setAvergeCount(double avergeCount) {
        this.avergeCount = avergeCount;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }


    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.posterPath);
        dest.writeInt(this.posterID);
        dest.writeString(this.posterTitle);
        dest.writeString(this.releaseDate);
        dest.writeDouble(this.avergeCount);
        dest.writeString(this.overView);
    }
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }

    };


}

