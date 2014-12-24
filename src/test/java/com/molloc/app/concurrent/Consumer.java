package com.molloc.app.concurrent;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(Producer.class);
	private BlockingQueue<String> queue;
	private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;

	public Consumer(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	public void run() {
		logger.debug("启动消费者线程！");
		Random r = new Random();
		boolean isRunning = true;
		try {
			while (isRunning) {
				logger.debug("正从队列获取数据...");
				String data = queue.poll(2, TimeUnit.SECONDS);
				if (null != data) {
					logger.debug("拿到数据：{}", data);
					logger.debug("正在消费数据：{}", data);
					Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));
				} else {
					// 超过2s还没数据，认为所有生产线程都已经退出，自动退出消费线程。
					isRunning = false;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally {
			logger.debug("退出消费者线程！");
		}
	}

}
