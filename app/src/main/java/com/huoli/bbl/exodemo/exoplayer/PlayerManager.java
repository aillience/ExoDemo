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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            Uri uri = Uri.parse(filterChinese(path));
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
                Uri uri = Uri.parse(filterChinese(url));
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

    /**
     * 判断字符串中是否包含中文
     * @param str
     * 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public  boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 过滤掉中文
     * @param str 待过滤中文的字符串
     * @return 过滤掉中文后字符串
     */
    public  String filterChinese(String str) {
        //先替换所有空格
        str = str.replaceAll(" ","%20");
        // 用于返回结果
        String result = str;
        boolean flag = isContainChinese(str);
        if (flag) {// 包含中文
            // 用于拼接过滤中文后的字符
            StringBuffer sb = new StringBuffer();
            // 用于校验是否为中文
            boolean flag2 = false;
            // 用于临时存储单字符
            char chinese = 0;
            // 5.去除掉文件名中的中文
            // 将字符串转换成char[]
            char[] charArray = str.toCharArray();
            // 过滤到中文及中文字符
            for (int i = 0; i < charArray.length; i++) {
                chinese = charArray[i];
                flag2 = isChinese(chinese);
                if (flag2) {
                    //是中日韩文字
                    sb.append(Uri.encode(String.valueOf(chinese)));
                }else {
                    sb.append(chinese);
                }
            }
            result = sb.toString();
        }
        return result;
    }

    /**
     * 判定输入的是否是汉字
     *
     * @param c
     *  被校验的字符
     * @return true代表是汉字
     */
    public  boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}

