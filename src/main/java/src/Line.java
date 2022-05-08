package src;

import org.apache.commons.math3.analysis.function.Sqrt;

public class Line {
    private final int a;
    private final int b;
    private final int c;
    private final int startIndex;
    private final int endIndex;

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public Line(ConvexHull chart, int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        int x1 = chart.x[chart.MAX_X];
        int y1 = chart.y[chart.MAX_X];
        int x2 = chart.x[chart.MIN_X];
        int y2 = chart.y[chart.MIN_X];

        a = y1 - y2;
        b = x2 - x1;
        c = y2 * x1 - y1 * x2;
    }


    /**
     * Calculates distance from this line to the given point
     * @param x
     * @param y
     * @return
     */
    public double calcAbsoluteDistance(int x, int y){
        return (a * x + b * y + c) / Math.sqrt((Math.pow(a, 2) + Math.pow(b, 2)));
    }

    public int calcRelativeDistance(int x, int y){
        return a * x + b * y + c;
    }
}
