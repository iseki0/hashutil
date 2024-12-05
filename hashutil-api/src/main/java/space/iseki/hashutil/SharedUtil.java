package space.iseki.hashutil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.function.Function;

class SharedUtil {
    public static final @NotNull String BUFFER_PROPERTY_NAME = "space.iseki.hashutil.buffer.size";
    public static final @NotNull String USE_THREAD_LOCAL_PROPERTY_NAME = "space.iseki.hashutil.threadlocal";
    public static final int DEFAULT_BUFFER_SIZE = 8 * 1024;
    public static final int BUFFER_SIZE;
    public static final boolean USE_THREAD_LOCAL;
    private static final ThreadLocal<WeakReference<ByteBuffer>> bufferThreadLocal = new ThreadLocal<>();
    static VarHandle AVH = MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.BIG_ENDIAN).withInvokeExactBehavior();

    @FunctionalInterface
    interface FC<T>{
        T apply(byte[] arr, int off);
    }
    static {
        var p = System.getProperty(BUFFER_PROPERTY_NAME);
        if (p == null || p.isBlank()) {
            BUFFER_SIZE = DEFAULT_BUFFER_SIZE;
        } else {
            BUFFER_SIZE = Integer.parseInt(p);
        }
        USE_THREAD_LOCAL = Boolean.parseBoolean(System.getProperty(USE_THREAD_LOCAL_PROPERTY_NAME, "true"));
    }

    static @Nullable ThreadLocal<MessageDigest> getThreadLocal(String name) {
        return USE_THREAD_LOCAL ? ThreadLocal.withInitial(() -> SharedUtil.messageDigest(name)) : null;
    }

    static MessageDigest messageDigest(String name) {
        try {
            return MessageDigest.getInstance(name);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    static <T> T forInputStream(MessageDigest digest, InputStream inputStream, FC<T> call) throws IOException {
        var buf = getSharedBuffer().array();
        try {
            while (true) {
                var n = inputStream.read(buf);
                if (n == -1) break;
                digest.update(buf, 0, n);
            }
            return handleDigest(buf, digest, call);
        } catch (Throwable th) {
            digest.reset();
            throw th;
        } finally {
            Arrays.fill(buf, (byte) 0);
        }
    }

    static <T> T forReadableChannel(MessageDigest digest, ReadableByteChannel channel, FC<T> call) throws IOException {
        var buf = getSharedBuffer();
        var arr = buf.array();
        try {
            while (true) {
                var n = channel.read(buf);
                if (n == -1) break;
                buf.flip();
                digest.update(buf);
                buf.clear();
            }
            return handleDigest(arr, digest, call);
        } catch (Throwable th) {
            digest.reset();
            throw th;
        } finally {
            buf.clear();
            Arrays.fill(arr, (byte) 0);
        }
    }


    static <T> T forPath(MessageDigest digest, Path path, FC<T> call) throws IOException {
        var ch = Files.newByteChannel(path, StandardOpenOption.READ);
        IOException th = null;
        try {
            return forReadableChannel(digest, ch, call);
        } catch (IOException e) {
            th = e;
            throw th;
        } finally {
            try {
                ch.close();
            } catch (IOException e) {
                if (th == null) {
                    throw e;
                } else {
                    th.addSuppressed(e);
                }
            }
        }
    }

    static <T> T forBytes(MessageDigest digest, byte[] data, int off, int len, FC<T> call) {
        try {
            digest.update(data, off, len);
            return call.apply(digest.digest(), 0);
        } catch (Throwable throwable) {
            digest.reset();
            throw throwable;
        }
    }

    private static <T> T handleDigest(byte[] arr, MessageDigest digest, FC<T> call) {
        try {
            digest.digest(arr, 0, digest.getDigestLength());
        } catch (DigestException e) {
            throw new RuntimeException(e);
        }
        return call.apply(arr, 0);
    }

    static <T> MessageDigestInterceptedInputStream<T> forInterceptedInputStream(MessageDigest digest, InputStream inputStream, FC<T> call) {
        return new MessageDigestInterceptedInputStream<>(digest, call, inputStream);
    }

    private static @NotNull ByteBuffer getSharedBuffer() {
        var ref = bufferThreadLocal.get();
        var r = ref != null ? ref.get() : null;
        if (r == null) {
            r = ByteBuffer.allocate(BUFFER_SIZE);
            bufferThreadLocal.set(new WeakReference<>(r));
        }
        return r;
    }
}
