import util.Partial
import util.SudokuGraph
import util.ConcGraphSearch
import util.SeqGraphSearch
import util.GraphSearch
import util.GraphSearch2


object Sudoku{
    def main(args: Array[String]) = {
        val fname = args(0)
        val n = args(1).toInt
        val useConc = args.length > 2 && args(2) == "--conc" 
        val generate = args(3)
        val numWorkers = args(4).toInt
        val p = new Partial(n); p.init(fname, generate)
        val g = new SudokuGraph(n)
        val solver1: GraphSearch2[Partial] = new ConcGraphSearch(g, numWorkers)
        val solver2: GraphSearch[Partial] = new SeqGraphSearch(g)

        val t1 = System.nanoTime
        solver1(p, _.complete) match{
            case Some(p1) => println("conc started")
            case None => println("No solution found")
        }
        val duration1 = (System.nanoTime - t1) / 1e9d
        println(duration1)
        println 

        val t2 = System.nanoTime
        solver2(p, _.complete) match{
            case Some(p1) => println("seq started")
            case None => println("No solution found")
        }
        val duration2 = (System.nanoTime - t2) / 1e9d
        print(duration2)
        println
        io.threadcso.exit

    }
}
