package util

import util.Partial
import util.Graph

/** A graph corresponding to Sudoku problems.  From each node p of the graph,
 * there is a path to every solution of p.  Further, there is at most one
 * path between any pair of nodes p1 and p2. */
class SudokuGraph(n: Int) extends Graph[Partial]{
    /** The successors of a particular partial solution.
     *
     * It is guaranteed that any solution of p is also a solution of a member
     * of succs(p), and vice versa.  Further, each element of succs(p) has
     * fewer blank squares than p.
     *
     * Pre: !p.complete. */
    def succs(p: Partial): List[Partial] = {
        val (i,j) = p.nextPos
        (for(d <- 1 to n*n; if p.canPlay(i, j, d)) yield p.play(i, j, d)).toList
    }
}
