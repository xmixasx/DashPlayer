package com.example.mixas.simpleexoplayer;

import android.content.Context;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.mixas.exoplayerwrapper.Player.DashRendererBuilder;
import com.example.mixas.exoplayerwrapper.Player.Player;
import com.example.mixas.exoplayerwrapper.SimpleMediaDrmCallback;
import com.google.android.exoplayer.util.Util;

/**
 * Created by mixas on 13.01.16.
 */
public class DASHPlayer {

    private static final String EXT_DASH = ".mpd";

    private Player player;
    private boolean playerNeedsPrepare = true;
    private MediaController mediaController;
    private Uri contentUri;
    private Context mContext;
    private long playerPosition;
    private SurfaceView mView;

    public DASHPlayer(Context context, SurfaceView view){
        mContext = context;
        mView = view;
        mediaController = new KeyCompatibleMediaController(mContext);
        mediaController.setAnchorView(mView);


    }


    private Player.RendererBuilder getRendererBuilder() {
        String userAgent = Util.getUserAgent(mContext, "DashPlayer");
        return new DashRendererBuilder(mContext, userAgent, contentUri.toString(),
                new SimpleMediaDrmCallback(contentUri.toString()));

    }




    public void play(long currentPos) {
        if (player == null) {
            player = new Player(getRendererBuilder());
            player.seekTo(currentPos);
            playerNeedsPrepare = true;
            mediaController.setMediaPlayer(player.getPlayerControl());
            mediaController.setEnabled(true);

        }
        if (playerNeedsPrepare) {
            player.prepare();
            playerNeedsPrepare = false;
        }
        player.setSurface(mView.getHolder().getSurface());
        player.setPlayWhenReady(true);
    }


    public long stop() {
        if (player != null) {
            playerPosition = player.getCurrentPosition();
            player.release();
            player = null;
            return playerPosition;
        }

        return 0;
    }

    public void pause() {
        if (player != null && player.getPlayerControl().canPause()) {
            player.getPlayerControl().pause();
        }

    }

    public void setSurface(Surface v) {
        player.setSurface(v);

    }

    public void setContent(Uri url) {
        String lastPathSegment = url.getLastPathSegment();
        if (lastPathSegment.endsWith(EXT_DASH)) {
            contentUri = url;
        }
        else  {
            Toast.makeText(mContext, "Url isn't a path to DASH", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isAdaptive() {
        return false;
    }

    public void setAdaptive() {

    }

    public void toggleMediaController() {
        if (mediaController.isShowing()) {
            mediaController.hide();

        } else {
            mediaController.show(0);
        }

    }

    public void blockingClearSurface() {
        player.blockingClearSurface();
    }

    public void addListener(Player.Listener listener) {
        player.addListener(listener);
    }

    private static final class KeyCompatibleMediaController extends MediaController {

        private MediaController.MediaPlayerControl playerControl;

        public KeyCompatibleMediaController(Context context) {
            super(context);
        }

        @Override
        public void setMediaPlayer(MediaController.MediaPlayerControl playerControl) {
            super.setMediaPlayer(playerControl);
            this.playerControl = playerControl;
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            int keyCode = event.getKeyCode();
            if (playerControl.canSeekForward() && keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    playerControl.seekTo(playerControl.getCurrentPosition() + 15000); // milliseconds
                    show();
                }
                return true;
            } else if (playerControl.canSeekBackward() && keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    playerControl.seekTo(playerControl.getCurrentPosition() - 5000); // milliseconds
                    show();
                }
                return true;
            }
            return super.dispatchKeyEvent(event);
        }
    }

}
