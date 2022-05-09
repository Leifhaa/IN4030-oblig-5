package src;

public class ConvexHullPointSplitterPara {

    private final Line line;
    private IntList pointCandidates;
    private final ConvexHull chart;
    private IntList rightSide;
    private IntList leftSide;
    private IntList midPoints;
    private int lowestLeftPointIndex = -1;
    private int lowestPointVal = 0;
    private int highestRightPointIndex = -1;
    private int highestPointVal = 0;



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

    public ConvexHullPointSplitterPara(Line midLineLeft, ConvexHull chart) {
        this.line = midLineLeft;
        this.chart = chart;
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
                if (distance > highestPointVal){
                    highestRightPointIndex = candidate;
                    highestPointVal = distance;
                }
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

    public int getHighestRIghtPointIndex() {
        return highestRightPointIndex;
    }

    public int getHighestPointVal(){
        return highestPointVal;
    }
}
