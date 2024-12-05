package space.iseki.hashutil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class MessageDigestInterceptedInputStream<T> extends InputStream {
    private final MessageDigest digest;
    private final SharedUtil.FC<T> creator;
    private final InputStream inputStream;
    private T r;

    MessageDigestInterceptedInputStream(MessageDigest digest, SharedUtil.FC<T> call, InputStream inputStream) {
        this.digest = digest;
        this.creator = call;
        this.inputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        checkDigestIsNull();
        var n = inputStream.read();
        if (n != -1) {
            digest.update((byte) n);
        }
        return inputStream.read();
    }

    @Override
    public int read(byte @NotNull [] b, int off, int len) throws IOException {
        checkDigestIsNull();
        var n = inputStream.read(b, off, len);
        if (n != -1) {
            digest.update(b, off, n);
        }
        return n;
    }

    @Override
    public long skip(long n) throws IOException {
        return inputStream.skip(n);
    }

    @Override
    public void skipNBytes(long n) throws IOException {
        inputStream.skipNBytes(n);
    }

    public T getDigest() {
        if (r != null) return r;
        return r = creator.apply(digest.digest(), 0);
    }

    private void checkDigestIsNull() {
        if (r != null) throw new IllegalStateException("digest is already created");
    }

    @Override
    public int available() throws IOException {
        return inputStream.available();
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
