package org.insane.ip.l8;

import java.awt.image.BufferedImage;

import com.google.common.primitives.Ints;

public class MyImage {
    private final int[][] pixels;
    private final int width;
    private final int height;
    private final int bitDepth;

    protected MyImage(int[][] pixels, int width, int height, int bitDepth) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        this.bitDepth = bitDepth;
    }

    public int getPixel(int x, int y) {
        int ix = x < 0 ? 0 : x >= width ? width - 1 : x;
        int iy = y < 0 ? 0 : y >= height ? height - 1 : y;

        return pixels[ix][iy];
    }

    public int[][] getRegion(int x0, int y0, int x1, int y1) {
        int width = x1 - x0 + 1;
        int height = y1 - y0 + 1;

        int[][] result = new int[width][height];

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                result[x][y] = getPixel(x0 + x, y0 + y);

        return result;
    }

    public int[][] getNeighbourhood(int x, int y, int size) {
        int x0 = x - size;
        int y0 = y - size;
        int x1 = x + size;
        int y1 = y + size;

        return getRegion(x0, y0, x1, y1);
    }

    public MyImage filter(Filter filter) {
        int[][] result = new int[width][height];

        int size = filter.getSize();

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                result[x][y] = filter.apply(getNeighbourhood(x, y, size / 2));

        return new MyImage(result, width, height, bitDepth);
    }

    public MyImage adjust() {
        int[][] result = new int[width][height];

        int max = getMax();
        int min = getMin();
        int maxGrey = 1 << bitDepth - 1;

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                result[x][y] = (int) (((double) pixels[x][y] - min) / max * maxGrey);

        return new MyImage(result, width, height, bitDepth);
    }

    private int[] flatten(int[][] data) {
        int[] result = new int[height * width];

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                result[x * width + y] = data[x][y];

        return result;
    }

    public int getMin() {
        return Ints.min(flatten(pixels));
    }

    public int getMax() {
        return Ints.max(flatten(pixels));
    }

}
