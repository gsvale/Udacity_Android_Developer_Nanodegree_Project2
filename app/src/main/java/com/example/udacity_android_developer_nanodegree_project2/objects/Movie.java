package com.example.udacity_android_developer_nanodegree_project2.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

/**
 * The type Movie.
 */
@Entity(tableName = "movie")
public class Movie implements Parcelable {

    /**
     * The constant MOVIE_TAG.
     */
    public static final String MOVIE_TAG = Movie.class.getSimpleName();

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "id")
    private String mId;

    private String mPosterImageUrl;
    private String mDetailsImageUrl;
    private String mOriginalTitle;
    private String mPlotSynopsis;
    private String mUserRating;
    private String mReleaseDate;

    @Ignore
    private List<MovieTrailer> mTrailers;

    @Ignore
    private List<MovieReview> mReviews;


    /**
     * Instantiates a new Movie.
     */
    @Ignore
    public Movie() {
    }

    /**
     * Instantiates a new Movie.
     *
     * @param id              the id
     * @param posterImageUrl  the poster image url
     * @param detailsImageUrl the details image url
     * @param originalTitle   the original title
     * @param plotSynopsis    the plot synopsis
     * @param userRating      the user rating
     * @param releaseDate     the release date
     */
    public Movie(String id, String posterImageUrl, String detailsImageUrl, String originalTitle, String plotSynopsis, String userRating, String releaseDate) {
        this.mId = id;
        this.mPosterImageUrl = posterImageUrl;
        this.mDetailsImageUrl = detailsImageUrl;
        this.mOriginalTitle = originalTitle;
        this.mPlotSynopsis = plotSynopsis;
        this.mUserRating = userRating;
        this.mReleaseDate = releaseDate;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return mId;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.mId = id;
    }

    /**
     * Gets poster image url.
     *
     * @return the poster image url
     */
    public String getPosterImageUrl() {
        return mPosterImageUrl;
    }

    /**
     * Sets poster image url.
     *
     * @param posterImageUrl the poster image url
     */
    public void setPosterImageUrl(String posterImageUrl) {
        this.mPosterImageUrl = posterImageUrl;
    }

    /**
     * Gets details image url.
     *
     * @return the details image url
     */
    public String getDetailsImageUrl() {
        return mDetailsImageUrl;
    }

    /**
     * Sets details image url.
     *
     * @param detailsImageUrl the details image url
     */
    public void setDetailsImageUrl(String detailsImageUrl) {
        this.mDetailsImageUrl = detailsImageUrl;
    }

    /**
     * Gets original title.
     *
     * @return the original title
     */
    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    /**
     * Sets original title.
     *
     * @param originalTitle the original title
     */
    public void setOriginalTitle(String originalTitle) {
        this.mOriginalTitle = originalTitle;
    }

    /**
     * Gets plot synopsis.
     *
     * @return the plot synopsis
     */
    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }

    /**
     * Sets plot synopsis.
     *
     * @param plotSynopsis the plot synopsis
     */
    public void setPlotSynopsis(String plotSynopsis) {
        this.mPlotSynopsis = plotSynopsis;
    }

    /**
     * Gets user rating.
     *
     * @return the user rating
     */
    public String getUserRating() {
        return mUserRating;
    }

    /**
     * Sets user rating.
     *
     * @param userRating the user rating
     */
    public void setUserRating(String userRating) {
        this.mUserRating = userRating;
    }

    /**
     * Gets release date.
     *
     * @return the release date
     */
    public String getReleaseDate() {
        return mReleaseDate;
    }

    /**
     * Sets release date.
     *
     * @param releaseDate the release date
     */
    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    public List<MovieTrailer> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(List<MovieTrailer> trailers) {
        this.mTrailers = trailers;
    }

    public List<MovieReview> getReviews() {
        return mReviews;
    }

    public void setReviews(List<MovieReview> reviews) {
        this.mReviews = reviews;
    }

    protected Movie(Parcel in) {
        mId = in.readString();
        mPosterImageUrl = in.readString();
        mDetailsImageUrl = in.readString();
        mOriginalTitle = in.readString();
        mPlotSynopsis = in.readString();
        mUserRating = in.readString();
        mReleaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mPosterImageUrl);
        dest.writeString(mDetailsImageUrl);
        dest.writeString(mOriginalTitle);
        dest.writeString(mPlotSynopsis);
        dest.writeString(mUserRating);
        dest.writeString(mReleaseDate);
    }
}
