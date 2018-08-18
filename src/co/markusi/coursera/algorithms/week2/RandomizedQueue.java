package co.markusi.coursera.algorithms.week2;

import edu.princeton.cs.algs4.StdRandom;

import java.util.*;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int size;
    private int nextArrayIndex;

    // construct an empty randomized queue
    public RandomizedQueue() {
        nextArrayIndex = 0;
        size = 0;
        items = (Item[]) new Object[2];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        validateInput(item);
        if (nextArrayIndex == items.length) {
            resizeArray();
        }
        items[nextArrayIndex] = item;
        nextArrayIndex++;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        checkIfEmpty();
        int index = getRandomIndex();
        Item item = items[index];
        items[index] = null;

        size--;

        if (size == items.length / 4) {
            resizeArray();
        }

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        checkIfEmpty();
        int index = getRandomIndex();
        return items[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {

            private Item[] items = shuffleItems();
            private int offset = 0;

            private Item[] shuffleItems() {
                Item[] itemsCopy = (Item[]) new Object[size];
                int i = 0;
                for (Item item : RandomizedQueue.this.items) {
                    if (item != null) {
                        itemsCopy[i] = item;
                        i++;
                    }
                }
                StdRandom.shuffle(itemsCopy);
                return itemsCopy;
            }

            @Override
            public boolean hasNext() {
                return offset < items.length;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more items to return.");
                }
                return items[offset++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove() not supported.");
            }
        };
    }

    private void validateInput(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null items not supported in this implementation of randomized queue.");
        }
    }

    private void checkIfEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("The randomized deque is empty.");
        }
    }

    private void resizeArray() {
        int newCapacity = size * 2;
        Item[] newItems = (Item[]) new Object[newCapacity];

        int i = 0;
        for (Item item : items) {
            if (item != null) {
                newItems[i] = item;
                i++;
            }
        }
        items = newItems;
        nextArrayIndex = size;
    }

    private int getRandomIndex() {
        int index = StdRandom.uniform(items.length);
        while (items[index] == null) {
            index = StdRandom.uniform(items.length);
        }
        return index;
    }


    // unit testing (optional)
    public static void main(String[] args) {
        testOneEnqueue();
        testEnqueuesWhichExpandArray();
        testSample();
        testDequeues();
        testOneDeque();
        testDequeuesWhichShrinkArray();
        testIterator();
    }

    private static void testOneEnqueue() {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue(1);

        List<Integer> items = Arrays.asList(randomizedQueue.items);
        assert items.get(0) == 1;
        assert randomizedQueue.size() == 1;
        assert randomizedQueue.nextArrayIndex == 1;
        assert items.size() == 2;
    }

    private static void testEnqueuesWhichExpandArray() {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue(1);
        randomizedQueue.enqueue(2);
        randomizedQueue.enqueue(3);

        List<Integer> items = Arrays.asList(randomizedQueue.items);
        assert randomizedQueue.size() == 3;
        assert items.get(3) == null;
        assert randomizedQueue.nextArrayIndex == 3;
        assert items.size() == 4;

        randomizedQueue.enqueue(4);
        randomizedQueue.enqueue(5);

        items = Arrays.asList(randomizedQueue.items);
        assert randomizedQueue.size() == 5;
        for (int i = 1; i <= 5; i++) {
            assert items.get(i - 1) == i;
        }
        for (int j = 5; j < 8; j++) {
            assert items.get(j) == null;
        }
        assert randomizedQueue.nextArrayIndex == 5;
        assert items.size() == 8;
    }

    private static void testSample() {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue(1);
        randomizedQueue.enqueue(2);
        randomizedQueue.enqueue(3);
        randomizedQueue.enqueue(4);
        randomizedQueue.enqueue(5);

        List<Integer> samples = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            samples.add(randomizedQueue.sample());
        }
        int first = samples.get(0);
        boolean allTheSame = true;
        for (int sample : samples) {
            if (sample != first) {
                allTheSame = false;
            }
        }
        assert !allTheSame;
    }

    private static void testDequeues() {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue(1);
        randomizedQueue.enqueue(2);
        randomizedQueue.enqueue(3);
        randomizedQueue.enqueue(4);
        randomizedQueue.enqueue(5);

        List<Integer> dequeuedItems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dequeuedItems.add(randomizedQueue.dequeue());
        }
        List<Integer> items = Arrays.asList(randomizedQueue.items);
        Set<Integer> set = new HashSet<>(dequeuedItems);
        assert items.size() == 0;
        assert dequeuedItems.size() == 5;
        assert set.size() == 5;
    }

    private static void testOneDeque() {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue(1);

        int item = randomizedQueue.dequeue();
        assert item == 1;
        assert randomizedQueue.size == 0;
        assert randomizedQueue.isEmpty();
    }

    private static void testDequeuesWhichShrinkArray() {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue(1);
        randomizedQueue.enqueue(2);
        randomizedQueue.enqueue(3);
        randomizedQueue.enqueue(4);
        randomizedQueue.enqueue(5);

        List<Integer> items = Arrays.asList(randomizedQueue.items);
        assert randomizedQueue.size() == 5;
        assert randomizedQueue.nextArrayIndex == 5;
        assert items.size() == 8;

        randomizedQueue.dequeue();
        randomizedQueue.dequeue();

        items = Arrays.asList(randomizedQueue.items);
        assert randomizedQueue.size() == 3;
        assert randomizedQueue.nextArrayIndex == 5;
        assert items.size() == 8;

        randomizedQueue.dequeue();

        items = Arrays.asList(randomizedQueue.items);
        assert randomizedQueue.size() == 2;
        assert randomizedQueue.nextArrayIndex == 2;
        assert items.size() == 4;
    }

    private static void testIterator() {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue(1);
        randomizedQueue.enqueue(2);
        randomizedQueue.enqueue(3);
        randomizedQueue.enqueue(4);
        randomizedQueue.enqueue(5);

        List<Integer> items = new ArrayList<>();
        for (int item : randomizedQueue) {
            items.add(item);
        }
        Set<Integer> iterated = new HashSet<>(items);

        assert iterated.size() == randomizedQueue.size();

        for (int item1 : randomizedQueue) {
            for (int item2 : randomizedQueue) {
            }
        }
    }
}