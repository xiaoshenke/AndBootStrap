package wuxian.me.andbootstrap.video;

/**
 * Created by wuxian on 16/3/2017.
 */

public interface IMediaPlayer {
    void start();

    void pause();

    int getDuration();

    int getCurrentPosition() throws IllegalStateException;

    void seekTo(int pos);

    boolean isPlaying();

    int getBufferPercentage();

    boolean canPause();

    boolean canSeekBackward();

    boolean canSeekForward();

    boolean isFullScreen();

    void toggleFullScreen();
}
