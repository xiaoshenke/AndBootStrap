package wuxian.me.andbootstrap.video;

/**
 * Created by wuxian on 28/3/2017.
 */

public interface IPlayer {
    void pause();

    void start();

    int getDuration();

    int getBufferPercentage();

    boolean canPause();

    int getCurrentPosition();

    void seekTo(int position);

    boolean isPlaying();

    boolean isLandscape();
}
