package src;

import java.util.Arrays;
import java.util.Comparator;

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
        seqRec(midLineLeft, splitterTop.getLowestLeftPointIndex(), splitterTop.getLeftSide());
        coHull.add(chart.MIN_X);

        Line midLineToRight = new Line(chart, chart.MIN_X, chart.MAX_X);
        ConvexHullPointSplitter splitterBottom = new ConvexHullPointSplitter(midLineToRight, splitterTop.getRightSide(), chart);
        splitterBottom.split();
        seqRec(midLineToRight, splitterBottom.getLowestLeftPointIndex(), splitterBottom.getLeftSide());

        Oblig5Precode precode = new Oblig5Precode(this.chart, this.coHull);
        precode.margin = 200;
        precode.drawGraph();
    }

    private void seqRec(Line line, int p3, IntList pointCandidates){
        //Try create first line
        Line firstLine = new Line(chart, line.getStartIndex(), p3);
        ConvexHullPointSplitter firstSplitter = new ConvexHullPointSplitter(firstLine, pointCandidates, chart);
        firstSplitter.split();
        if (firstSplitter.hasFoundLowestPoint()){
            seqRec(firstLine, firstSplitter.getLowestLeftPointIndex(), firstSplitter.getLeftSide());
        }
        else{
            //Remaining points (if any) is on the current line
            sortLinePoints(firstSplitter.getMidPoints(), line.getEndIndex());
            this.coHull.append(firstSplitter.getMidPoints());
        }

        coHull.add(p3);

        //Try create second line
        Line secondLine = new Line(chart, p3, line.getEndIndex());
        ConvexHullPointSplitter secondSplitter = new ConvexHullPointSplitter(secondLine, firstSplitter.getRightSide(), chart);
        secondSplitter.split();
        if (secondSplitter.hasFoundLowestPoint()){
            seqRec(secondLine, secondSplitter.getLowestLeftPointIndex(), secondSplitter.getLeftSide());
        }
        else{
            //Remaining points (if any) is on the current line
            sortLinePoints(secondSplitter.getMidPoints(), line.getStartIndex());
            this.coHull.append(secondSplitter.getMidPoints());
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

        Arrays.sort(clone, 0, points.size(), (Comparator.comparingInt((Integer i) -> relativeDistanceBetweenPoints(i, p2))));

        for (int i = 0; i < clone.length; i++) {
            points.data[i] = clone[i];
        }
    }

    private int relativeDistanceBetweenPoints(int p1, int p2) {
        return (int) (Math.pow(chart.x[p1] - chart.x[p2], 2) + Math.pow(chart.y[p1] - chart.y[p2], 2));
    }
}
