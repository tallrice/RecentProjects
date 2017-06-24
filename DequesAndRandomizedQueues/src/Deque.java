/***********************************************************************
 *  Compilation:  javac-algs4 Deque.java
 *  Execution:    java-algs4 Deque < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/13stacks/tobe.txt
 *
 *  A generic stack, implemented using a linked list. Each stack
 *  element is of type Item.
 *  
 *  % more tobe.txt 
 *  to be or not to - be - - that - - - is
 *
 *  % java-algs4 Deque < tobe.txt
 *  to be not that or be (2 left on stack)
 *
 **********************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn; 
import edu.princeton.cs.algs4.StdOut;
/**
 *  The {@code Deque} class represents a last-in-first-out (LIFO) stack
 *  of generic items.
 *  It supports <em>addFirst</em> ,<em>removeFirst</em>,
 *  <em>addLast</em>, and <em>removeLast</em>
 *  operation, along with methods
 *  for testing if the stack is empty, and
 *  iterating through the items from front to end.
 *  <p>
 *  This implementation uses a singly-linked list with a non-static
 *  nested class for linked-list nodes.
 *  Each operation takes constant time in the worst case.
 *  <p>
 *
 *  @author Michael Tutterrow
 *
 *  Based on:
 *  LinkedList.java:
 *  see <a href="http://algs4.cs.princeton.edu/code">
 *  Java Algorithms and Clients
 *  </a>
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Deque<Item> implements Iterable<Item> {
    private int n;          // size of the stack
    private DoubleNode first;     // front of stack
    private DoubleNode last;      // end of stack

    // helper linked list class
    private class DoubleNode {
        private Item item;
        private DoubleNode next;
        private DoubleNode prev;
    }

    /**
     * Initializes an empty stack.
     */
    public Deque() {
        first = null;
        last = null;
        n = 0;
        assert check();
    }

    /**
     * Is this stack empty?
     * @return true if this stack is empty; false otherwise
     */
    public boolean isEmpty() {
        return (first == null || last == null);
    }

    /**
     * Returns the number of items in the stack.
     * @return the number of items in the stack
     */
    public int size() {
        return n;
    }

    /**
     * Adds the item to the front of the stack.
     * @param item the item to add
     */
    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }
        DoubleNode oldfirst = first;
        first = new DoubleNode();
        first.item = item;
        first.next = oldfirst;
        first.prev = null;
        if (isEmpty())  last = first;
        else            oldfirst.prev = first;
        n++;
        assert check();
    }

    /**
     * Adds the item to the end of the stack.
     * @param item the item to add
     * @throws java.lang.NullPointerException if item is null
     */
    public void addLast(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }
        DoubleNode oldlast = last;
        last = new DoubleNode();
        last.item = item;
        last.next = null;
        last.prev = oldlast;
        if (isEmpty())  first = last;
        else            oldlast.next = last;
        n++;
        assert check();
    }

    /**
     * Removes and returns the item from the front
     * @return the item from the front
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = first.item;        // save item to return
        first = first.next;            // delete first node
        n--;
        if (isEmpty()) last = null;
        else first.prev = null;
        assert check();
        return item;                   // return the saved item
    }

    /**
     * Removes and returns the item from the end
     * @return the item from the end
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = last.item;        // save item to return
        last = last.prev;            // delete last node
        n--;
        if (isEmpty()) first = null;
        else last.next = null;
        assert check();
        return item;                   // return the saved item
    }

    /**
     * Returns an iterator to this stack that iterates through the items
     * from front to end
     * @return an iterator to this stack that iterates through the items
     * from front to end
     */
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> {
        private DoubleNode current = first;
        public boolean hasNext()  
        { return current != null;                     }
        public void remove()      
        { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }


    // check internal invariants
    private boolean check() {

        // check a few properties of instance variable 'first'
        if (n < 0) {
            return false;
        }
        if (n == 0) {
            if (first != null) return false;
        }
        else if (n == 1) {
            if (first == null)      return false;
            if (first.next != null) return false;
        }
        else {
            if (first == null)      return false;
            if (first.next == null) return false;
        }

        // check internal consistency of instance variable n
        int numberOfDoubleNodes = 0;
        for (DoubleNode x = first; x != null && numberOfDoubleNodes <= n; 
                x = x.next) {
            numberOfDoubleNodes++;
        }
        if (numberOfDoubleNodes != n) return false;

        return true;
    }

    /**
     * Unit tests the {@code Deque} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Deque<Integer> stack = new Deque<Integer>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                stack.addLast(Integer.parseInt(item));
            else if (!stack.isEmpty())
                StdOut.print(stack.removeLast() + " ");
        }
        StdOut.println("(" + stack.size() + " left on stack)");
    }
}
