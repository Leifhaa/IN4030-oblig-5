package src;

public class Main {
    public static void main(String[] args) {


        int n = 10;


        /*
        ConvexHull chart = new ConvexHull(n);
        SequentialConvexHull seq = new SequentialConvexHull(chart);
        seq.populateChart(n);

        long seqTime = System.nanoTime();
        seq.find();
        double total = (System.nanoTime() - seqTime) / 1000000.0;;
        System.out.println(total);
        Oblig5Precode precode = new Oblig5Precode(seq.chart, seq.coHull);
        precode.margin = 200;
        precode.drawGraph();

         */





        ParaConvexHull para = new ParaConvexHull(n);

        long seqTime = System.nanoTime();
        para.find();
        double total = (System.nanoTime() - seqTime) / 1000000.0;;
        System.out.println(total);

        Oblig5Precode precode = new Oblig5Precode(para.chart, para.coHull);
        precode.margin = 200;
        precode.drawGraph();
    }
}
