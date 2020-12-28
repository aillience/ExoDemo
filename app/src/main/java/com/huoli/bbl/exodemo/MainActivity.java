package com.huoli.bbl.exodemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.RepeatModeUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.C;
import com.huoli.bbl.exodemo.exoplayer.PlayerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * ===============================
 * 千万不要出现 B U G ，出现就 G G
 * THE BEST CODE IS NO CODE
 * ===============================
 *
 * @author: yfl
 * @date: 2020-12-22
 * @description: 就是一个普通类
 * @lastUpdateTime 2020-12-22
 * #更新内容
 * ===============================
 **/
public class MainActivity extends AppCompatActivity {

    private PlayerView playerView;
    private PlayerManager playerManager;

    private List<String> videoList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerView = findViewById(R.id.player_view);

        initializePlayer();
    }

    private void initializePlayer() {

        playerManager = new PlayerManager(playerView);
        playerManager.setRepeatMode(RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL);
        playerManager.setControllerShowTimeoutMs(0);
        playerManager.paly();

        ;
//        filterChinese("https://lijuanqiniu.mnnmedu.com/20190523151536045 希腊神话-阿尔忒弥斯与奥利温_标清.mp4");
//        filterChinese("https://lijuanqiniu.mnnmedu.com/201904041457512013法国旅游官方推广宣传片_标清_batch.mp4");

//        videoList.add("https://qiniu.bangbangli.com/5GGZPVYQWLXNQZAFGY.mp4");
//        videoList.add("https://lijuanqiniu.mnnmedu.com/201904041457512013法国旅游官方推广宣传片_标清_batch.mp4");
//        videoList.add("http://5.595818.com/2015/ring/000/140/6731c71dfb5c4c09a80901b65528168b.mp3");
        videoList.add("https://lijuanqiniu.mnnmedu.com/20190523151536045 希腊神话-阿尔忒弥斯与奥利温_标清.mp4");
//        videoList.add(ul);
//        videoList.add("https://lijuanqiniu.mnnmedu.com/20190523151536045%20%E5%B8%8C%E8%85%8A%E7%A5%9E%E8%AF%9D-%E9%98%BF%E5%B0%94%E5%BF%92%E5%BC%A5%E6%96%AF%E4%B8%8E%E5%A5%A5%E5%88%A9%E6%B8%A9_%E6%A0%87%E6%B8%85.mp4");
        videoList.add("https://lijuanqiniu.mnnmedu.com/20190404145745d - 大调卡农.mp3");
        playerManager.setPlayerUri(videoList);
//        startActivity(new Intent(this,DialogActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放播放器
        playerManager.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            requestFullScreenIfLandscape();
        }
    }


    private void requestFullScreenIfLandscape() {
        if (getResources().getBoolean(R.bool.landscape)) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

}
