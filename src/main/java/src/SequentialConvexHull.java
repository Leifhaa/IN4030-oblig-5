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
        ConvexHullPointSplitter splitter = new ConvexHullPointSplitter(middleLine, chart);
        splitter.split();

        //Call recursive method for upper & lower part of the convex hull
        seqRec(middleLine, splitter.lowestPointIndex, splitter.lowerPoints);
        seqRec(middleLine, splitter.lowestPointIndex, splitter.upperPoints);

        Oblig5Precode precode = new Oblig5Precode(this.chart, this.coHull);
        precode.margin = 200;
        precode.drawGraph();
    }

    private void seqRec(Line line, int p3, IntList pointCandidates){
        coHull.add(p3);
        Line firstLine = new Line(chart, line.getStartIndex(), p3);

        //Find new point which is the least non-negative
        ConvexHullPointSplitter splitter = new ConvexHullPointSplitter(firstLine, pointCandidates);
        splitter.split();
        if (splitter.hasFoundLowestPoint()){
            seqRec(firstLine, splitter.lowestPointIndex, splitter.lowerPoints);
        }

        Line secondLine = new Line(chart, p3, line.getEndIndex());
        ConvexHullPointSplitter secondSplitter = new ConvexHullPointSplitter(secondLine, splitter.upperPoints);
        secondSplitter.split();
        if (secondSplitter.hasFoundLowestPoint()){
            seqRec(secondLine, secondSplitter.lowestPointIndex, secondSplitter.lowerPoints);
        }
    }

    /**
     * Helper class to find out points which is above or below a line in a convex hull
     */
    private class ConvexHullPointSplitter {
        private final Line line;
        private final IntList pointCandidates;
        private IntList upperPoints;
        private IntList lowerPoints;
        private int lowestPointIndex = -1;

        public boolean hasFoundLowestPoint(){
            return lowestPointIndex != -1;
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

        public IntList getUpperPoints() {
            return upperPoints;
        }

        public IntList getLowerPoints() {
            return lowerPoints;
        }

        public void split(){
            int lowestPoint = 0;
            int highestPoint = 0;
            upperPoints = new IntList();
            lowerPoints = new IntList();
            for (int i = 0; i < pointCandidates.len; i++){
                int distance = line.calcRelativeDistance(chart.x[pointCandidates.get(i)], chart.y[pointCandidates.get(i)]);
                if (distance >= 0){
                    upperPoints.add(pointCandidates.get(i));
                    if (distance > highestPoint){
                        highestPoint = distance;
                    }
                }
                else{
                    lowerPoints.add(pointCandidates.get(i));
                    if (distance < lowestPoint){
                        lowestPointIndex = pointCandidates.get(i);
                        lowestPoint = distance;
                    }
                }
            }
        }
    }
}
