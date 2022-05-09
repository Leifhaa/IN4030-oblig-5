package src;

public class ParaConvexHull {

    private final ConvexHull chart;
    int nThreads;
    private FindMaxWorker[] findMaxWorkers;
    private Thread[] findMaxThreads;


    public ParaConvexHull(int n){
        this.chart = new ConvexHull(n);
        NPunkter17 nPnkter = new NPunkter17(n, 2);
        nPnkter.fyllArrayer(chart.x, chart.y);
        nThreads = Runtime.getRuntime().availableProcessors();
        findMaxWorkers = new FindMaxWorker[nThreads];
        findMaxThreads = new Thread[nThreads];
    }

    public void find(){
        findMinAndMax();

        System.out.println("Hello world!");
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