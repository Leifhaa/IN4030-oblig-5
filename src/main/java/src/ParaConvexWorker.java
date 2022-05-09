package src;

import java.util.Arrays;
import java.util.Comparator;

public class ParaConvexWorker implements Runnable {
    private final int maxDepth;
    private final boolean allowThreadCreation;
    private Line parentLine;
    private ConvexHullPointSplitter parentSplitter;
    private final ConvexHull chart;
    private int treeLevel;
    private SequentialConvexHull seq;
    private IntList pointsFound = new IntList();

    public ParaConvexWorker(Line parentLine, ConvexHullPointSplitter splitter, ConvexHull chart, int treeLevel, int maxDepth) {
        this.parentLine = parentLine;
        this.parentSplitter = splitter;
        this.chart = chart;
        this.treeLevel = treeLevel + 1;
        this.allowThreadCreation = treeLevel < maxDepth;
        this.maxDepth = maxDepth;
    }

    @Override
    public void run() {
        parentSplitter.split();

        //If there's new external points, create new threads
        if (parentSplitter.hasFoundLowestPoint()){
            //Create new threads for investigating these external threads, if allowed
            if (allowThreadCreation) {
                Line line0 = new Line(chart, parentLine.getStartIndex(), parentSplitter.getLowestLeftPointIndex());
                ConvexHullPointSplitter splitter0 = new ConvexHullPointSplitter(line0, parentSplitter.getLeftSide(), chart);
                ParaConvexWorker worker0 = new ParaConvexWorker(line0, splitter0, chart, treeLevel, maxDepth);
                Thread thread0 = new Thread(worker0);
                thread0.start();


                //Process the second node instead of creating a thread for it. Otherwise this thread would just be idle.
                Line line1 = new Line(chart, parentSplitter.getLowestLeftPointIndex(), parentLine.getEndIndex());
                ConvexHullPointSplitter splitter1 = new ConvexHullPointSplitter(line1, parentSplitter.getLeftSide(), chart);
                ParaConvexWorker worker1 = new ParaConvexWorker(line1, splitter1, chart, treeLevel, maxDepth);
                worker1.run();


                try {
                    thread0.join();
                    //thread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                pointsFound.append(worker0.getPointsFound());
                pointsFound.append(worker1.getPointsFound());
            } else {
                //Do it sequentially as we're not allowed to create more threads
                SequentialConvexHull seq = new SequentialConvexHull(chart);
                seq.seqRec(parentLine, parentSplitter.getLowestLeftPointIndex(), parentSplitter.getLeftSide());
                pointsFound.append(seq.coHull);
            }
        }
        else{
            pointsFound.append(parentSplitter.getMidPoints());
            pointsFound.add(parentLine.getEndIndex());
        }
    }


    public IntList getPointsFound() {
        return pointsFound;
    }
}
