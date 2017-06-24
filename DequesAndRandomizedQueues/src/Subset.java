/******************************************************************************
 *  Compilation:  javac-algs4 Subset.java
 *  Execution:    java-algs4-algs4 Subset input_integer
 *  Dependencies: StdIn.java StdOut.java RandomizedQueue.java
 *  
 *  Randomized subset of an input sequence
 *
 * % echo A B C D E F G H I | java-algs4 Subset 3
 * B
 * A
 * E
 * % echo A B C D E F G H I | java-algs4 Subset 3
 * D
 * F
 * E
 *****************************************************************************/

import edu.princeton.cs.algs4.StdIn; 
import edu.princeton.cs.algs4.StdOut;

/**
 *  Takes a command-line integer k; reads in a sequence of N strings from 
 *  standard input and prints out exactly k of them, uniformly at random;
 *  Each item is printed out at most once. 
 *
 *  @author Michael Tutterrow
 *
 */
public class Subset {
   public static void main(String[] args) {
        RandomizedQueue<String> stack = new RandomizedQueue<String>();
        int k = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            stack.enqueue(item);
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(stack.dequeue());
        }
   }
}
