# Concurrent algorithm for Sudoku and comparison with sequential algorithm

In this repository I include a sequential algorithm for Sudoku as well as a concurrent algorithm for it. I am using Scala. In particular, I am using the skills learned in the Concurrent Programming course taught in the University of Oxford CS Department. Additional to my own submitted homework with regards to this problem, I am providing an extended analysis about different ways one could solve this problem concurrently. I am availing myself of the CSO library, the one I learned concurrent programming with, and please see https://www.cs.ox.ac.uk/people/bernard.sufrin/personal/CSO/ for a reference.

I am generating random random intermediate states from the valid solution

1 2 3 4 5 6 7 8 9

4 5 6 7 8 9 1 2 3

7 8 9 1 2 3 4 5 6 

2 3 4 ...

.

.

.

If I start with an empty grid, I get a heap memory exception for n >= 5. This is why for big n I am building the full valid solution for the general case n, following the idea in the diagram, and then randomly making log(n^2) blocks empty, where log is in base 2. 

According to the plot, I am using 4 workers. My laptop has 4 cores, so I am not surpised. 

The more sparsely generated will the soon-emptied blocks be, the better. In rare cases, they get sampled in the same connected component in the graph formed by the empty nodes, and I am providing the analysis for the cases when this does not happen. Most of the time we do not get these rare cases, which take more than 5 minutes to solve, compared to less than one second as one of the .png file shows. This is enough for the purposes of these experiments.

The concurrent algorithm performs worse than the sequential algorithm, presumably because this implementation uses a stack. Even if the stack is not explicit, the recursive calls make the stack implicit, because of the calls of the recursions lie on the recursion stack. A stack is inherently hard to parralelize, because the threads will have to work on that stack and there will be a lot of latency because of that. Since this implementation uses a concurrent stack, it is most likely  because of this that the algorithm does not improve upon its sequential counterpart. 

## Running the code

scala Sudoku.scala test.sud <sudoku instance size> --<true/false/empty> --conc <number of workers>
