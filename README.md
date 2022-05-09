# IN4030-oblig-5

## Introduction – what this report is about
This report is about finding the Convex Hull of a set of Points in a Plane: A Recursive Geometric Problem. It involves a detailed description of how my approach to find
such convex holes sequentially. After the sequential approach, I'll elaborate on how to parallelize this in order to increase the efficiency of the algorithm. The report will also
explain in detail how such parallelization impacts the algorithm in terms of speedup and descriptions to why the speedup increases or decreases.



## 2. User guide – how to run your program (short, but essential), include a very simple example.
#### Build the program
Build the program by running the command:
   ```
   mvn clean install -Dskiptests
   ```

#### Run the program:
Run the program by running the command:
```
java -cp target/IN4030-oblig-5-1.0-SNAPSHOT.jar src.Main <m> <n> <seed> <threads>
```
- m - The mode to run. Can be either 0 (sequential), 1 (parallel) or 2 (benchmarking)
- n - The program will generate n amount of random numbers. 0 means it will generate for these numbers: 1000, 10 000,
  100 000, 1 mill, 10 mill og 100 mill
- seed - Seed to generating random numbers
- threads - how many threads to use in the parallel sorting. 0 means to use your computer's amount of cores.

#### Examples of running:
Run sequential algorithm for 100 000 points using seed 3 and all cores of the computer
```
java -cp target/IN4030-oblig-4-1.0-SNAPSHOT.jar src.MainParallelSort 100000 3 8 0
```

Running tests for the numbers 1000, 100 000, 1 mill etc.
```
java -cp target/IN4030-oblig-4-1.0-SNAPSHOT.jar src.MainParallelSort 0 3 8 0
```

### Finding the convex hull

We start by finding finding the two points which we see are obvious in the convex hull: the point with lowest x and the point with highest x.
If there's duplicates with same value, we just choose one.


Each line can be written as ax + by + c = 0. The line equation means that all points on this line satisfy the equation

Distance from line to other points
Check the docs regarding measuring the distance from a line to another point. If the point is on the left side of the line, it's positive & if it's onr hte right side of the line it's negative.
The further away from the line it is, the higher the number is.