package src;

public class Main {
    private static int totalRuns = 7;

    public static void main(String[] args) {
        int m, n, seed, nThreads;
        try {
            m = Integer.parseInt(args[0]);
            n = Integer.parseInt(args[1]);
            seed = Integer.parseInt(args[2]);
            nThreads = Integer.parseInt(args[3]);
        } catch (Exception e) {
            System.out.println("Correct usage is: java -cp target/IN4030-oblig-5-1.0-SNAPSHOT.jar src.Main <m> <n> <seed> <threads>");
            return;
        }
        if (nThreads == 0) {
            nThreads = Runtime.getRuntime().availableProcessors();
        }
        if (m == 0){
            runSequential(n, seed, nThreads);
        }
        else if (m == 1){
            runParallel(n, seed, nThreads);
        }



        //Oblig5Precode precode = new Oblig5Precode(para.chart, para.coHull);
        //precode.margin = 200;
        //precode.drawGraph();
    }

    private static void runSequential(int n, int seed, int nThreads){
        for (int runId = 0; runId < totalRuns; runId++) {
            ConvexHull chart = new ConvexHull(n);
            SequentialConvexHull seq = new SequentialConvexHull(chart);
            seq.populateChart(n, seed);
            long seqTime = System.nanoTime();
            seq.find();
            double total = (System.nanoTime() - seqTime) / 1000000.0;;
            System.out.println("Sequential algorithm finished in " + total + "ms");
        }
    }

    private static void runParallel(int n , int seed, int nThreads){
        for (int runId = 0; runId < totalRuns; runId++) {
            ParaConvexHull para = new ParaConvexHull(n, seed, nThreads);

            long seqTime = System.nanoTime();
            para.find();
            double total = (System.nanoTime() - seqTime) / 1000000.0;;
            System.out.println("Parallel algorithm finished in " + total + "ms");
        }
    }
}
