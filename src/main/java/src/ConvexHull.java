package src;

public class ConvexHull {
    public int n;
    public int[] y;
    public int[] x;

    /**
     * Notice, these points will always be on the hull.
     * These are indexes, not values.
     */
    public int MIN_X;
    public int MAX_X;
    public int MAX_Y;

    public ConvexHull(int n) {
        this.n = n;
        this.y = new int[n];
        this.x = new int[n];
    }
}
