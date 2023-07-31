package space.iseki.hashutil;

import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
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

