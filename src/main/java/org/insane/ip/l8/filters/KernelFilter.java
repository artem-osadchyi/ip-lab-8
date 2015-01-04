package org.insane.ip.l8.filters;

import org.insane.ip.l8.Filter;

public class KernelFilter implements Filter {
    private final double[][] kernel;

    public KernelFilter(double[][] kernel) {
        this.kernel = kernel;
    }

    @Override
    public int apply(int[][] region) {
        double result = 0;

        for (int x = 0; x < getSize(); x++)
            for (int y = 0; y < getSize(); y++)
                result += kernel[x][y] * region[x][y];

        return (int) result;
    }

    @Override
    public int getSize() {
        return kernel.length;
    }

}
