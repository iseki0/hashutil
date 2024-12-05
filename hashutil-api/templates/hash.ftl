package space.iseki.hashutil;

import kotlinx.serialization.Serializable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Type for ${typename}
 */
@Serializable(with = ${typename}Serializer.class)
public final class ${typename} implements Comparable<${typename}> {
    private static final ThreadLocal<MessageDigest> threadLocal = SharedUtil.getThreadLocal("${typename}");
    public static final @NotNull ${typename} ZERO = new ${typename}(<#list 0..<size as i>0<#sep>, </#list>);
    public static final int SIZE_IN_BYTES = ${size} * 4;

    private static @NotNull MessageDigest newDigest(){
        return SharedUtil.messageDigest("${typename}");
    }

<#list 0..<size as i>
    private final int i${i};
</#list>

    ${typename}(<#list 0..<size as i>int i${i}<#sep>, </#list>){
<#list 0..<size as i>
        this.i${i} = i${i};
</#list>
    }

    /**
     * Create ${typename} from a byte array, big-endian
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
     * Create ${typename} from a byte array, big-endian
     * @param arr bytes
     */
    public ${typename}(byte @NotNull [] arr) {
        this(checkHashBytes(arr), 0);
    }

    /**
     * Create ${typename} from hex string
     * @param hex hex string
     */
    public ${typename}(@NotNull String hex) {
        this(HexFormat.of().parseHex(checkHashHex(hex)));
    }

    private static String checkHashHex(String s) {
        if (s.length() != SIZE_IN_BYTES * 2) {
            throw new IllegalArgumentException("Expected a " + SIZE_IN_BYTES * 2 + "-character hexadecimal ${typename} hash, but got " + s.length() + " characters.");
        }
        return s;
    }

    private static byte[] checkHashBytes(byte[] b){
        if (b.length != SIZE_IN_BYTES) {
            throw new IllegalArgumentException("Expected a " + SIZE_IN_BYTES + "-byte ${typename} hash, but got " + b.length + " bytes.");
        }
        return b;
    }

    static @NotNull MessageDigest getThreadLocalDigest() {
        return threadLocal != null ? threadLocal.get() : newDigest();
    }

    /**
    * Create a new {@link MessageDigest} with the algorithm "${typename}".
    *
    * @throws RuntimeException wraps a {@link NoSuchAlgorithmException}, if the algorithm is not available
    * @return the message digest
    * @see MessageDigest#getInstance(String)
    */
    public static @NotNull MessageDigest getMessageDigest() {
        return newDigest();
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
        int r = i0;
<#list 1..<size as i>
        r = 31 * r + i${i};
</#list>
        return r;
    }


    /**
     * Create hash of the input stream.
     * @param inputStream the input stream
     * @return the hash
     * @throws IOException for underlying {@link IOException}
     * @throws RuntimeException wraps a {@link NoSuchAlgorithmException}, if the algorithm is not available
     */
    public static @NotNull ${typename} of(@NotNull InputStream inputStream) throws IOException {
        return SharedUtil.forInputStream(getThreadLocalDigest(), inputStream, ${typename}::new);
    }

    /**
     * Create hash of the channel.
     * @param channel the channel
     * @return the hash
     * @throws IOException for underlying {@link IOException}
     * @throws RuntimeException wraps a {@link NoSuchAlgorithmException}, if the algorithm is not available
     */
    public static @NotNull ${typename} of(@NotNull ReadableByteChannel channel) throws IOException {
        return SharedUtil.forReadableChannel(getThreadLocalDigest(), channel, ${typename}::new);
    }

    /**
     * Create hash of the file.
     * @param path path of the file
     * @return the hash
     * @throws IOException for underlying {@link IOException}
     * @throws RuntimeException wraps a {@link NoSuchAlgorithmException}, if the algorithm is not available
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
     * @throws RuntimeException wraps a {@link NoSuchAlgorithmException}, if the algorithm is not available
     */
    public static @NotNull ${typename} of(byte @NotNull [] data, int off, int len) {
        return SharedUtil.forBytes(getThreadLocalDigest(), data, off, len, ${typename}::new);
    }

    /**
     * Create hash of the byte array.
     * @param data the byte array
     * @return the hash
     * @throws RuntimeException wraps a {@link NoSuchAlgorithmException}, if the algorithm is not available
     */
    public static @NotNull ${typename} of(byte@NotNull [] data){
        return of(data, 0, data.length);
    }

    /**
     * Create an interception {@link InputStream}.
     * @param inputStream the input stream
     * @return the intercepted input stream
     * @throws RuntimeException wraps a {@link NoSuchAlgorithmException}, if the algorithm is not available
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

