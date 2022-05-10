package src;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.SECONDS)
public class Benchmarks {
    private static int n = 100_000_00;
    private static int seed = 1;


    @Benchmark
    @Fork(1)
    @Warmup(iterations = 1)
    @Measurement(iterations = 3)
    public void testSequential(){
        long seqTime = System.nanoTime();
        ConvexHull chart = new ConvexHull(n);
        SequentialConvexHull seq = new SequentialConvexHull(chart);
        seq.populateChart(n, seed);
        double res = (System.nanoTime() - seqTime) / 1000000.0;
        System.out.println("Generating points took " + res);

        seq.find();
    }

    @Benchmark
    @Fork(1)
    @Warmup(iterations = 1)
    @Measurement(iterations = 3)
    public void testParallel(){
        long seqTime = System.nanoTime();
        ParaConvexHull para = new ParaConvexHull(n, seed, Runtime.getRuntime().availableProcessors());
        double res = (System.nanoTime() - seqTime) / 1000000.0;
        System.out.println("Generating points took " + res);
        para.find();
    }
}
