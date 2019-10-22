package com.example.udacity_android_developer_nanodegree_project2.objects;

/**
 * The type Movie review.
 */
public class MovieReview {

    private String mAuthor;
    private String mContent;

    /**
     * Instantiates a new Movie review.
     */
    public MovieReview(){

    }

    /**
     * Instantiates a new Movie review.
     *
     * @param author  the author
     * @param content the content
     */
    public MovieReview(String author, String content) {
        this.mAuthor = author;
        this.mContent = content;
    }

    /**
     * Gets author.
     *
     * @return the author
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Sets author.
     *
     * @param author the author
     */
    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return mContent;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.mContent = content;
    }
}
