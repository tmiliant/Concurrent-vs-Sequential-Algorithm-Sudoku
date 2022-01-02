# Concurrent algorithm for Sudoku and comparison with sequential algorithm

In this repository I include a sequential algorithm for Sudoku as well as a concurrent algorithm for it. The general problem is known to be complete for the complexity class NP. I am using Scala. In particular, I am using the skills learned in the Concurrent Programming course taught in the University of Oxford CS Department. I am availing myself of the CSO library, the one I learned concurrent programming with, and please see https://www.cs.ox.ac.uk/people/bernard.sufrin/personal/CSO/ for a reference. This is a relatively complex object oriented architecture which offers great modularity, so the code is easily maintained and updated. 

## Approach followed in the analysis of experiments

For the purposes of this research experiment, I will always start with a sudoku grid for which there is a solution. The way I do this is by first building a complete solution and then empty-ing certain cells of the grid. Since before empty-ing those cells the solution was complete and valid, it follows that there is at least one solution to the puzzle even if we start with the grid which misses some cells. I will not worry about grids whose complete solution does not exist.

## How I generate full valid grids, generalizable for n^2 x n^2 grids

Observe the following pattern is valid for the case of 9x9 grids. This pattern that comes from the idea of permutations is easily generalizable to the n^2 x n^2 case. 

1 2 3 4 5 6 7 8 9

4 5 6 7 8 9 1 2 3

7 8 9 1 2 3 4 5 6 

2 3 4 ...

.

.

.


## How I build the initial grid from the full grid


If I start with an empty grid, I get a heap memory exception for n >= 5. Hence it is better to start from a state where as few cells are empty as possible, and in the same time there should be enough empty cells so that my experiments are relevant. I am building the full valid solution for the general case n, following the idea in the diagram, and then randomly making log(n^2) blocks empty, where log is in base 2. Note n^2 is the number of blocks in the grid. The logarithm is chosen because the complexity of the algorithm is exponential - worst case is O(k^m) where k is the number of possible choices at each step and m is the number of empty cells - hence to compensate for that growth I take the inverse of the exponential as the growth rate of the number of empty blocks. 

The more sparsely generated will the soon-emptied blocks be, the better. In rare cases, they get sampled in the same connected component in the graph formed by the empty nodes, and I am providing the analysis for the cases when this does not happen. Most of the time we do not get these rare cases, which take more than 5 minutes to solve, compared to less than one second as one of the .png file shows. This is enough for the purposes of these experiments.

## How I choose the number of workers

I am running the algorithm with different values for the number of workers, each time for the fixed empty 9x9 grid. According to the plot, I choose to use 4 workers. My laptop has 4 cores, so I am not surprised by the plot. Just for fun, I also tried 1000 workers, which dramatically increases the runtime relatively, and this is due to the complexity incurred by the latency in the communications between these processes. This trade-off is indeed observed in the plot: initially, the complexity due to message passing between the processes is almost non-existent, whereas as the number of processes increases, this complexity will outweigh the benefits that concurrency is supposed to offer.

<img src = "https://user-images.githubusercontent.com/58377307/147744960-d3e224a8-4d04-43a5-8a96-a2aaafa0acc8.jpg" height="400" width="500">

## Result

The concurrent algorithm performs worse than the sequential algorithm, presumably because this implementation uses a stack. Even when the stack is not explicit, the recursive calls make the stack implicit, because the calls of the recursions lie on the recursion stack - c.f. backtracking algorithms. A stack is inherently hard to parallelize, because the threads will have to work on that same stack and there will be a lot of latency because of that. Since this implementation uses a concurrent stack, it is most likely  because of this that the algorithm does not improve upon its sequential counterpart. 

<img src = "https://user-images.githubusercontent.com/58377307/147743736-6565f2a4-d154-4bdb-b240-2ee9568b7367.jpg" height="300" width="600">

## Running the code

scala Sudoku.scala test.sud <sudoku instance size like 2,3,...,8> --true --conc <number of workers>
