package space.iseki.hashutil;

import kotlinx.serialization.Serializable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Objects;

/**
 * Type for ${typename}
 */
@Serializable(with = ${typename}Serializer.class)
public final class ${typename} implements Comparable<${typename}> {
    private static final ThreadLocal<MessageDigest> threadLocal = ThreadLocal.withInitial(() -> SharedUtil.messageDigest("${typename}"));
    public static final @NotNull ${typename} ZERO = new ${typename}(new byte[${size} * 4]);

    private static @NotNull MessageDigest newDigest(){
        return SharedUtil.messageDigest("${typename}");
    }

<#list 0..<size as i>
    private final int i${i};
</#list>

    /**
     * Create ${typename} from byte array, big-endian
     * @param arr bytes
     * @param off offset
     */
    @SuppressWarnings("PointlessArithmeticExpression")
    public ${typename}(byte @NotNull [] arr, int off) {
<#list 0..<size as i>
        this.i${i} = (int) SharedUtil.AVH.get(arr, off + ${i} * 4);
</#list>
    }

    /**
     * Create ${typename} from byte array, big-endian
     * @param arr bytes
     */
    public ${typename}(byte @NotNull [] arr) {
        this(arr, 0);
    }

    /**
     * Create ${typename} from hex string
     * @param hex hex string
     */
    public ${typename}(@NotNull String hex) {
        this(HexFormat.of().parseHex(hex));
    }

    static @NotNull MessageDigest getThreadLocalDigest() {
        return threadLocal.get();
    }


    @SuppressWarnings("PointlessArithmeticExpression")
    public byte @NotNull [] bytes(byte @NotNull [] arr, int off) {
<#list 0..<size as i>
        SharedUtil.AVH.set(arr, off + ${i} * 4, i${i});
</#list>
        return arr;
    }

    public byte @NotNull [] bytes() {
        return bytes(new byte[${size} * 4], 0);
    }

    @Override
    public @NotNull String toString() {
        return HexFormat.of().formatHex(bytes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ${typename} t = (${typename}) o;
        return <#list 0..<size as i>i${i} == t.i${i}<#sep> && </#list>;
    }

    @Override
    public int hashCode() {
        return Objects.hash(<#list 0..<size as i>i${i}<#sep>, </#list>);
    }


    /**
     * Create hash of the input stream.
     * @param inputStream the input stream
     * @return the hash
     * @throws IOException for underlying {@link IOException}
     */
    public static @NotNull ${typename} of(@NotNull InputStream inputStream) throws IOException {
        return SharedUtil.forInputStream(getThreadLocalDigest(), inputStream, ${typename}::new);
    }

    /**
     * Create hash of the channel.
     * @param channel the channel
     * @return the hash
     * @throws IOException for underlying {@link IOException}
     */
    public static @NotNull ${typename} of(@NotNull ReadableByteChannel channel) throws IOException {
        return SharedUtil.forReadableChannel(getThreadLocalDigest(), channel, ${typename}::new);
    }

    /**
     * Create hash of the file.
     * @param path path of the file
     * @return the hash
     * @throws IOException for underlying {@link IOException}
     */
    public static @NotNull ${typename} of(@NotNull Path path) throws IOException {
        return SharedUtil.forPath(getThreadLocalDigest(), path, ${typename}::new);
    }

    /**
     * Create hash of the byte array.
     * @param data the byte array
     * @param off the offset
     * @param len the length
     * @return the hash
     */
    public static @NotNull ${typename} of(byte @NotNull [] data, int off, int len) {
        return SharedUtil.forBytes(getThreadLocalDigest(), data, off, len, ${typename}::new);
    }

    /**
     * Create hash of the byte array.
     * @param data the byte array
     * @return the hash
     */
    public static @NotNull ${typename} of(byte@NotNull [] data){
        return of(data, 0, data.length);
    }

    /**
     * Create an interception {@link InputStream}.
     * @param inputStream the input stream
     * @return the intercepted input stream
     */
    public static @NotNull MessageDigestInterceptedInputStream<${typename}> intercept(@NotNull InputStream inputStream) {
        return SharedUtil.forInterceptedInputStream(newDigest(), inputStream, ${typename}::new);
    }

    @Override
    public int compareTo(@NotNull ${typename} o) {
        int r;
<#list 0..<size as i>
        r = Integer.compareUnsigned(i${i}, o.i${i});
        if (r != 0) {
            return r;
        }
</#list>
        return 0;
    }
}

