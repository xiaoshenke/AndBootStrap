package wuxian.me.andbootstrapdemo.utils.talk;

/**
 * Created by wuxian on 12/4/2017.
 * <p>
 * 身份其实分成两类 一类是server 一类是client
 * 但是一旦连接上以后就没有server和client的区别 --> Fixme:暂时先这么写
 */

public interface ITalkieClient {
    void destroy();

    void startTalking();

    void stopTalking();

    boolean connect(String serverIp, int serverPort);

    boolean bind(int port);

    boolean isBinded();

    boolean isConnected();

    void setClientConnectedListener(IClientConnected listener);

    interface IClientConnected {
        void onClientConnected(String ip, int port);
    }
}
