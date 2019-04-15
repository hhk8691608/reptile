package com.example.demo.dispatch;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Storage <T> {
	
	private BlockingQueue<T> queue ;
	private boolean isExist = false;


	public Storage() {
		queue = new LinkedBlockingQueue<T>(100); 
	}
	
	public Storage(int queueSize) {
		super();
		queue = new LinkedBlockingQueue<T>(queueSize);
	}
	
	public T take() throws InterruptedException {
		return this.queue.take();
	}
	
	public void put(T e) throws InterruptedException {
		queue.put(e);
	}
	
	
	public boolean getIsExist() {
		return isExist;
	}

	public void setIsExist(boolean isExist) {
		this.isExist = isExist;
	}

	public BlockingQueue<T> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<T> queue) {
		this.queue = queue;
	}
	
	
	

	
}
