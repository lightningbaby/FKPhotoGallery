package com.example.ttc.fkphotogallery;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return  PhotoGalleryFragment.newInstance();
        //!!!!!!!!!!to dispatch a func which can return a fragment obj,so do not use"new"
        //and it is a public func
    }
}
