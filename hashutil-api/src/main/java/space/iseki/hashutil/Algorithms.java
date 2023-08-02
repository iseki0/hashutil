package space.iseki.hashutil;

public enum Algorithms {
    MD5(16),
    SHA1(20),
    SHA128(16),
    SHA224(28),
    SHA256(32),
    SHA512(64),

    ;

    private final int len;

    Algorithms(int len) {
        this.len = len;
    }

    /**
     * the hash length
     *
     * @return number of bytes
     */
    public int length() {
        return len;
    }
}
