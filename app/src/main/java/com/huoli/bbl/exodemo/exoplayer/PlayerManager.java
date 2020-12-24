package com.huoli.bbl.exodemo.exoplayer;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.C;

import java.util.ArrayList;
import java.util.List;

/**
 * ===============================
 * 千万不要出现 B U G ，出现就 G G
 * THE BEST CODE IS NO CODE
 * ===============================
 *
 * @author: yfl
 * @date: 2020-12-24
 * @description: 就是一个普通类
 * @lastUpdateTime 2020-12-24
 * #更新内容
 * ===============================
 **/
public class PlayerManager {
    private Context context;
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private PlayerManager(){

    }
    public PlayerManager(PlayerView pView){
        super();
        this.playerView = pView;
        this.context = pView.getContext();
        createPlayers();
    }

    private void releasePlayers() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void createPlayers() {
        if (player != null) {
            player.release();
        }
        player = createPlayer();
        playerView.setPlayer(player);
    }

    private SimpleExoPlayer createPlayer() {
        SimpleExoPlayer player = new SimpleExoPlayer.Builder(context).build();
        return player;
    }

    /**
     * 设置播放路径
     * @param path
     */
    public void setPlayerUri(String path){
        if(player != null){
            Uri uri = Uri.parse(path);
            MediaSource mediaSource = getMediaSource(uri);
            player.prepare(mediaSource);
        }
    }

    /**
     * 设置播放路径
     * @param videoList 播放列表
     */
    public void setPlayerUri(List<String> videoList){
        setPlayerUri(videoList,0);
    }

    public void setPlayerUri(List<String> videoList,int windowIndex){
        if(player != null){
            List<MediaSource> playlist = new ArrayList<>();
            for(String url :videoList){
                Uri uri = Uri.parse(url);
                MediaSource mediaSource = getMediaSource(uri);
                playlist.add(mediaSource);
            }
            if(windowIndex < 0 || windowIndex >= playlist.size()){
                windowIndex =0;
            }
            ConcatenatingMediaSource concatenatedSource = new ConcatenatingMediaSource(
                    playlist.toArray(new MediaSource[playlist.size()]));
            player.prepare(concatenatedSource);
            player.seekTo(windowIndex, C.TIME_UNSET);
        }
    }

    public Player getPlayer(){
        if(player == null){
            createPlayers();
        }
        return player;
    }

    public void setRepeatMode(int mode){
        if(player != null){
            player.setRepeatMode(mode);
        }
    }

    public void setControllerShowTimeoutMs(int timeoutMs){
        if(playerView != null){
            //0-是常显示
            playerView.setControllerShowTimeoutMs(timeoutMs);
        }
    }

    public void paly(){
        if(player != null){
            player.setPlayWhenReady(true);
        }
    }

    public void onDestroy(){
        releasePlayers();
    }

    public MediaSource getMediaSource(Uri uri) {
        int streamType  = Util.inferContentType(uri.getLastPathSegment());
        switch (streamType) {
            case C.TYPE_SS:
                return new SsMediaSource.Factory(getDataSourceFactory(true)).createMediaSource(uri);
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(getDataSourceFactory(true)).createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(getDataSourceFactory(true)).createMediaSource(uri);
            case C.TYPE_OTHER:
            default: {
                return new ProgressiveMediaSource.Factory(getDataSourceFactory(true)).createMediaSource(uri);
            }
        }
    }

    private DataSource.Factory getDataSourceFactory(boolean preview) {
        return new DefaultDataSourceFactory(context,getHttpDataSourceFactory(preview));
    }

    private DataSource.Factory getHttpDataSourceFactory(boolean preview) {
        return new DefaultHttpDataSourceFactory(Util.getUserAgent(context,
                "exo-aillience"), DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,preview);
    }
}

