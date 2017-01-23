/******************************************************************************
 *  Compilation:  javac Deque.java
 *  Execution: java Deque < input.txt
 *  Dependencies: none
 *  @author: Alex Erhardt
 * 
 *  Princeton Algorithms Part I on Coursera - Week 3 programming assignment
 * 
 *  Creates and tests a deque.
 * 
 *  Grader output: 99 / 100
 *  
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;

public class Deque<Item> implements Iterable<Item> {
    private int numberOfItems;
    private Node head;
    private Node tail;
    
    private class Node {
        private Item storedItem;
        private Node previous;
        private Node next;
    }
    
    // construct an empty deque
    public Deque() {
        numberOfItems = 0;
        head = new Node();
        tail = new Node();
        head.storedItem = null;
        head.previous = null;
        head.next = tail;
        tail.storedItem = null;
        tail.previous = head;
        tail.next = null;
    }
    
    // is the deque empty?
    public boolean isEmpty() {
        return numberOfItems == 0;
    }
    
    // return the number of items on the deque
    public int size() {
        return numberOfItems;
    }
   
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        Node first = new Node();
        Node second = head.next;
        first.storedItem = item;
        first.next = second;
        first.previous = head;
        head.next = first;
        second.previous = first;
        numberOfItems++;
    }
    
    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        Node secondToLast = tail.previous;
        Node last = new Node();
        last.storedItem = item;
        last.next = tail;
        last.previous = secondToLast;
        secondToLast.next = last;
        tail.previous = last;
        numberOfItems++;
    }
    
    // remove and return the item from the front
    public Item removeFirst() {
        if (numberOfItems == 0) {
            throw new NoSuchElementException("Cannot remove from empty deque");
        }
        Node oldFirst = head.next;
        Item item = oldFirst.storedItem;
        Node newFirst = oldFirst.next;
        head.next = newFirst;
        newFirst.previous = head;
        oldFirst.storedItem = null;
        oldFirst.next = null;
        oldFirst.previous = null;
        numberOfItems--;
        return item;
    }
    
    // remove and return the item from the end
    public Item removeLast() {
        if (numberOfItems == 0) {
            throw new NoSuchElementException("Cannot remove from empty deque");
        }
        Node oldLast = tail.previous;
        Item item = oldLast.storedItem;
        Node newLast = oldLast.previous;
        tail.previous = newLast;
        newLast.next = tail;
        oldLast.storedItem = null;
        oldLast.next = null;
        oldLast.previous = null;
        numberOfItems--;
        return item;
    }
    
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator();
    }
    
    private class ListIterator implements Iterator<Item> {
        private Node current = head.next;
        public boolean hasNext() { return current.next != null; }
        public void remove() { throw new UnsupportedOperationException(); }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.storedItem;
            current = current.next;
            return item;
        }
    }
 
    
    // unit testing
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        
        // Builds linked list
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            deque.addLast(item);
            // deque.addFirst(item);
        }
        
        // Prints current state of linked list
        Deque<String>.Node current = deque.new Node();
        current = deque.head.next;
        int nodeCounter = 1;
        System.out.println("Custom Iterator test: ");
        System.out.println("Head Item: " + deque.head.storedItem);
        while (current.next != null) {
            System.out.println("Item #" + nodeCounter + ": " + current.storedItem);
            current = current.next;
            nodeCounter++;
        }
        System.out.println("Tail Item: " + deque.tail.storedItem);
        
        // Iterator test
        System.out.println("Iterator test: ");
        for (String s : deque) System.out.println(s);
        
        // Prints removal
        while (deque.head.next.next != null) {
            // System.out.println("Removed: " + deque.removeFirst() + " ");
            System.out.println("Removed: " + deque.removeLast() + " ");
        }
    }   
}
