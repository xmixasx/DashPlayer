package com.example.mixas.simpleexoplayer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.mixas.exoplayerwrapper.Player.Player;
import com.google.android.exoplayer.AspectRatioFrameLayout;

public class PlayerActivity extends AppCompatActivity implements Player.Listener, SurfaceHolder.Callback{
    private DASHPlayer player;
    SurfaceView playerView;
    AspectRatioFrameLayout videoFrame;
    private long currentPos;
    boolean playerNeedsprepare;


    @Override
    protected void onStop() {
        super.onStop();
        currentPos = player.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HandleIntent();
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentPos = player.stop();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        playerView  = (SurfaceView) findViewById(R.id.surface_view);

        videoFrame = (AspectRatioFrameLayout) findViewById(R.id.video_frame);
        View root = findViewById(R.id.root);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    player.toggleMediaController();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.performClick();
                }
                return true;
            }
        });
    }

    private void HandleIntent() {
        Intent intent = getIntent();
        String videoUri = null;
        player = new DASHPlayer(this, playerView);
        if (intent != null) {
            videoUri = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        player.setContent(Uri.parse(videoUri));
        player.play(currentPos);
        player.addListener(this);

    }
    @Override
    public void onNewIntent(Intent intent) {
        player.stop();
        currentPos = 0;
        setIntent(intent);
    }
    public void onDestroy() {
        super.onDestroy();
        currentPos = player.stop();
    }

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                                   float pixelWidthAspectRatio) {
        videoFrame.setAspectRatio(
                height == 0 ? 1 : (width * pixelWidthAspectRatio) / height);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (player != null) {
            player.setSurface(holder.getSurface());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Do nothing.
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (player != null) {
            player.blockingClearSurface();
        }
    }
}
