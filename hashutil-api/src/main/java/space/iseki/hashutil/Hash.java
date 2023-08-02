package space.iseki.hashutil;

import org.jetbrains.annotations.NotNull;

/**
 * Common hash object
 */
public interface Hash {
    /**
     * Put bytes into the given array
     *
     * @param arr the array
     * @param off offset of the array
     */
    @NotNull
    byte[] bytes(@NotNull byte[] arr, int off);

    /**
     * Returns byte array of hash, in big-endian
     *
     * @return the array
     */
    @NotNull
    byte[] bytes();

    /**
     * Hash algorithm
     *
     * @return the algorithm
     */
    @NotNull
    Algorithms kind();
}
