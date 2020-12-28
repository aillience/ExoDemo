package com.huoli.bbl.exodemo;

import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.RepeatModeUtil;
import com.huoli.bbl.exodemo.exoplayer.PlayerManager;

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
 * #更新内容 播放以小弹窗形式展示，使用activity是为了更好的管理播放器资源，便于释放
 * ===============================
 **/
public class DialogActivity extends AppCompatActivity {

    private PlayerView playerView;
    private PlayerManager playerManager;

    private List<String> videoList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        playerView = findViewById(R.id.player_view);

        initializePlayer();
    }

    private void initializePlayer() {

        playerManager = new PlayerManager(playerView);
        playerManager.setRepeatMode(RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL);
        playerManager.setControllerShowTimeoutMs(0);
        playerManager.paly();

        //        videoList.add("https://qiniu.bangbangli.com/5GGZPVYQWLXNQZAFGY.mp4");
//                videoList.add("https://qiniu.bangbangli.com/WOKHDT2R7LL1OV9O3Q.mp4");
//        videoList.add("http://5.595818.com/2015/ring/000/140/6731c71dfb5c4c09a80901b65528168b.mp3");
        videoList.add("https://lijuanqiniu.mnnmedu.com/20190404145745d - 大调卡农.mp3");
        playerManager.setPlayerUri(videoList);
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
