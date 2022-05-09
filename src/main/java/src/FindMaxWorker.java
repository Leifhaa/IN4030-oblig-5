package src;

public class FindMaxWorker implements Runnable {
    public int searchFromIndex;
    public int searchToIndex;
    private ConvexHull chart;
    private int minCandidate;
    private int minValue;
    private int maxCandidate;
    private int maxValue;


    public FindMaxWorker(int searchFromIndex, int searchToIndex, ConvexHull hull) {
        this.searchFromIndex = searchFromIndex;
        this.searchToIndex = searchToIndex;
        this.chart = hull;
    }

    public int getMinCandidate() {
        return minCandidate;
    }

    public int getMaxCandidate() {
        return maxCandidate;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    @Override
    public void run() {
        minValue = chart.x[0];
        maxValue = minValue;

        for (int i = searchFromIndex; i < searchToIndex; i++){
            if (chart.x[i] > maxValue){
                maxValue = chart.x[i];
                maxCandidate = i;
            }

            if (chart.x[i] < minValue){
                minValue = chart.x[i];
                minCandidate = i;
            }
        }
    }
}
