package com.scau.easyfarm.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.scau.easyfarm.R;

import java.io.IOException;

/**
 * Created by chenhehong on 2017/10/26.
 */
public class VideoPlayActivity extends Activity{

    public static String BUNDLEKEY_VIDEOSOURCE_TYPE = "bundlekey_videosource_type";
    public static int VIDEOTYPEURL = 1;
    public static int VIDEOTYPEFILE = 2;
    public static String BUNDLEKEY_VIDEOSOURCE = "bundlekey_videosource";

    FullscreenVideoLayout videoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);

        videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
        videoLayout.setActivity(this);

        int videoType = getIntent().getExtras().getInt(BUNDLEKEY_VIDEOSOURCE_TYPE);
        String source = getIntent().getExtras().getString(BUNDLEKEY_VIDEOSOURCE);
        if (videoType==VIDEOTYPEURL){
            Uri videoUri = Uri.parse(source);
            try {
                videoLayout.setVideoURI(videoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(videoType==VIDEOTYPEFILE) {
            try {
                videoLayout.setVideoPath(source);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
