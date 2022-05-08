package src;

import java.util.Arrays;

public class SequentialConvexHull {
    public ConvexHull chart;
    public IntList coHull = new IntList();

    public SequentialConvexHull(int n){
        this.chart = new ConvexHull(n);
        NPunkter17 nPnkter = new NPunkter17(n, 2);
        nPnkter.fyllArrayer(chart.x, chart.y);
    }

    private void findMinAndMaxX(){
        int tmpMin = chart.x[0];
        int tmpMax = tmpMin;

        for (int i = 1; i < chart.x.length; i++){
            if (chart.x[i] > tmpMax){
                tmpMax = chart.x[i];
                chart.MAX_X = i;
            }

            if (chart.x[i] < tmpMin){
                tmpMin = chart.x[i];
                chart.MIN_X = i;
            }
        }
    }

    public void find(){
        /**
         * We can start with two points that we can see are obviously on the convex hull: the point that
         * has the lowest value for x and the point that has the highest value for x
         */
        findMinAndMaxX();

        //Add max X
        coHull.add(chart.MAX_X);


        //Create the middle line between min & max
        Line midLineLeft = new Line(chart, chart.MAX_X, chart.MIN_X);
        ConvexHullPointSplitter splitterTop = new ConvexHullPointSplitter(midLineLeft, chart);
        splitterTop.split();

        //Call recursive method for upper & lower part of the convex hull
        seqRec(midLineLeft, splitterTop.lowestLeftPointIndex, splitterTop.leftSide);
        coHull.add(chart.MIN_X);

        Line midLineToRight = new Line(chart, chart.MIN_X, chart.MAX_X);
        ConvexHullPointSplitter splitterBottom = new ConvexHullPointSplitter(midLineToRight, splitterTop.rightSide);
        splitterBottom.split();
        seqRec(midLineToRight, splitterBottom.lowestLeftPointIndex, splitterBottom.leftSide);

        //Oblig5Precode precode = new Oblig5Precode(this.chart, this.coHull);
        //precode.margin = 200;
        //precode.drawGraph();
    }

    private void seqRec(Line line, int p3, IntList pointCandidates){
        //Try create first line
        Line firstLine = new Line(chart, line.getStartIndex(), p3);
        ConvexHullPointSplitter firstSplitter = new ConvexHullPointSplitter(firstLine, pointCandidates);
        firstSplitter.split();
        if (firstSplitter.hasFoundLowestPoint()){
            seqRec(firstLine, firstSplitter.lowestLeftPointIndex, firstSplitter.leftSide);
        }
        else{
            //Remaining points (if any) is on the current line
            sortLinePoints(firstSplitter.midPoints, line.getEndIndex());
            this.coHull.append(firstSplitter.midPoints);
        }

        coHull.add(p3);

        //Try create second line
        Line secondLine = new Line(chart, p3, line.getEndIndex());
        ConvexHullPointSplitter secondSplitter = new ConvexHullPointSplitter(secondLine, firstSplitter.rightSide);
        secondSplitter.split();
        if (secondSplitter.hasFoundLowestPoint()){
            seqRec(secondLine, secondSplitter.lowestLeftPointIndex, secondSplitter.leftSide);
        }
        else{
            //Remaining points (if any) is on the current line
            sortLinePoints(secondSplitter.midPoints, line.getStartIndex());
            this.coHull.append(secondSplitter.midPoints);
        }
    }


    private void sortLinePoints(IntList points, int p2){
        if (points.len == 0){
            return;
        }

        //Create an copy of the array (size has to be correct here)
        Integer[] clone = new Integer[points.len];
        for (int i = 0; i < points.len; i++){
            clone[i] = points.get(i);
        }

        Arrays.sort(clone, 0, points.size(), ((Integer i, Integer j) -> (relativeDistanceBetweenPoints(i, p2) - relativeDistanceBetweenPoints(j, p2))));

        for (int i = 0; i < clone.length; i++) {
            points.data[i] = clone[i];
        }
    }

    private int relativeDistanceBetweenPoints(int p1, int p2) {
        return (int) (Math.pow(chart.x[p1] - chart.x[p2], 2) + Math.pow(chart.y[p1] - chart.y[p2], 2));
    }

    /**
     * Helper class to find out points which is above or below a line in a convex hull
     */
    private class ConvexHullPointSplitter {
        private final Line line;
        private final IntList pointCandidates;
        private IntList rightSide;
        private IntList leftSide;
        private IntList midPoints;
        private int lowestLeftPointIndex = -1;
        private int lowestPointVal = 0;

        public boolean hasFoundLowestPoint(){
            return lowestLeftPointIndex != -1;
        }

        public boolean hasPointsOnLine(){
            return leftSide.len > 0 && lowestPointVal == 0;
        }

        public int pointsOnLine(){
            return leftSide.len;
        }

        public ConvexHullPointSplitter(Line line, IntList pointCandidates){
            this.pointCandidates = pointCandidates;
            this.line = line;
        }

        public ConvexHullPointSplitter(Line middleLine, ConvexHull chart) {
            IntList list = new IntList(chart.n);
            for (int i = 0; i < chart.n; i++){
                list.add(i);
            }
            this.pointCandidates = list;
            this.line = middleLine;
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
    }
}
