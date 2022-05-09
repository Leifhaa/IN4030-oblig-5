package src;

import org.junit.Assert;
import org.junit.Test;

public class TestLine {

    @Test
    public void test100Seq() {
        int n = 100;
        ConvexHull chart = new ConvexHull(n);
        SequentialConvexHull seq = new SequentialConvexHull(chart);
        seq.populateChart(n);

        long seqTime = System.nanoTime();
        seq.find();
        double total = (System.nanoTime() - seqTime) / 1000000.0;
        ;
        System.out.println(total);


        IntList expected = new IntList();
        expected.add(3);
        expected.add(34);
        expected.add(38);
        expected.add(10);
        expected.add(51);
        expected.add(16);
        expected.add(54);
        expected.add(88);
        expected.add(17);
        expected.add(47);
        expected.add(29);
        expected.add(81);
        expected.add(1);
        expected.add(65);
        expected.add(67);
        expected.add(85);
        for (int i = 0; i < seq.coHull.len; i++) {
            Assert.assertEquals(seq.coHull.get(i), expected.get(i));
        }
    }

    @Test
    public void testPara100() {
        ParaConvexHull para = new ParaConvexHull(100);

        long seqTime = System.nanoTime();
        para.find();
        double total = (System.nanoTime() - seqTime) / 1000000.0;
        ;
        System.out.println(total);


        IntList expected = new IntList();
        expected.add(3);
        expected.add(34);
        expected.add(38);
        expected.add(10);
        expected.add(51);
        expected.add(16);
        expected.add(54);
        expected.add(88);
        expected.add(17);
        expected.add(47);
        expected.add(29);
        expected.add(81);
        expected.add(1);
        expected.add(65);
        expected.add(67);
        expected.add(85);
        for (int i = 0; i < para.coHull.len; i++) {
            Assert.assertEquals(para.coHull.get(i), expected.get(i));
        }
    }

    @Test
    public void testSeq_10million() {
        int n = 100_000_00;

        for (int i = 0; i < 7; i++) {
            ConvexHull chart = new ConvexHull(n);
            SequentialConvexHull seq = new SequentialConvexHull(chart);
            seq.populateChart(n);

            long seqTime = System.nanoTime();
            seq.find();
            double total = (System.nanoTime() - seqTime) / 1000000.0;
            ;
            System.out.println(total);
        }

    }


    @Test
    public void testPara_10million() {
        int n = 100_000_00;

        for (int i = 0; i < 7; i++) {
            ParaConvexHull para = new ParaConvexHull(n);
            long seqTime = System.nanoTime();
            para.find();
            double total = (System.nanoTime() - seqTime) / 1000000.0;
            System.out.println(total);
        }

    }

    private void compareResults(int n) {
        ParaConvexHull para = new ParaConvexHull(n);
        para.find();


        ConvexHull chart = new ConvexHull(n);
        SequentialConvexHull seq = new SequentialConvexHull(chart);
        seq.populateChart(n);
        seq.find();

        Assert.assertEquals(para.coHull.len, seq.coHull.len);
        for (int i = 0; i < para.coHull.len; i++) {
            Assert.assertEquals(para.coHull.get(i), seq.coHull.get(i));
        }
    }

    @Test
    public void test_seqAndParaIsEqual_10() {
        int n = 10;
        compareResults(n);
    }



    @Test
    public void test_seqAndParaIsEqual_100() {
        int n = 100;
        compareResults(n);
    }

    @Test
    public void test_seqAndParaIsEqual_1000() {
        int n = 1000;
        compareResults(n);
    }

    @Test
    public void test_seqAndParaIsEqual_10000() {
        int n = 10000;
        compareResults(n);
    }

    @Test
    public void test_seqAndParaIsEqual_100000() {
        int n = 100000;
        compareResults(n);
    }


}
