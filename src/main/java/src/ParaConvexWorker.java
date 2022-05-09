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
    private IntList pointsFound;
    private ParaConvexWorker workers[];

    public ParaConvexWorker(Line parentLine, ConvexHullPointSplitter splitter, ConvexHull chart, int treeLevel, int maxDepth, IntList pointsFound){
        this.parentLine = parentLine;
        this.parentSplitter = splitter;
        this.chart = chart;
        this.treeLevel = treeLevel++;
        this.allowThreadCreation = treeLevel >= maxDepth;
        this.maxDepth = maxDepth;
        this.pointsFound = pointsFound;
    }

    @Override
    public void run() {
        parentSplitter.split();

        if (parentSplitter.hasFoundLowestPoint()){
            if (allowThreadCreation){
                //createChildWorkers();
            }
            else{
                //SequentialConvexHull seq = new SequentialConvexHull()
            }
        }
    }
}
