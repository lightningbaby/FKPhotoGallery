package com.example.ttc.fkphotogallery;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ttc on 2017/3/9.
 */

public class PhotoGalleryFragment extends Fragment{
    private RecyclerView mPhotoRecyclerView;
    private static final String TAG="TweetFetchFragment";
    private List<Tweet> mItems = new ArrayList<>();
    private ThumbnailDownloader<PhotoHolder> mPhotoHolderThumbnailDownloader;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);//need to learn more
        new FetchItemsTask().execute();

        Handler responseHandler=new Handler();

        mPhotoHolderThumbnailDownloader=new ThumbnailDownloader<>(responseHandler);
        mPhotoHolderThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
                    @Override
                    public void onThumbnailDownloaded(PhotoHolder photoHolder, Bitmap thumbnail) {
                        Drawable drawable=new BitmapDrawable(getResources(),thumbnail);
                        photoHolder.bindDrawable(drawable);
                    }
                }
        );
        mPhotoHolderThumbnailDownloader.start();
        mPhotoHolderThumbnailDownloader.getLooper();
        Log.i(TAG,"Background thread started");

    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * 实例View ，实例RecyclerView,使用layoutManager，设置类型，访问网站，获取数据，设置adapter
     */
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_photo_gallery,container,false);
        mPhotoRecyclerView=(RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycler_view);

        mPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        new FetchItemsTask().execute();

        setupAdapter();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPhotoHolderThumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPhotoHolderThumbnailDownloader.quit();
        Log.i(TAG,"Backeground thread destroyed");
    }
//it is Void ,not void

    /**
     * doInBackground启用后台线程来获取数据，当后台完成任务后用onPostExecute更新UI
     */
    private class FetchItemsTask extends AsyncTask<Void,Void,List<Tweet>> {
        @Override
        protected List<Tweet> doInBackground(Void... voids) {
//
            return new TweetFetch().fetchItems("test");
        }

        /**
         *当后台完成任务后用onPostExecute更新UI
         */
        @Override
        protected void onPostExecute(List<Tweet> items) {
            mItems = items;
            setupAdapter();
        }
    }

    //set view
    private class PhotoHolder extends RecyclerView.ViewHolder {
        //VH 里放 View组件
        private ImageView mTweetPortraitImageView;
        private TextView mAuthorTextView;
        private TextView mTweetBodyTextView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mTweetPortraitImageView = (ImageView) itemView
                    .findViewById(R.id.tweet_portrait_image_view);
            mAuthorTextView= (TextView) itemView.findViewById(R.id.tweet_author_text_view);
            mTweetBodyTextView= (TextView) itemView.findViewById(R.id.tweet_body_text_view);
        }
        public void bindDrawable(Drawable drawable) {
//            mTweetPortraitImageView.setImageDrawable(drawable);

        }
        public void bindTweetOthers(Tweet tweet){
            mAuthorTextView.setText(tweet.getAuthor().toString());
            mTweetBodyTextView.setText(tweet.getTweetBody().toString());
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        //Adapter里放模型
        private List<Tweet> mTweets;

        public PhotoAdapter(List<Tweet> tweets) {
            mTweets = tweets;
        }

        /**
         * @param viewGroup
         * @param viewType
         * 这个函数是搞啥嘞？？？？？？？？？？？？
         */
        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item, viewGroup, false);
            return new PhotoHolder(view);
        }
        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            Tweet tweet = mTweets.get(position);
//            Drawable placeholder = getResources().getDrawable(R.drawable.bill_up_close);
//            photoHolder.bindDrawable(tweet.getPortraitUrl().);
            mPhotoHolderThumbnailDownloader.queueThumbanail(photoHolder, tweet.getPortraitUrl());
            photoHolder.bindTweetOthers(tweet);



        }
        @Override
        public int getItemCount() {
            return mTweets.size();
        }
    }

    //关联adapter
    private void setupAdapter() {
        if (isAdded()) {
//            判断该Fragment是否已经与Activity关联
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }
}
