package util

import scala.util.Random

import scala.language.postfixOps

class Partial(n: Int){
    /** Array holding the digits played so far, with 0 representing a blank
     * square.
     * n must be the square of a natural number. */
    private val contents = Array.ofDim[Int](n*n, n*n)

    /** Initialise from a file. */
    def init(fname: String, generate: String) = {
      if(generate=="--true"){
        for(i <- 0 until n*n){
            for(j <- 0 until n*n){
                val fillIn = j/n * n  + i/n  + 1 + n * (i - i/n * n) + (j - j/n * n)
                if(fillIn > n*n){
                    contents(i)(j) = fillIn - n*n
                }
                else{
                    contents(i)(j) = fillIn
                }
            }
        }
        val myRandomList = Random.shuffle(0 until n*n toList).take((scala.math.log(n*n)/scala.math.log(2)).toInt)
        for(i <- 0 until myRandomList.length){
            val blockStartsAtI = myRandomList(i) / n 
            val blockStartsAtJ = myRandomList(i) % n
            for(j <- n*blockStartsAtI until n*blockStartsAtI + n){
                for(k <- n*blockStartsAtJ until n*blockStartsAtJ + n){
                  contents(j)(k) = 0
                }
            }
        }
      }
      else if(generate=="--false"){
          val lines = scala.io.Source.fromFile(fname).getLines
          for(i <- 0 until n*n){
              val line = lines.next
              for(j <- 0 until n*n){
                  val c = line.charAt(2*j)
                  if(c.isDigit) contents(i)(j) = c.asDigit
                  else { assert(c=='.'); contents(i)(j) = 0 }
              }
          }
      }
      else{
          println("We start from an empty grid!")
      }
    }

    /** Print. */
    def printPartial = {
        for(i <- 0 until n*n){
          for(j <- 0 until n*n) print(contents(i)(j) + " ") 
          println 
        }
        println
    }

    /** Is the partial solution complete? */
    def complete : Boolean = {
        for(i <- 0 until n*n; j <- 0 until n*n) if(contents(i)(j) == 0) return false
        true
    }


    /** Precondition: check if there is a blank position in the grid provided. 
     *  If not, the sudoku grid is solved already provided it is valid. 
     *  See precondition below. */
    def nextPos: (Int,Int) = {
        for(i <- 0 until n*n; j <- 0 until n*n) if(contents(i)(j) == 0) return (i,j)
        throw new RuntimeException("No blank position")
    }

    /** Can we play value d in position (i,j); precondition: (i,j) is blank. */
    def canPlay(i:Int, j:Int, d:Int): Boolean = {
        // Check if d appears in row i
        for(j1 <- 0 until n*n) if(contents(i)(j1) == d) return false
        // Check if d appears in column j
        for(i1 <- 0 until n*n) if(contents(i1)(j) == d) return false
        // Check if d appears in this 3x3 block
        val basei = i/n*n; val basej = j/n*n
        for(i1 <- basei until basei+n; j1 <- basej until basej+n)
            if(contents(i1)(j1) == d) return false
        // All checks passed
        true
    }



    /** Create a new partial solution, extending this one by playing d in
     * position (i,j). */
    def play(i:Int, j:Int, d:Int) : Partial = {
        val p = new Partial(n)
        for(i1 <- 0 until n*n; j1 <- 0 until n*n)
            p.contents(i1)(j1) = contents(i1)(j1)
        p.contents(i)(j) = d
        p
    }
}


