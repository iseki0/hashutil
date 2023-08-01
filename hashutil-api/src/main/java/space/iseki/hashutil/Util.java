package space.iseki.hashutil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.util.HexFormat;

class Util {
    private static final VarHandle INT_HANDLE = MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.BIG_ENDIAN);

    public static int getInt(byte[] a, int off) {
        return (int) INT_HANDLE.get(a, off);
    }

    public static void putInt(byte[] a, int off, int value) {
        INT_HANDLE.set(a, off, value);
    }


    public static @NotNull byte[] decodeHex(@NotNull String s) {
        return HexUtil.decode(s);
    }

    public static @NotNull String encodeHex(@NotNull byte[] a) {
        return HexUtil.encode(a);
    }

    static final int DEFAULT_BUFFER_SIZE = 16 * 1024;

    static @NotNull byte[] hashStream(@NotNull MessageDigest messageDigest, @NotNull InputStream inputStream, int bufferSize) throws IOException {
        var buf = new byte[bufferSize];
        while (true) {
            var i = inputStream.read(buf);
            if (i == -1) break;
            messageDigest.update(buf, 0, i);
        }
        return messageDigest.digest();
    }
}

class HexUtil {
    private static final HexFormat hf = HexFormat.of();

    public static @NotNull byte[] decode(@NotNull String s) {
        return hf.parseHex(s);
    }

    public static @NotNull String encode(@NotNull byte[] a) {
        return hf.formatHex(a);
    }
}

