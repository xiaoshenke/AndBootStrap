package com.adnroid.ufirephone.home;

public class AudioCodec {
    static {
        System.loadLibrary("audiowrapper");

    }
    // initialize decoder and encoder
    public static native int audio_codec_init(int mode);

    // encode
    public static native int audio_encode(byte[] origin, int originOffset,
                                          int originLength, byte[] encoded, int encodedOffset);

    // decode
    public static native int audio_decode(byte[] origin, int originOffset,
                                          int originLength, byte[] decoded, int decodedLength);

}
