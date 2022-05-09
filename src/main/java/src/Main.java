package src;

public class Main {
    public static void main(String[] args) {
        int n = 100;
        ConvexHull chart = new ConvexHull(n);
        SequentialConvexHull seq = new SequentialConvexHull(chart);
        seq.populateChart(n);

        long seqTime = System.nanoTime();
        seq.find();
        double total = (System.nanoTime() - seqTime) / 1000000.0;;
        System.out.println(total);
    }
}
