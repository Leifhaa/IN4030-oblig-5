package src;

public class SequentialConvexHull {
    private ConvexHull chart;
    private IntList coHull = new IntList();

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
        Line middleLine = new Line(chart, chart.MAX_X, chart.MIN_X);
        ConvexHullPointSplitter splitterTop = new ConvexHullPointSplitter(middleLine, chart);
        splitterTop.split();

        //Call recursive method for upper & lower part of the convex hull
        seqRec(middleLine, splitterTop.lowestLeftPointIndex, splitterTop.leftSide);
        coHull.add(chart.MIN_X);

        Line middleLine2 = new Line(chart, chart.MIN_X, chart.MAX_X);
        ConvexHullPointSplitter splitterBottom = new ConvexHullPointSplitter(middleLine2, splitterTop.rightSide);
        splitterBottom.split();
        seqRec(middleLine2, splitterBottom.lowestLeftPointIndex, splitterBottom.leftSide);

        Oblig5Precode precode = new Oblig5Precode(this.chart, this.coHull);
        precode.margin = 200;
        precode.drawGraph();
    }

    private void seqRec(Line line, int p3, IntList pointCandidates){
        Line firstLine = new Line(chart, line.getStartIndex(), p3);

        //Find new point which is the least non-negative
        ConvexHullPointSplitter splitter = new ConvexHullPointSplitter(firstLine, pointCandidates);
        splitter.split();
        if (splitter.hasFoundLowestPoint()){
            seqRec(firstLine, splitter.lowestLeftPointIndex, splitter.leftSide);
        }
        coHull.add(p3);

        Line secondLine = new Line(chart, p3, line.getEndIndex());
        ConvexHullPointSplitter secondSplitter = new ConvexHullPointSplitter(secondLine, splitter.rightSide);
        secondSplitter.split();
        if (secondSplitter.hasFoundLowestPoint()){
            seqRec(secondLine, secondSplitter.lowestLeftPointIndex, secondSplitter.leftSide);
        }
    }

    /**
     * Helper class to find out points which is above or below a line in a convex hull
     */
    private class ConvexHullPointSplitter {
        private final Line line;
        private final IntList pointCandidates;
        private IntList rightSide;
        private IntList leftSide;
        private int lowestLeftPointIndex = -1;

        public boolean hasFoundLowestPoint(){
            return lowestLeftPointIndex != -1;
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
            int lowestPoint = 0;
            rightSide = new IntList();
            leftSide = new IntList();
            for (int i = 0; i < pointCandidates.len; i++){
                int candidate = pointCandidates.get(i);
                if (candidate == line.getStartIndex() || candidate == line.getEndIndex()){
                    //Don't include the line start & end points.
                    continue;
                }

                int x = chart.x[candidate];
                int y = chart.y[candidate];
                int distance = line.calcRelativeDistance(x, y);
                if (distance > 0){
                    rightSide.add(pointCandidates.get(i));
                }
                else if (distance < 0){
                    leftSide.add(pointCandidates.get(i));
                    if (distance < lowestPoint){
                        lowestLeftPointIndex = pointCandidates.get(i);
                        lowestPoint = distance;
                    }
                }
                else{
                    //On the line
                }
            }
        }
    }
}
