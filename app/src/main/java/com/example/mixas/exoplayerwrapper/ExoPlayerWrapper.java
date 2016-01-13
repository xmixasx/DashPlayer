package com.example.mixas.exoplayerwrapper;

import android.content.Context;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;

import com.example.mixas.exoplayerwrapper.Player.*;
import com.google.android.exoplayer.util.DebugTextViewHelper;
import com.google.android.exoplayer.util.Util;

/**
 * Created by mixas on 12.01.16.
 */
public class ExoPlayerWrapper implements SurfaceHolder.Callback{

    private DemoPlayer player;
    boolean playerNeedsPrepare;
    private MediaController mediaController;
    private Uri contentUri;
    private Context mContext;
    private long playerPosition;
    private SurfaceView mView;

    public ExoPlayerWrapper(Context context, SurfaceView view){
        mContext = context;
        mView = view;
        mediaController = new KeyCompatibleMediaController(mContext);
        mediaController.setAnchorView(view.getRootView().getRootView());

    }

    public void preparePlayer(boolean playWhenReady) {
        if (player == null) {
            player = new DemoPlayer(getRendererBuilder());
            player.seekTo(playerPosition);
            playerNeedsPrepare = true;
            mediaController.setMediaPlayer(player.getPlayerControl());
            mediaController.setEnabled(true);

        }
        if (playerNeedsPrepare) {
            player.prepare();
            playerNeedsPrepare = false;
        }
        player.setSurface(mView.getHolder().getSurface());
        player.setPlayWhenReady(playWhenReady);
    }

    public void releasePlayer() {
        if (player != null) {
            playerPosition = player.getCurrentPosition();
            player.release();
            player = null;
        }
    }

    public void setListener(DemoPlayer.Listener listener){
        player.addListener(listener);
    }

    public void setCaptionListener(DemoPlayer.CaptionListener listener){
        player.setCaptionListener(listener);
    }

    public MediaController getMediaController() {
        return mediaController;
    }

    public void setMetadataListener(DemoPlayer.Id3MetadataListener listener){
        player.setMetadataListener(listener);
    }

    private DemoPlayer.RendererBuilder getRendererBuilder() {
        String userAgent = Util.getUserAgent(mContext, "ExoPlayerDemo");
        return new DashRendererBuilder(mContext, userAgent, contentUri.toString(),
                new SimpleMediaDrmCallback(contentUri.toString()));

    }


    public void setContentUri(Uri contentUri) {
        this.contentUri = contentUri;
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
