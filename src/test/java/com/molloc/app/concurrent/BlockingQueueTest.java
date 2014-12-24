package com.molloc.app.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class BlockingQueueTest {

	public static void main(String[] args) throws Exception {

		// 声明一个容量为10的缓存队列
		BlockingQueue<String> queue = new LinkedBlockingDeque<String>(10);

		Producer producer1 = new Producer(queue);
		Producer producer2 = new Producer(queue);
		Producer producer3 = new Producer(queue);
		Producer producer4 = new Producer(queue);
		Producer producer5 = new Producer(queue);
		Producer producer6 = new Producer(queue);
		Consumer consumer = new Consumer(queue);

		// 借助Executors
		ExecutorService service = Executors.newCachedThreadPool();
		// 启动线程
		service.execute(producer1);
		service.execute(producer2);
		service.execute(producer3);
		service.execute(producer4);
		service.execute(producer5);
		service.execute(producer6);
		service.execute(consumer);

		// 执行10s
		Thread.sleep(10 * 1000);
		producer1.stop();
		producer2.stop();
		producer3.stop();
		producer4.stop();
		producer5.stop();
		producer6.stop();

		Thread.sleep(2000);
		// 退出Executor
		service.shutdown();
	}
}
