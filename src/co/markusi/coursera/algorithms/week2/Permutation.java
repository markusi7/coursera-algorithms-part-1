package co.markusi.coursera.algorithms.week2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String input = StdIn.readString();
            randomizedQueue.enqueue(input);
        }
        Iterator<String> iterator = randomizedQueue.iterator();
        int i = 0;
        while (iterator.hasNext() && i < k) {
            String item = iterator.next();
            StdOut.println(item);
            i++;
        }
    }
}
