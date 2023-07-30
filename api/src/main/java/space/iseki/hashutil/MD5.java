package space.iseki.hashutil;

import kotlinx.serialization.Serializable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Serializable(with = MD5Serializer.class)
public final class MD5 implements Hash {
    private final int i0;
    private final int i1;
    private final int i2;
    private final int i3;

    @SuppressWarnings("PointlessArithmeticExpression")
    public MD5(@NotNull byte[] arr, int off) {
        this.i0 = Util.getInt(arr, off + 0 * 4);
        this.i1 = Util.getInt(arr, off + 1 * 4);
        this.i2 = Util.getInt(arr, off + 2 * 4);
        this.i3 = Util.getInt(arr, off + 3 * 4);
    }

    public MD5(@NotNull byte[] arr) {
        this(arr, 0);
    }

    public MD5(String hex) {
        this(Util.decodeHex(hex));
    }

    @Override
    @SuppressWarnings("PointlessArithmeticExpression")
    public @NotNull byte[] bytes(@NotNull byte[] arr, int off) {
        Util.putInt(arr, off + 0 * 4, i0);
        Util.putInt(arr, off + 1 * 4, i1);
        Util.putInt(arr, off + 2 * 4, i2);
        Util.putInt(arr, off + 3 * 4, i3);
        return arr;
    }

    @Override
    public @NotNull byte[] bytes() {
        return bytes(new byte[16], 0);
    }

    @Override
    public @NotNull String toString() {
        return Util.encodeHex(bytes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MD5 t = (MD5) o;
        return i0 == t.i0 && i1 == t.i1 && i2 == t.i2 && i3 == t.i3;
    }

    @Override
    public @NotNull Algorithms kind() {
        return Algorithms.MD5;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i0, i1, i2, i3);
    }
}
