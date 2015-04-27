package com.molloc.app.guava;

import org.junit.Test;

import com.google.common.eventbus.EventBus;
import com.molloc.app.BaseTest;

public class TestEventBus extends BaseTest
{

	@Test
	public void testReceiveEvent()
	{
		EventBus eventBus = new EventBus("test");
		EventListener listener = new EventListener();

		eventBus.register(listener);

		eventBus.post(new TestEvent(200));
		eventBus.post(new TestEvent(300));
		eventBus.post(new TestEvent(400));

		logger.info("LastMessage: {}", listener.getLastMessage());
	}

	@Test
	public void testMultipleEvents() throws Exception
	{

		EventBus eventBus = new EventBus("test");
		MultipleListener multiListener = new MultipleListener();

		eventBus.register(multiListener);

		eventBus.post(new Integer(100));
		eventBus.post(new Integer(200));
		eventBus.post(new Integer(300));
		eventBus.post(new Long(800));
		eventBus.post(new Long(800990));
		eventBus.post(new Long(800882934));

		logger.info("LastInteger: {}", multiListener.getLastInteger());
		logger.info("LastLong: {}", multiListener.getLastLong());
	}

	@Test
	public void testDeadEventListeners() throws Exception
	{

		EventBus eventBus = new EventBus("test");
		DeadEventListener deadEventListener = new DeadEventListener();
		eventBus.register(deadEventListener);
		eventBus.post(new TestEvent(200));
		eventBus.post(new TestEvent(300));

		logger.info("deadEvent: {}", deadEventListener.isNotDelivered());
	}

	@Test
	public void testEventsFromSubclass() throws Exception
	{

		EventBus eventBus = new EventBus("test");
		IntegerListener integerListener = new IntegerListener();
		NumberListener numberListener = new NumberListener();
		eventBus.register(integerListener);
		eventBus.register(numberListener);

		eventBus.post(new Integer(100));

		logger.info("integerListener message: {}", integerListener.getLastMessage());
		logger.info("numberListener message: {}", numberListener.getLastMessage());

		eventBus.post(new Long(200L));

		logger.info("integerListener message: {}", integerListener.getLastMessage());
		logger.info("numberListener message: {}", numberListener.getLastMessage());
	}
}
