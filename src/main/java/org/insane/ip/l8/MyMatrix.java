package org.insane.ip.l8;

public class MyMatrix {
    public static final double[][] SX = new double[][] {
            { -1, 0, 1 },
            { -2, 0, 2 },
            { -1, 0, 1 }
    };
    public static final double[][] SY = new double[][] {
            { -1, -2, -1 },
            { 0, 0, 0 },
            { 1, 2, 1 }
    };
    public static final double[][] DX = new double[][] {
            { 0, 0, 0 },
            { -0.5, 0, 0.5 },
            { 0, 0, 0 }
    };
    public static final double[][] DY = new double[][] {
            { 0, -0.5, 0 },
            { 0, 0, 0 },
            { 0, 0.5, 0 }
    };
    public static final double[][] LAPLACIAN = new double[][] {
            { -1, -1, -1 },
            { -1, 8, -1 },
            { -1, -1, -1 }
    };

    private final double[][] data;
    private final int width;
    private final int height;

    protected MyMatrix(double[][] data) {
        // data is vector of columns
        this.data = data;
        this.width = this.data.length;
        this.height = this.data[0].length;
    }

    public double[][] getData() {
        return data;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getItem(int x, int y) {
        int ix = x < 0 ? 0 : x >= width ? width - 1 : x;
        int iy = y < 0 ? 0 : y >= height ? height - 1 : y;

        return data[ix][iy];
    }

    public MyMatrix getRegion(int x0, int y0, int x1, int y1) {
        int width = x1 - x0 + 1;
        int height = y1 - y0 + 1;

        double[][] result = new double[width][height];

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                result[x][y] = getItem(x0 + x, y0 + y);

        return new MyMatrix(result);
    }

    public MyMatrix getNeighbourhood(int x, int y, int size) {
        return getRegion(x - size, y - size, x + size, y + size);
    }

    public MyMatrix convolve(double[][] kernel) {
        double[][] result = new double[width][height];

        int size = kernel.length / 2;

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                double[][] neighbourhood = getNeighbourhood(x, y, size).getData();
                result[x][y] = convolve(neighbourhood, kernel);
            }

        return new MyMatrix(result);
    }

    public MyMatrix sobels() {
        double[][] result = new double[width][height];

        double[][] sx = convolve(SX).getData();
        double[][] sy = convolve(SY).getData();

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                result[x][y] = Math.sqrt(Math.pow(sx[x][y], 2) + Math.pow(sy[x][y], 2));

        return new MyMatrix(result);
    }

    public MyMatrix normalize(double min, double max) {
        double[][] result = new double[width][height];

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                result[x][y] = (data[x][y] - min) / (max - min);

        return new MyMatrix(result);
    }

    public MyMatrix sqr() {
        double[][] result = new double[width][height];

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                result[x][y] = Math.pow(data[x][y], 2);

        return new MyMatrix(result);
    }

    public MyMatrix dx() {
        return convolve(DX);
    }

    public MyMatrix dy() {
        return convolve(DY);
    }

    public MyMatrix laplace() {
        return convolve(LAPLACIAN);
    }

    public static double convolve(double[][] a, double[][] b) {
        int width = a.length;
        int height = a[0].length;

        double result = 0;

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                result += a[x][y] * b[x][y];

        return result;
    }

}
