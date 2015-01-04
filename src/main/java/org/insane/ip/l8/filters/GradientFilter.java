package org.insane.ip.l8.filters;

import org.insane.ip.l8.Filter;

public class GradientFilter implements Filter {
    private final Filter dx;
    private final Filter dy;

    public GradientFilter(Filter dx, Filter dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public int apply(int[][] region) {
        int dx = this.dx.apply(region);
        int dy = this.dy.apply(region);

        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public int getSize() {
        return dx.getSize();
    }

}
