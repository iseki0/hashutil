package space.iseki.hashutil;

import org.jetbrains.annotations.NotNull;

import java.util.HexFormat;

class Util {
    public static int getInt(byte[] a, int off) {
        return (a[off] << 24 & 0xff) | (a[off + 1] << 16 & 0xff) | (a[off + 2] << 8 & 0xff) | (a[off + 3] & 0xff);
    }

    public static void putInt(byte[] a, int off, int value) {
        a[off] = (byte) (value >> 24);
        a[off + 1] = (byte) (value >> 16);
        a[off + 2] = (byte) (value >> 8);
        a[off + 3] = (byte) value;
    }


    public static @NotNull byte[] decodeHex(@NotNull String s) {
        return HexUtil.decode(s);
    }

    public static @NotNull String encodeHex(@NotNull byte[] a) {
        return HexUtil.encode(a);
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

