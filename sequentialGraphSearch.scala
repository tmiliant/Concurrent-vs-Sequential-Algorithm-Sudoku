package util

import scala.collection.mutable.{Queue, Stack}

abstract class GraphSearch[N](g: Graph[N]){
    /** Perform a depth-first search in g, starting from start, for a node that
     * satisfies isTarget. */
    def apply(start: N, isTarget: N => Boolean): Option[N]
}

class SeqGraphSearch[N](g: Graph[N]) extends GraphSearch[N](g){
    /** Perform a depth-first search in g, starting from start, for a node that
     satisfies isTarget.  This performs a tree-search, not storing the set of
     nodes seen previously. */
    def apply(start: N, isTarget: N => Boolean): Option[N] = {
        // Stack storing nodes
        val stack = new Stack[N](); stack.push(start)

        while(stack.nonEmpty){
            val n = stack.pop
            for(n1 <- g.succs(n)){
                if(isTarget(n1)) return Some(n1) else stack.push(n1)
            }
        }
        None
    }
}
