package wuxian.me.andbootstrap.video;

/**
 * Created by wuxian on 16/3/2017.
 */

public interface IVolumListener {

    //根据percent来设置
    void updateVolum(float percent);

    //根据移动距离来设置
    void updateVolumBy(int distanceX, int distanceY);

}
