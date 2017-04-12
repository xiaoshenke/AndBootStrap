package wuxian.me.andbootstrapdemo.talk;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import com.adnroid.ufirephone.home.AudioCodec;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by wuxian on 11/4/2017.
 */

public class TalkieClient {
    private static final String TAG = "TalkieClient";
    private String serverIp;
    private int serverPort;

    private Context context;
    private boolean isServer;

    public TalkieClient(Context context, boolean isServer) {

        this.isServer = isServer;
        this.context = context;

        init();
    }

    public void destroy() {
        if (speeker != null) {
            speeker.interrupt();
        }
        if (listener != null) {
            listener.interrupt();
        }

        if(null != recorder){
            recorder.release();
            recorder = null;
        }
    }

    public void startTalking() {
        if (!isConnected) {
            Log.e(TAG,"startTalking,but not connected");
            return;
        }
        Log.e(TAG,"startTalking");
        speeker = new Speeker(socketRec, recorder);
        speeker.start();
    }

    public void stopTalking() {
        if (!isConnected || speeker == null) {

            return;
        }
        Log.e(TAG,"stopTalking");
        speeker.interrupt();
    }

    //绑定server ip,port
    public boolean connect(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;

        try {
            socketRec = new DatagramSocket();

        } catch (SocketException e) {
            if (socketRec != null) {
                socketRec.close();
            }
            socketRec = null;
            return false;
        }

        isConnected = true;
        listen();
        return true;
    }

    private boolean binded = false;

    public boolean isBinded() {
        if (isServer) {
            return binded;
        } else {
            return true;  //如果是客户端 总是返回true
        }
    }

    //如果是server 绑定自身端口
    public boolean bind(int port) {
        if (binded) {
            return true;
        }
        try {
            socketRec = new DatagramSocket(port);
            binded = true;
            listen();
            return true;
        } catch (SocketException e) {
            if (socketRec != null) {
                socketRec.close();
            }
            socketRec = null;
            return false;
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    private Speeker speeker;
    private Listener listener;

    private void listen() {
        listener = new Listener(socketRec, player);
        listener.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e(TAG, "throwable ex: " + ex.toString());
            }
        });
        listener.start();
    }

    DatagramSocket socketRec;
    AudioRecord recorder;
    AudioTrack player;
    AudioManager mAudioManager;

    private int m_bs = 0;
    private static final int SAMPLE_RATE_IN_HZ = 8000;
    private AudioRecord findAudioRecord() {
        m_bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        if ((m_bs % 320) != 0) {
            m_bs += (320 - m_bs % 320);
        }

        return new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, m_bs);
    }

    boolean isConnected = false;

    private void init() {
        AudioCodec.audio_codec_init(20);
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        recorder = findAudioRecord();

        int sbufferSize = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        player = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, sbufferSize * 10, AudioTrack.MODE_STREAM);
        player.setStereoVolume(1.0f, 1.0f);

    }

    InetAddress inetAddress = null;

    private class Listener extends Thread {
        DatagramSocket socket;
        AudioTrack player;

        public Listener(DatagramSocket socket, AudioTrack player) {
            this.socket = socket;
            this.player = player;

            setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    Log.e(TAG,"ListenerThread e: "+e.getMessage());
                }
            });
        }

        int getlength = 0;

        private int decodeSize = 0;
        private byte[] decodeBuf = new byte[16960];
        private byte[] recvBuffer = new byte[MAX_BYTE_NUM_ENCODE];
        byte[] revbuf = new byte[2048];

        public int receive(byte[] buf, int reqlength) {
            try {
                Arrays.fill(recvBuffer, (byte) 0);
                DatagramPacket rdp = new DatagramPacket(recvBuffer, recvBuffer.length);
                socketRec.receive(rdp);

                if (isServer && !isConnected) {
                    serverIp = rdp.getAddress().toString();
                    inetAddress = rdp.getAddress();
                    serverPort = rdp.getPort();
                    Log.e(TAG,"SERVER get connected from client,ip: "+serverIp+" port: "+serverPort);
                    if (userConnectedListener != null) {
                        userConnectedListener.onUserConnected(serverIp, serverPort);
                    }
                    isConnected = true;
                }

                int getlength = 0;
                if (reqlength <= rdp.getLength()) {
                    getlength = reqlength;
                    System.arraycopy(recvBuffer, 0, buf, 0, getlength);
                } else {
                    getlength = rdp.getLength();
                    System.arraycopy(recvBuffer, 0, buf, 0, getlength);
                }

                return getlength;
            } catch (Exception e) {
                //SocketExceptionRec(); //Fixme
                e.printStackTrace();
                return -1;
            }
        }

        public void run() {
            player.play();

            while (!this.isInterrupted()) {
                try {
                    getlength = receive(revbuf, revbuf.length);

                    Log.e(TAG,"voice received[encoded]: "+getlength);
                    if (getlength > 0) {
                        ByteBuffer byteBuffer = decodeAfterReceive(revbuf, getlength);
                        if (byteBuffer == null) {
                            continue;
                        }
                        decodeSize = AudioCodec.audio_decode(byteBuffer.array(), 0, byteBuffer.remaining(), decodeBuf, 0);
                        Log.e(TAG,"voice write to AudioTrack[decoded]: "+decodeSize);
                        player.write(decodeBuf, 0, decodeSize);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                player.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class Speeker extends Thread {
        DatagramSocket socket;
        AudioRecord recorder;

        byte[] audio = new byte[m_bs];
        int getlength = 0;
        byte[] encodeBuf = new byte[MAX_BYTE_NUM_ENCODE];
        int encodeSize = 0;

        public Speeker(DatagramSocket socket, AudioRecord recorder) {
            this.socket = socket;
            this.recorder = recorder;

            setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    Log.e(TAG,"SpeekerThread e: "+e.getMessage());
                }
            });
        }

        public void run() {
            recorder.startRecording();
            if(!isServer){
                try{
                    socketRec.connect(InetAddress.getByName(serverIp),serverPort);
                } catch (UnknownHostException e){
                    Log.e(TAG,"SpeekerThread connect fail, due to UnkownHost");
                    return;
                }
            }

            while (!this.isInterrupted()) {
                try {
                    getlength = recorder.read(audio, 0, m_bs);
                    Log.e(TAG,"voice readed from AudioPlayer: "+getlength);
                    encodeSize = AudioCodec.audio_encode(audio, 0, getlength, encodeBuf, 0);

                    if (getlength >= 0) {
                        ByteBuffer byteBuffer = wrapBeforeSend(encodeBuf, encodeSize);
                        Log.e(TAG,"voice send[encoded]: "+byteBuffer.remaining());
                        send(byteBuffer.array(), byteBuffer.remaining());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            recorder.stop();
        }

        private void send(byte[] buf, int length) {
            if(isServer && inetAddress == null){ //是server我必须知道对方的inetAddress
                return;
            }

            DatagramPacket packet;
            if (isServer) {
                packet = new DatagramPacket(buf, length, inetAddress, serverPort);
            } else {
                packet = new DatagramPacket(buf, length);
            }
            try {
                socket.send(packet);
            } catch (IOException e) {
                //socket = null;  //Fixme
            }
        }

    }

    private static final int MAX_BYTE_NUM = 640;
    private static final int MAX_BYTE_NUM_ENCODE = 2048 + 16;

    private ByteBuffer decodeAfterReceive(byte[] buf, int length) {
        return ByteBuffer.wrap(buf, 0, length);
    }

    private ByteBuffer wrapBeforeSend(byte[] buf, int length) {
        return ByteBuffer.wrap(buf, 0, length);
    }

    private IUserConnected userConnectedListener;

    public void setUserConnectedListener(final IUserConnected listener) {
        if(listener == null){
            return;
        }
        this.userConnectedListener = new IUserConnected() {
            @Override
            public void onUserConnected(final String ip, final int port) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onUserConnected(ip, port);
                    }
                });
            }
        };
    }

    interface IUserConnected {
        void onUserConnected(String ip, int port);
    }

    private Handler handler = new Handler();

}
