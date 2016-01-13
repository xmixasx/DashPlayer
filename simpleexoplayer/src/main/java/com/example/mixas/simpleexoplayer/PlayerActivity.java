package com.example.mixas.simpleexoplayer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;

import com.example.mixas.exoplayerwrapper.ExoPlayerWrapper;
import com.example.mixas.exoplayerwrapper.Player.DemoPlayer;
import com.google.android.exoplayer.AspectRatioFrameLayout;

public class PlayerActivity extends AppCompatActivity implements DemoPlayer.Listener{
    ExoPlayerWrapper player;
    SurfaceView playerView;
    AspectRatioFrameLayout videoFrame;


    @Override
    protected void onPause() {
        super.onPause();
        if (player != null){
            player.releasePlayer();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();
        String videoUri = null;
        if (intent != null) {
            videoUri = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        playerView  = (SurfaceView) findViewById(R.id.surface_view);
        player = new ExoPlayerWrapper(this, playerView);
        player.setContentUri(Uri.parse(videoUri));
        player.preparePlayer(true);
        videoFrame = (AspectRatioFrameLayout) findViewById(R.id.video_frame);
        View root = findViewById(R.id.root);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    toggleControlsVisibility();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.performClick();
                }
                return true;
            }
        });
    }

    private void toggleControlsVisibility()  {
        if (player.getMediaController().isShowing()) {
            player.getMediaController().hide();

        } else {
            showControls();
        }
    }

    private void showControls() {
        player.getMediaController().show(0);
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
}
