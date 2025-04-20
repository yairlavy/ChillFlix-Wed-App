package com.example.androidchillflix.main.activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import com.example.androidchillflix.R;

public class VideoPlayerActivity extends AppCompatActivity {
    private PlayerView playerView;
    private ExoPlayer player;
    private ProgressBar progressBar;
    private ImageButton btnPlayPause;
    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        playerView = findViewById(R.id.exo_player_view);
        progressBar = findViewById(R.id.exo_player_progress_bar);

        btnPlayPause = findViewById(R.id.btn_play_pause);
        ImageButton btnRewind = findViewById(R.id.btn_rewind);
        ImageButton btnForward = findViewById(R.id.btn_forward);
        ImageButton btnForward10 = findViewById(R.id.btn_forward_10);

        // Get videoUrl from intent extras if available
        videoUrl = getIntent().getStringExtra("videoUrl");
        if (videoUrl == null || videoUrl.isEmpty()) {
            // If no video URL is provided, use the default video.
            videoUrl = "android.resource://" + getPackageName() + "/" + R.raw.video;
        }

        initPlayer();

        btnPlayPause.setOnClickListener(v -> togglePlayPause());
        btnRewind.setOnClickListener(v -> rewindVideo());
        btnForward.setOnClickListener(v -> forwardVideo());
        btnForward10.setOnClickListener(v -> fastForward10());
    }

    private void initPlayer() {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
        player.setMediaItem(mediaItem);

        player.prepare();
        player.play();

        player.addListener(new ExoPlayer.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == ExoPlayer.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void togglePlayPause() {
        if (player.isPlaying()) {
            player.pause();
            btnPlayPause.setImageResource(R.drawable.play_arrow);
        } else {
            player.play();
            // Replace with your pause icon if available
            btnPlayPause.setImageResource(R.drawable.play_arrow);
        }
    }

    private void rewindVideo() {
        long currentPosition = player.getCurrentPosition();
        player.seekTo(Math.max(currentPosition - 10000, 0));
    }

    private void forwardVideo() {
        long currentPosition = player.getCurrentPosition();
        long duration = player.getDuration();
        player.seekTo(Math.min(currentPosition + 10000, duration));
    }

    private void fastForward10() {
        long currentPosition = player.getCurrentPosition();
        long duration = player.getDuration();
        player.seekTo(Math.min(currentPosition + 10000, duration));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
