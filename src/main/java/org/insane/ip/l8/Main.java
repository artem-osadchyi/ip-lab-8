package org.insane.ip.l8;

public class Main {
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

    public static double[][] getImage(String path) {
        return null;
    }

    public static double[][] empty(double[][] data) {
        double[][] result = new double[data.length][];

        for (int i = 0; i < data.length; i++)
            result[i] = new double[data[i].length];

        return result;
    }

    public static double[][] convolve(double[][] data, double[][] kernel) {
        double[][] result = null;

        int dataSize = 0;
        int kernelSize = 0;

        for (int i = 0; i < dataSize; i++) {
            for (int j = 0; j < dataSize; j++) {
                for (int x = i - kernelSize / 2; x <= i + kernelSize / 2; x++) {
                    for (int y = j - kernelSize / 2; y <= j + kernelSize / 2; y++) {

                        ;
                    }
                }
            }
        }

        return result;
    }

    public static double[][] sobel(double[][] data) {
        int width = data.length;
        int height = data[0].length;

        double[][] result = new double[height][width];

        double[][] sx = convolve(data, SX);
        double[][] sy = convolve(data, SY);

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                result[x][y] = Math.sqrt(Math.pow(sx[x][y], 2) + Math.pow(sy[x][y], 2));

        return result;
    }

    public static double[][] normalize(double[][] data, double min, double max) {
        int height = data.length;
        int width = data[0].length;

        double[][] result = new double[height][width];

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                result[x][y] = (data[x][y] - min) / (max - min);

        return result;
    }

    public static double[][] sqr(double[][] data) {
        int height = data.length;
        int width = data[0].length;

        double[][] result = new double[height][width];

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                result[x][y] = data[x][y] * data[x][y];

        return result;
    }

    public static double[][] dx(double[][] data) {
        return convolve(data, DX);
    }

    public static double[][] dy(double[][] data) {
        return convolve(data, DY);
    }

    public static double[][] laplace(double[][] data) {
        return convolve(data, LAPLACIAN);
    }

    public static void gvfc(double[][] image, double mu, double dt, int iterations) {
        int width = 0;
        int height = 0;
        double[][] grim = normalize(sobel(image), 0, 255);
        double[][] fsqr = sqr(sobel(grim));
        double[][] fx = dx(grim);
        double[][] fy = dy(grim);
        double[][] u = fx;
        double[][] v = fy;
        for (int i = 0; i < iterations; i++) {
            double[][] tmpx = laplace(u);
            double[][] tmpy = laplace(v);
            for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++) {
                    tmpx[x][y] = mu * tmpx[x][y] - (u[x][y] - fx[x][y]) * fsqr[x][y];
                    tmpy[x][y] = mu * tmpy[x][y] - (v[x][y] - fy[x][y]) * fsqr[x][y];
                }
            for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++) {
                    u[x][y] += Math.pow(dt, tmpx[x][y]);
                    v[x][y] += Math.pow(dt, tmpy[x][y]);
                }
        }
    }

    public static void main(String[] args) {
        String path = "";
        double mu = 0.2;
        double dt = 0.5;
        int iterations = 500;
        gvfc(getImage(path), mu, dt, iterations);
    }
}
