package com.example.udacity_android_developer_nanodegree_project2.objects;

/**
 * The type Movie trailer.
 */
public class MovieTrailer {

    private String mName;
    private String mKey;

    /**
     * Instantiates a new Movie trailer.
     */
    public MovieTrailer(){

    }

    /**
     * Instantiates a new Movie trailer.
     *
     * @param name the m name
     * @param key  the m key
     */
    public MovieTrailer(String name, String key) {
        this.mName = name;
        this.mKey = key;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.mName = name;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public String getKey() {
        return mKey;
    }

    /**
     * Sets key.
     *
     * @param key the key
     */
    public void setKey(String key) {
        this.mKey = key;
    }
}
