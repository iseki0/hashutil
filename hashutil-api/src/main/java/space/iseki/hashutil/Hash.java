package space.iseki.hashutil;

import org.jetbrains.annotations.NotNull;

public interface Hash {
    @NotNull
    byte[] bytes(@NotNull byte[] arr, int off);

    @NotNull byte[] bytes();

    @NotNull Algorithms kind();
}
