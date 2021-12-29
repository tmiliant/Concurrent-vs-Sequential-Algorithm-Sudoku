package util

trait Graph[N]{
    /** The successors of node n. */
    def succs(n: N): List[N]
}

