package space.iseki.hashutil;

import kotlinx.serialization.Serializable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
* Type for ${typename}
*/
@Serializable(with = ${typename}Serializer.class)
public final class ${typename} implements Hash {
<#list 0..<size as i>
    private final int i${i};
</#list>

    /**
    * Create ${typename} from byte array, big-endian
    * @param arr bytes
    * @param off offset
    */
    @SuppressWarnings("PointlessArithmeticExpression")
    public ${typename}(@NotNull byte[] arr, int off) {
<#list 0..<size as i>
    this.i${i} = Util.getInt(arr, off + ${i} * 4);
</#list>
    }

    /**
    * Create ${typename} from byte array, big-endian
    * @param arr bytes
    */
    public ${typename}(@NotNull byte[] arr) {
        this(arr, 0);
    }

    /**
    * Create ${typename} from hex string
    * @param hex hex string
    */
    public ${typename}(String hex) {
        this(Util.decodeHex(hex));
    }

    @Override
    @SuppressWarnings("PointlessArithmeticExpression")
    public @NotNull byte[] bytes(@NotNull byte[] arr, int off) {
<#list 0..<size as i>
    Util.putInt(arr, off + ${i} * 4, i${i});
</#list>
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
        ${typename} t = (${typename}) o;
        return <#list 0..<size as i>i${i} == t.i${i}<#sep> && </#list>;
    }

    @Override
    public @NotNull Algorithms kind() {
        return Algorithms.${typename};
    }

    @Override
    public int hashCode() {
        return Objects.hash(<#list 0..<size as i>i${i}<#sep>, </#list>);
    }
}
