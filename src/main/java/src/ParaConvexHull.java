package src;

public class ParaConvexHull {

    public final ConvexHull chart;
    int nThreads;
    private FindMaxWorker[] findMaxWorkers;
    private Thread[] findMaxThreads;
    public IntList coHull = new IntList();

    public ParaConvexHull(int n){
        this.chart = new ConvexHull(n);
        NPunkter17 nPnkter = new NPunkter17(n, 2);
        nPnkter.fyllArrayer(chart.x, chart.y);
        nThreads = Runtime.getRuntime().availableProcessors();
        findMaxWorkers = new FindMaxWorker[nThreads];
        findMaxThreads = new Thread[nThreads];
    }

    public void find() {
        int treeLevel = 1;
        findMinAndMax();

        //Decide depth of using threads
        int maxDepth = nThreads / 2;

        //Create the middle line between min & max
        Line midLineLeft = new Line(chart, chart.MAX_X, chart.MIN_X);

        /**
         * Do the initial splitting only once
         */
        ConvexHullPointSplitterPara initSplit = new ConvexHullPointSplitterPara(midLineLeft, chart);
        initSplit.splitByChart();

        ConvexHullPointSplitter splitterLeft = new ConvexHullPointSplitter(midLineLeft, initSplit.getLeftSide(), chart);
        splitterLeft.setSplitValues(initSplit.getRightSide(), initSplit.getLeftSide(), initSplit.getMidPoints(), initSplit.getLowestLeftPointIndex(), initSplit.getLowestPointVal());
        ParaConvexWorker worker0 = new ParaConvexWorker(midLineLeft, splitterLeft, this.chart, treeLevel, maxDepth);
        Thread thread0 = new Thread(worker0);
        thread0.start();


        Line midlineRight = new Line(chart, chart.MIN_X, chart.MAX_X);
        ConvexHullPointSplitter splitterRight = new ConvexHullPointSplitter(midlineRight, initSplit.getRightSide(), chart);
        splitterRight.setSplitValues(initSplit.getLeftSide(), initSplit.getRightSide(), initSplit.getMidPoints(), initSplit.getHighestRIghtPointIndex(), initSplit.getHighestPointVal());
        ParaConvexWorker worker1 = new ParaConvexWorker(midlineRight, splitterRight, this.chart, treeLevel, maxDepth);
        worker1.run();

        try {
            thread0.join();
            //thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        coHull.add(chart.MAX_X);
        coHull.append(worker0.getPointsFound());
        coHull.add(chart.MIN_X);
        coHull.append(worker1.getPointsFound());
    }

    private void findMinAndMax() {
        int readSize = chart.x.length / nThreads;
        int readFrom = 0;
        for (int i = 0; i < nThreads; i++){
            int tmpReadSize = readSize;
            if (i < chart.x.length % nThreads) {
                tmpReadSize++;
            }
            FindMaxWorker worker = new FindMaxWorker(readFrom, readFrom + readSize, chart);
            findMaxWorkers[i] = worker;
            findMaxThreads[i] = new Thread(worker);
            findMaxThreads[i].start();
            readFrom += tmpReadSize;
        }

        for (int i = 0; i < nThreads; i++){
            try {
                findMaxThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < findMaxWorkers.length; i++){
            if (findMaxWorkers[i].getMinValue() < chart.x[chart.MIN_X]){
                chart.MIN_X = findMaxWorkers[i].getMinCandidate();
            }
            if (findMaxWorkers[i].getMaxValue() > chart.x[chart.MAX_X]){
                chart.MAX_X = findMaxWorkers[i].getMaxCandidate();
            }
        }
    }


}
