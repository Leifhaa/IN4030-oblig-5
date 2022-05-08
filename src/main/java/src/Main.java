package src;

public class Main {
    public static void main(String[] args) {
        SequentialConvexHull seq = new SequentialConvexHull(100_0000_00);

        long seqTime = System.nanoTime();
        seq.find();
        double total = (System.nanoTime() - seqTime) / 1000000.0;;
        System.out.println(total);
    }
}
