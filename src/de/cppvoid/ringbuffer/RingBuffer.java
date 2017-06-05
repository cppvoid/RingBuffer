package de.cppvoid.ringbuffer;

import java.util.ArrayList;
import java.util.List;

public class RingBuffer<T> {
	private final List<T> data;
	private int head;
	private int tail;
	private int count = 0;
	private final int size;

	public RingBuffer(int size) {
		this.size = size;
		data = new ArrayList<>(size);

		// Initialize the list that the set function works
		for (int i = 0; i < size; ++i) {
			data.add(null);
		}
	}

	public synchronized void put(T object) throws InterruptedException {
		while (isFull()) {
			try {
				wait();
			} catch (InterruptedException e) {
				throw new InterruptedException("Interupted while trying to put " + object.getClass().getName());
			}
		}
		notifyAll();

		data.set(head, object);

		if (head == size - 1) { // Set the head to the begin
			head = 0;
		} else {
			++head;
		}
		++count;
	}

	public synchronized T get() throws InterruptedException {

		while (isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				throw new InterruptedException("Interupted while trying to get a object");
			}
		}

		notifyAll();
		T object = data.get(tail);
		if (tail == size - 1) { // Set the tail to the begin
			tail = 0;
		} else {
			++tail;
		}
		--count;

		return object;
	}

	public boolean isEmpty() {
		return count == 0;
	}

	public boolean isFull() {
		return count == size;
	}
}
