package co.markusi.coursera.algorithms.week2;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private final SentinelNode<Item> sentinelNode;
    private int size;

    // construct an empty deque
    public Deque() {
        sentinelNode = new SentinelNode<>();
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        validateInput(item);
        Node<Item> newNode = new Node<>(item);
        newNode.next = sentinelNode.first;
        if (sentinelNode.first != null) {
            sentinelNode.first.previous = newNode;
        }
        sentinelNode.first = newNode;
        if (sentinelNode.last == null) {
            sentinelNode.last = newNode;
        }
        size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        validateInput(item);
        Node<Item> newNode = new Node<>(item);
        newNode.previous = sentinelNode.last;
        if (sentinelNode.last != null) {
            sentinelNode.last.next = newNode;
        } else {
            sentinelNode.first = newNode;
        }
        sentinelNode.last = newNode;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkIfEmpty();
        Item item = sentinelNode.first.item;
        sentinelNode.first = sentinelNode.first.next;
        if (sentinelNode.first != null) {
            sentinelNode.first.previous = null;
        }
        size--;
        if (size == 0) {
            sentinelNode.last = null;
        }
        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        checkIfEmpty();
        Item item = sentinelNode.last.item;
        sentinelNode.last = sentinelNode.last.previous;
        if (sentinelNode.last != null) {
            sentinelNode.last.next = null;
        }
        size--;
        if (size == 0) {
            sentinelNode.first = null;
        }
        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node<Item> current = sentinelNode.first;
            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more items to return.");
                }
                Item item = current.item;
                current = current.next;
                return item;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove() not supported.");
            }
        };
    }

    private void validateInput(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null items not supported in this implementation of deque.");
        }
    }

    private void checkIfEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("The deque is empty.");
        }
    }

    private static class Node<Item> {
        private final Item item;
        private Node<Item> next;
        private Node<Item> previous;

        Node(Item item) {
            this.item = item;
        }
    }

    private static class SentinelNode<Item> {
        private Node<Item> first;
        private Node<Item> last;
    }

    // unit testing (optional)
    public static void main(String[] args) {
        testAddFirst();
        testAddLast();
        testAddMix();
        testRemoveFirst();
        testRemoveLast();
        testRemoveMix();
        testIterator();
    }

    private static void testAddFirst() {
        Deque<Integer> deque = new Deque<>();

        assert deque.size() == 0;
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);

        Node<Integer> first = deque.sentinelNode.first;
        Node<Integer> second = first.next;
        Node<Integer> third = second.next;

        assert first.item == 3;
        assert first.previous == null;
        assert second.item == 2;
        assert second.previous == first;
        assert third.item == 1;
        assert third.previous == second;
        assert third.next == null;
        assert deque.size() == 3;
        assert !deque.isEmpty();
    }

    private static void testAddLast() {
        Deque<Integer> deque = new Deque<>();

        assert deque.size() == 0;
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);

        Node<Integer> first = deque.sentinelNode.first;
        Node<Integer> second = first.next;
        Node<Integer> third = second.next;

        assert first.item == 1;
        assert first.previous == null;
        assert second.item == 2;
        assert second.previous == first;
        assert third.item == 3;
        assert third.previous == second;
        assert third.next == null;
        assert deque.size() == 3;
        assert !deque.isEmpty();
    }

    private static void testAddMix() {
        Deque<Integer> deque = new Deque<>();

        assert deque.size() == 0;
        deque.addLast(1);
        deque.addLast(2);
        deque.addFirst(3);
        deque.addFirst(4);
        deque.addLast(5);

        Node<Integer> first = deque.sentinelNode.first;
        Node<Integer> second = first.next;
        Node<Integer> third = second.next;
        Node<Integer> fourth = third.next;
        Node<Integer> fifth = fourth.next;

        assert first.item == 4;
        assert first.previous == null;
        assert second.item == 3;
        assert second.previous == first;
        assert third.item == 1;
        assert third.previous == second;
        assert fourth.item == 2;
        assert fourth.previous == third;
        assert fifth.item == 5;
        assert fifth.previous == fourth;
        assert fifth.next == null;
        assert deque.size() == 5;
        assert !deque.isEmpty();
    }

    private static void testRemoveFirst() {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);

        int firstRemovedItem = deque.removeFirst();
        int secondRemovedItem = deque.removeFirst();
        int thirdRemovedItem = deque.removeFirst();

        assert firstRemovedItem == 3;
        assert secondRemovedItem == 2;
        assert thirdRemovedItem == 1;
        assert deque.size() == 0;
        assert deque.sentinelNode.first == null;
        assert deque.sentinelNode.last == null;
        assert deque.isEmpty();
    }

    private static void testRemoveLast() {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);

        int firstRemovedItem = deque.removeLast();
        int secondRemovedItem = deque.removeLast();
        int thirdRemovedItem = deque.removeLast();

        assert firstRemovedItem == 1;
        assert secondRemovedItem == 2;
        assert thirdRemovedItem == 3;
        assert deque.size() == 0;
        assert deque.sentinelNode.first == null;
        assert deque.sentinelNode.last == null;
        assert deque.isEmpty();
    }

    private static void testRemoveMix() {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(6);
        deque.addLast(5);
        deque.addLast(4);
        deque.addLast(3);
        deque.addLast(2);
        deque.addLast(1);

        int firstRemovedItem = deque.removeLast();
        int secondRemovedItem = deque.removeLast();
        int thirdRemovedItem = deque.removeFirst();
        int fourthRemovedItem = deque.removeFirst();
        int fifthRemovedItem = deque.removeLast();

        assert firstRemovedItem == 1;
        assert secondRemovedItem == 2;
        assert thirdRemovedItem == 6;
        assert fourthRemovedItem == 5;
        assert fifthRemovedItem == 3;
        assert deque.sentinelNode.first == deque.sentinelNode.last;
        assert deque.sentinelNode.first.item == 4;
        assert deque.size() == 1;
        assert !deque.isEmpty();
    }

    private static void testIterator() {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(6);
        deque.addFirst(5);
        deque.addFirst(4);
        deque.addFirst(3);
        deque.addFirst(2);
        deque.addFirst(1);

        int i = 1;
        for (int item: deque) {
            assert item == i;
            i++;
        }

        int j = 1;
        for (int item1: deque) {
            assert item1 == j;
            j++;
            int k = 1;
            for (int item2: deque) {
                assert item2 == k;
                k++;
            }
        }
    }
}