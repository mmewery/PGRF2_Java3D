package raster;

import java.util.Optional;

public interface Raster<T> {
    void setValue(int x, int y, T value);
    Optional<T> getValue(int x, int y);
    int getWidth();
    int getHeight();
    void clear();
}
