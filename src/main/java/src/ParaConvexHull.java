package src;

public class ParaConvexHull {

    private final ConvexHull chart;
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


        //Add max X
        coHull.add(chart.MAX_X);

        //Create the middle line between min & max
        Line midLineLeft = new Line(chart, chart.MAX_X, chart.MIN_X);
        ConvexHullPointSplitter splitter = new ConvexHullPointSplitter(midLineLeft, chart);
        splitter.split();

        //create 2 threads
        ParaConvexWorker worker0 = new ParaConvexWorker(midLineLeft, splitter, this.chart, treeLevel, maxDepth);
        Thread thread0 = new Thread(worker0);
        thread0.start();

        Line midlineRight = new Line(chart, chart.MIN_X, chart.MAX_X);
        ParaConvexWorker worker1 = new ParaConvexWorker(midlineRight, splitter, this.chart, treeLevel, maxDepth);
        Thread thread1 = new Thread(worker1);
        thread1.start();


        try {
            thread0.join();
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        coHull.add(chart.MAX_X);
        coHull.append(worker0.getPointsFound());
        coHull.add(chart.MIN_X);
        coHull.append(worker1.getPointsFound());



        Oblig5Precode precode = new Oblig5Precode(this.chart, this.coHull);
        precode.margin = 200;
        precode.drawGraph();
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
