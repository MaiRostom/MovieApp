package com.ex.mairostom.movieapp;

/**
 * Created by Mai Rostom on 4/23/2016.
 */
public class Trailer {
    String key;
    String name;
   String site;
    String type;
    public Trailer() {

    }
    public Trailer(String key, String name, String size, String type) {
        this.key = key;
        this.name = name;
        this.site = size;
        this.type = type;
    }

    public Trailer(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return site;
    }

    public void setSize(String size) {
        this.site = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
