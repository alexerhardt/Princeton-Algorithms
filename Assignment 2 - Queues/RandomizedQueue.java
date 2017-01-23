/******************************************************************************
 *  Compilation:  javac RandomizedQueue.java
 *  Execution: java RandomizedQueue < input.txt
 *  Dependencies: none
 *  @author: Alex Erhardt
 * 
 *  Princeton Algorithms Part I on Coursera - Week 3 programming assignment
 * 
 *  Creates and tests a randomized queue.
 * 
 *  Grader output: 99 / 100
 *  
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] array;
    private int n;
    
    // construct an empty randomized queue
    public RandomizedQueue() {
        array = (Item[]) new Object[2];
        n = 0;
    }
    
    // is the queue empty?
    public boolean isEmpty() {
        return n == 0;
    }
   
    // return the number of items on the queue
    public int size() {
        return n;
    }
    
    private void resize(int capacity) {
        assert capacity >= n;
        
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = array[i];
        }
        array = temp;
    }
    
    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (n == array.length) {
            resize(2 * array.length);
        }
        array[n++] = item;
    }          
    
    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack underflow");
        }
        int randomIndex = StdRandom.uniform(n);
        Item item = array[randomIndex];
        array[randomIndex] = array[n-1];
        array[n-1] = null;
        n--;
        if (n == array.length / 4 && n > 0) {
            resize(array.length / 2); 
        }
        return item;
    }                    
   
    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack underflow");
        }
        return array[StdRandom.uniform(n)];
    }
    
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedArrayIterator();
    }
    
    private class RandomizedArrayIterator implements Iterator<Item> {
        private Item[] randomizedArray;
        private int i;
        
        public RandomizedArrayIterator() {
            if (isEmpty()) {
                throw new NoSuchElementException();
            }
            randomizedArray = (Item[]) new Object[n];
            randomizedArray = Arrays.copyOf(array, n);
            StdRandom.shuffle(randomizedArray);
            i = 0;
        }
        
        public boolean hasNext() {
            return i < n;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return randomizedArray[i++];
        }
    }
   
   
    // unit testing
    public static void main(String[] args) {
        RandomizedQueue<String> rando = new RandomizedQueue<String>();
        
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            rando.enqueue(item);
        }
        
        Object[] arr = rando.array;
        
        System.out.println("Number of items: " + rando.n);
        System.out.println("Array size: " + arr.length);
        
        System.out.println("Array state test");
        for (int i = 0; i < rando.n; i++) {
            System.out.println("Item #" + i + ": " + arr[i]);
        }
        
        System.out.println("Random sampling test");
        for (int i = 0; i < 15; i++) {
            System.out.println("Random sample: " + rando.sample());
        }
        
        System.out.println("Iterator test");
        for (String s : rando) System.out.println(s);
        
        System.out.println("Dequeue test");
        while (!rando.isEmpty()) {
            System.out.println("Dequeued item: " + rando.dequeue());
        }
    }   
}
