package src;

public class Main {
    private static int totalRuns = 7;

    public static void main(String[] args) {
        int m, n, seed, nThreads, output;
        try {
            m = Integer.parseInt(args[0]);
            n = Integer.parseInt(args[1]);
            seed = Integer.parseInt(args[2]);
            nThreads = Integer.parseInt(args[3]);
            output = Integer.parseInt(args[4]);
        } catch (Exception e) {
            System.out.println("Correct usage is: java -cp target/IN4030-oblig-5-1.0-SNAPSHOT.jar src.Main <m> <n> <seed> <threads>");
            return;
        }
        if (nThreads == 0) {
            nThreads = Runtime.getRuntime().availableProcessors();
        }
        if (m == 0){
            runSequential(n, seed, nThreads, output);
        }
        else if (m == 1){
            runParallel(n, seed, nThreads, output);
        }
        else if (m == 2){
            runBenchmark(100, seed, nThreads);
            runBenchmark(1000, seed, nThreads);
            runBenchmark(10000, seed, nThreads);
            runBenchmark(100000, seed, nThreads);
            runBenchmark(1000000, seed, nThreads);
            runBenchmark(10000000, seed, nThreads);
        }
    }

    private static void runBenchmark(int n, int seed, int nThreads) {
        System.out.println("Benchmarking " + n + ". Please wait...");
        double seqTimes[] = new double[totalRuns];
        double paraTimes[] = new double[totalRuns];
        for (int runId = 0; runId < totalRuns; runId++){
            //Seq
            ConvexHull chart = new ConvexHull(n);
            SequentialConvexHull seq = new SequentialConvexHull(chart);
            seq.populateChart(n, seed);
            long seqTime = System.nanoTime();
            seq.find();
            seqTimes[runId] = (System.nanoTime() - seqTime) / 1000000.0;

            //para
            ParaConvexHull para = new ParaConvexHull(n, seed, nThreads);
            long paraTime = System.nanoTime();
            para.find();
            paraTimes[runId] = (System.nanoTime() - paraTime) / 1000000.0;
        }
        double seqMedian = seqTimes[(seqTimes.length) / 2];
        double paraMedian = paraTimes[(paraTimes.length) / 2];
        System.out.println("Median time of sequential run: " + seqMedian + "ms" );
        System.out.println("Median time of paralell run: " + paraMedian + "ms" );
        System.out.println("Total speedup: " + seqMedian / paraMedian + " for number " + n);
    }

    private static void runSequential(int n, int seed, int nThreads, int output){
        SequentialConvexHull seq = null;
        for (int runId = 0; runId < totalRuns; runId++) {
            ConvexHull chart = new ConvexHull(n);
            seq = new SequentialConvexHull(chart);
            seq.populateChart(n, seed);
            long seqTime = System.nanoTime();
            seq.find();
            double total = (System.nanoTime() - seqTime) / 1000000.0;;
            System.out.println("Sequential algorithm finished in " + total + "ms");
        }
        produceOutput(output, seq.chart, seq.coHull);
    }

    private static void runParallel(int n , int seed, int nThreads, int output){
        ParaConvexHull para = null;
        for (int runId = 0; runId < totalRuns; runId++) {
            para = new ParaConvexHull(n, seed, nThreads);

            long seqTime = System.nanoTime();
            para.find();
            double total = (System.nanoTime() - seqTime) / 1000000.0;;
            System.out.println("Parallel algorithm finished in " + total + "ms");
        }
        produceOutput(output, para.chart, para.coHull);
    }

    private static void produceOutput(int output, ConvexHull d, IntList CoHull){
        if (output == 1){
            Oblig5Precode precode = new Oblig5Precode(d, CoHull);
            precode.writeHullPoints();
        }
        else if (output == 2){
            Oblig5Precode precode = new Oblig5Precode(d, CoHull);
            precode.margin = 200;
            precode.drawGraph();
        }
    }

}
