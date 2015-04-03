package com.molloc.app.concurrent;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生产者线程
 * 
 * @author robot
 *
 */
public class Producer implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(Producer.class);
	private volatile boolean isRunning = true;
	@SuppressWarnings("rawtypes")
	private BlockingQueue queue;
	private static AtomicInteger count = new AtomicInteger();
	private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;

	@SuppressWarnings("rawtypes")
	public Producer(BlockingQueue queue) {
		this.queue = queue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		String data = null;
		Random r = new Random();

		logger.debug("启动生产者线程！");
		try {
			while (isRunning) {
				logger.debug("正在生产数据...");
				Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));

				data = "data:" + count.incrementAndGet();
				logger.debug("将数据：{}放入队列...", data);
				if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
					logger.debug("放入数据失败：{}", data);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally {
			logger.debug("退出生产者线程！");
		}
	}

	public void stop() {
		isRunning = false;
	}

}
