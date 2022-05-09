package src;


/**
 * Helper class to find out points which is above or below a line in a convex hull
 */
public class ConvexHullPointSplitter {
    private final Line line;
    private IntList pointCandidates;
    private final ConvexHull chart;
    private IntList rightSide;
    private IntList leftSide;
    private IntList midPoints;
    private int lowestLeftPointIndex = -1;
    private int lowestPointVal = 0;



    public boolean hasFoundLowestPoint(){
        return lowestLeftPointIndex != -1;
    }

    public int getLowestLeftPointIndex() {
        return lowestLeftPointIndex;
    }

    public int getLowestPointVal() {
        return lowestPointVal;
    }

    public IntList getRightSide() {
        return rightSide;
    }

    public IntList getLeftSide() {
        return leftSide;
    }

    public IntList getMidPoints() {
        return midPoints;
    }

    public ConvexHullPointSplitter(Line line, IntList pointCandidates, ConvexHull chart){
        this.pointCandidates = pointCandidates;
        this.line = line;
        this.chart = chart;
    }

    public ConvexHullPointSplitter(Line middleLine, ConvexHull chart) {
        IntList list = new IntList(chart.n);
        for (int i = 0; i < chart.n; i++){
            list.add(i);
        }
        this.pointCandidates = list;
        this.line = middleLine;
        this.chart = chart;
    }

    public ConvexHullPointSplitter(Line midLineLeft, ConvexHull chart, boolean parallel) {
        this.line = midLineLeft;
        this.chart = chart;
    }

    public void split(){
        rightSide = new IntList();
        leftSide = new IntList();
        midPoints = new IntList();
        for (int i = 0; i < pointCandidates.len; i++){
            int candidate = pointCandidates.get(i);

            if (candidate == line.getStartIndex() || candidate == line.getEndIndex()){
                continue;
            }

            int x = chart.x[candidate];
            int y = chart.y[candidate];
            int distance = line.calcRelativeDistance(x, y);
            if (distance > 0){
                rightSide.add(candidate);
            }
            else if (distance < 0) {
                leftSide.add(candidate);
                if (distance < lowestPointVal){
                    lowestLeftPointIndex = candidate;
                    lowestPointVal = distance;
                }
            }
            else{
                midPoints.add(candidate);
            }
        }
    }

    public void splitByChart(){
        rightSide = new IntList();
        leftSide = new IntList();
        midPoints = new IntList();
        for (int candidate = 0; candidate < chart.n; candidate++){

            if (candidate == line.getStartIndex() || candidate == line.getEndIndex()){
                continue;
            }

            int x = chart.x[candidate];
            int y = chart.y[candidate];
            int distance = line.calcRelativeDistance(x, y);
            if (distance > 0){
                rightSide.add(candidate);
            }
            else if (distance < 0) {
                leftSide.add(candidate);
                if (distance < lowestPointVal){
                    lowestLeftPointIndex = candidate;
                    lowestPointVal = distance;
                }
            }
            else{
                midPoints.add(candidate);
            }
        }
    }
}
