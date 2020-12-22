package com.huoli.bbl.exodemo;

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
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.C;

import java.util.ArrayList;
import java.util.List;

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
    private SimpleExoPlayer player;
    private PlayerView playerView;

    private String[] videoList = new String[]{
            "https://qiniu.bangbangli.com/5GGZPVYQWLXNQZAFGY.mp4",
            "https://qiniu.bangbangli.com/WOKHDT2R7LL1OV9O3Q.mp4",
            "https://qiniu.bangbangli.com/T2HLOUE5P8R13X3ZAT.mp4"
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerView = findViewById(R.id.player_view);
        initializePlayer();
    }

    private void initializePlayer() {
        if (player==null){
            player =new SimpleExoPlayer.Builder(this).build();
            //            Uri uri = Uri.parse(getString(R.string.url_dash));
            List<MediaSource> playlist = new ArrayList<>();
            for(String url :videoList){
                Uri uri = Uri.parse(url);
                MediaSource mediaSource = getMediaSource(uri);
                playlist.add(mediaSource);
            }
            ConcatenatingMediaSource concatenatedSource = new ConcatenatingMediaSource(
                    playlist.toArray(new MediaSource[playlist.size()]));
            player.prepare(concatenatedSource);
            player.seekTo(1, C.TIME_UNSET);
            player.setRepeatMode(2);
            //到哪一个位置
//            player.seekTo(currentWindow, playbackPosition);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(player != null){
            playerView.setPlayer(player);
            player.setPlayWhenReady(true);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放播放器
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    public MediaSource getMediaSource(Uri uri) {
        int streamType  = Util.inferContentType(uri.getLastPathSegment());
        switch (streamType) {
            case C.TYPE_SS:
                return new SsMediaSource.Factory(new DefaultDataSourceFactory(this, null,
                        getHttpDataSourceFactory(true))).createMediaSource(uri);
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(new DefaultDataSourceFactory(this, null,
                        getHttpDataSourceFactory(true))).createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(getDataSourceFactory(true)).createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(getHttpDataSourceFactory(true)).createMediaSource(uri);
            default: {
                throw new IllegalStateException("Unsupported type: " + streamType);
            }
        }
    }

    private DataSource.Factory getDataSourceFactory(boolean preview) {
        return new DefaultDataSourceFactory(this,Util.getUserAgent(this,"exo-aillience"));
    }

    private DataSource.Factory getHttpDataSourceFactory(boolean preview) {
        return new DefaultHttpDataSourceFactory(Util.getUserAgent(this,
                "exo-aillience"));
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
