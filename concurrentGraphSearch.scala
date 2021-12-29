package util


import io.threadcso._

import scala.collection.mutable.{Queue, Stack}


import util.TerminatingPartialStack

abstract class GraphSearch2[N](g: Graph[N]){
    /** Perform a depth-first search in g, starting from start, for a node that
     * satisfies isTarget. */
    def apply(start: N, isTarget: N => Boolean): Option[List[N]]
}



class ConcGraphSearch[N](g: Graph[N], workers: Int) extends GraphSearch2[N](g){
    /**The number of workers. */
    val numWorkers = workers
    /** Try to find a path in g from start to a node that satisfies isTarget. */
    

    def apply(start: N, isTarget: N => Boolean): Option[List[N]] = {

        // All nodes seen so far.
        //val seen = new ConcSet[N]; seen.add(start)

        // Stack storing nodes and the path leading to that node
        val stack = new TerminatingPartialStack[(N, List[N])](numWorkers)
        stack.push((start, List(start)))

        // Channel on which a worker tells the coordinator that it has found a
        // solution.
        val pathFound = ManyOne[List[N]]

        // A single worker
        def worker = proc("worker"){
            repeat{
                val (n, path) = stack.pop
                for(n1 <- g.succs(n)){
                    if(isTarget(n1)) pathFound!(path :+ n1) // done!
                    else stack.push((n1, path :+ n1))
                    //else if(seen.add(n1)) stack.push((n1, path :+ n1))
                }
            }
            pathFound.close // causes coordinator to close down
        }

        // Variable that ends up holding the result; written by coordinator.
        var result: Option[List[N]] = None

        def coordinator = proc("coordinator"){
            attempt{ result = Some(pathFound?()) }{ }
            stack.shutdown // close stack; this will cause most workers to terminate
            pathFound.close // in case another thread has found solution
        }

        val workers = || (for(_ <- 0 until numWorkers) yield worker)
        run(workers || coordinator)
        result
    }
}
