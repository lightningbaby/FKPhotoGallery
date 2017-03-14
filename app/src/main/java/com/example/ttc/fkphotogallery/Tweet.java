package com.example.ttc.fkphotogallery;

/**
 * Created by ttc on 2017/3/9.
 */

public class Tweet {
    private String mTweetAuthorName;
    private String mTweetId;
    private String mPortraitUrl;
    private String mAuthorId;
    private String mTweetBody;
    private String mTweetPubDate;
    private String mCommentCount;

    public Tweet() {

    }

    public String getAuthor() {
        return mTweetAuthorName;
    }

    public void setAuthor(String author) {
        mTweetAuthorName = author;
    }

    public String getTweetId() {
        return mTweetId;
    }

    public void setTweetId(String tweetId) {
        mTweetId = tweetId;
    }

    public String getPortraitUrl() {
        return mPortraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        mPortraitUrl = portraitUrl;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(String authorId) {
        mAuthorId = authorId;
    }

    public String getTweetBody() {
        return mTweetBody;
    }

    public void setTweetBody(String tweetBody) {
        mTweetBody = tweetBody;
    }

    public String getTweetPubDate() {
        return mTweetPubDate;
    }

    public void setTweetPubDate(String tweetPubDate) {
        mTweetPubDate = tweetPubDate;
    }

    public String getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(String commentCount) {
        mCommentCount = commentCount;
    }
}
