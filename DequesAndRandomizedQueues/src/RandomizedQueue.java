/******************************************************************************
 *  Compilation:  javac-algs4 RandomizedQueue.java
 *  Execution:    java-algs4 RandomizedQueue < input.txt
 *  Dependencies: StdIn.java StdOut.java StdRandom
 *  Data files:   http://algs4.cs.princeton.edu/13stacks/tobe.txt
 *  
 *  Stack implementation with a randomized resizing array.
 *
 *  % more tobe.txt 
 *  to be or not to - be - - that - - - is
 *
 *  % java-algs4 RandomizedQueue < tobe.txt
 *  to to be not be that (2 left on stack)
 *  % java-algs4 RandomizedQueue < tobe.txt
 *  to or to be not be (2 left on stack)
 *  % java-algs4 RandomizedQueue < tobe.txt
 *  to or be be to not (2 left on stack)
 *
 *****************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn; 
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 *  The {@code RandomizedQueue} class represents a randomized stack
 *  of generic items.
 *  It supports the usual <em>enqueue</em> and <em>dequeue</em> 
 *  operations, along with methods
 *  for sampling at the top item, testing if the stack is empty, and 
 *  iterating through the items in random order.
 *  <p>
 *  This implementation uses a resizing array, which double the 
 *  underlying array when it is full and halves the 
 *  underlying array when it is one-quarter full.
 *  The <em>enqueue</em> and <em>dequeue</em> operations take 
 *  constant amortized time.
 *  The <em>size</em>, <em>sample</em>, and <em>is-empty</em> 
 *  operations takes constant time in the worst case. 
 *  The iterator operations <em>next</em> and <em>hasNext</em> take
 *  constant worst-case time.
 *  The iterator construction takes linear time.
 *  Each iterator uses a linear amount of extra memory.
 *  <p>
 *
 *  @author Michael Tutterrow
 *
 *  Based on:
 *  ResizingArrayStack.java:
 *  see <a href="http://algs4.cs.princeton.edu/code">
 *  Java Algorithms and Clients
 *  </a>
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;         // array of items
    private int n;            // number of elements on stack


    /**
     * Initializes an empty stack.
     */
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        n = 0;
    }

    /**
     * Is this stack empty?
     * @return true if this stack is empty; false otherwise
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Returns the number of items in the stack.
     * @return the number of items in the stack
     */
    public int size() {
        return n;
    }


    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= n;

        // textbook implementation
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;

       // alternative implementation
       // a = java.util.Arrays.copyOf(a, capacity);
    }



    /**
     * Adds the item to this stack.
     * @param item the item to add
     * @throws java.lang.NullPointerException if item is null
     */
    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException(
                "item added to the stack cannot be null");
        if (n == a.length) resize(2*a.length);    // double size of array 
                                                  // if necessary
        a[n++] = item;                            // add item
    }

    /**
     * Returns a random item from stack
     * @return a random item
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int b = StdRandom.uniform(n);
        Item item = a[b];   // get the random item to return
        a[b] = a[n-1];      // replace the random item with last item
        a[n-1] = null;      // to avoid loitering
        n--;
        // shrink size of array if necessary
        if (n > 0 && n == a.length/4) resize(a.length/2);
        return item;
    }


    /**
     * Returns (but does not remove) a random item from stack
     * @return a random item
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int b = StdRandom.uniform(n);
        Item item = a[b];   // get the random item to return
        return item;
    }

    /**
     * Returns an iterator to this stack that iterates 
     * through the items in random order.
     * @return an iterator to this stack that iterates 
     * through the items in random order.
     */
    public Iterator<Item> iterator() {
        return new RandomArrayIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class RandomArrayIterator implements Iterator<Item> {
        private int i;
        private Item[] c;
    
        public RandomArrayIterator() {
            i = n-1;
            c = (Item[]) new Object[n];
            for (int j = 0; j < n; j++) {
                c[j] = a[j];
            }
            StdRandom.shuffle(c);
        }

        public boolean hasNext() {
            return i >= 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return c[i--];
        }
    }


    /**
     * Unit tests the {@code Stack} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        RandomizedQueue<String> stack = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) stack.enqueue(item);
            else if (!stack.isEmpty()) StdOut.print(stack.dequeue() + " ");
        }
        StdOut.println("(" + stack.size() + " left on stack)");
    }
}
