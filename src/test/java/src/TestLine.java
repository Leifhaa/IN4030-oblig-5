package src;

import org.junit.Assert;
import org.junit.Test;

public class TestLine {

    @Test
    public void test100Seq(){
        SequentialConvexHull seq = new SequentialConvexHull(100);

        long seqTime = System.nanoTime();
        seq.find();
        double total = (System.nanoTime() - seqTime) / 1000000.0;;
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
        for (int i = 0; i <  seq.coHull.len; i++){
            Assert.assertEquals(seq.coHull.get(i), expected.get(i));
        }
    }

    @Test
    public void testPara100(){
        ParaConvexHull para = new ParaConvexHull(100);

        long seqTime = System.nanoTime();
        para.find();
        double total = (System.nanoTime() - seqTime) / 1000000.0;;
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
        //for (int i = 0; i <  para.coHull.len; i++){
        //    Assert.assertEquals(para.coHull.get(i), expected.get(i));
        //}
    }


}
