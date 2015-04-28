package com.molloc.app.guava;

import org.junit.Test;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import com.google.common.primitives.Ints;
import com.molloc.app.BaseTest;

public class TestRange extends BaseTest
{

	@Test
	public void testBaseRange()
	{
		logger.info("open: {}", Range.open(1, 10));
		logger.info("closed: {}", Range.closed(1, 10));
		logger.info("closedOpen: {}", Range.closedOpen(1, 10));
		logger.info("openClosed: {}", Range.openClosed(1, 10));
		logger.info("greaterThan: {}", Range.greaterThan(10));
		logger.info("atLeast: {}", Range.atLeast(10));
		logger.info("lessThan: {}", Range.lessThan(10));
		logger.info("atMost: {}", Range.atMost(10));
		logger.info("all: {}", Range.all());
		logger.info("closed: {}", Range.closed(10, 10));
		logger.info("closedOpen: {}", Range.closedOpen(10, 10));
		// 会抛出异常
		logger.info("open: {}", Range.open(10, 10));
	}

	@Test
	public void testRange()
	{
		logger.info("downTo: {}", Range.downTo(4, BoundType.OPEN));
		logger.info("upTo: {}", Range.upTo(4, BoundType.CLOSED));
		logger.info("range: {}", Range.range(1, BoundType.CLOSED, 4, BoundType.OPEN));
	}

	@Test
	public void testContains()
	{
		log(Range.closed(1, 3).contains(2));
		log(Range.closed(1, 3).contains(4));
		log(Range.lessThan(5).contains(5));
		log(Range.closed(1, 4).containsAll(Ints.asList(1, 2, 3)));
	}

	@Test
	public void testQuery()
	{
		logger.info("hasLowerBound: {}", Range.closedOpen(4, 4).hasLowerBound());
		logger.info("hasUpperBound: {}", Range.closedOpen(4, 4).hasUpperBound());
		log(Range.closedOpen(4, 4).isEmpty());
		log(Range.openClosed(4, 4).isEmpty());
		log(Range.closed(4, 4).isEmpty());

		log(Range.closed(3, 10).lowerEndpoint());
		log(Range.open(3, 10).lowerEndpoint());
		log(Range.closed(3, 10).upperEndpoint());
		log(Range.open(3, 10).upperEndpoint());
		log(Range.closed(3, 10).upperBoundType());
		log(Range.open(3, 10).lowerBoundType());
	}

	@Test
	public void testEncloses()
	{
		Range<Integer> rangeBase = Range.open(1, 4); // 1 < x < 4
		Range<Integer> rangeClose = Range.closed(2, 3);// 1 <= x <=4
		Range<Integer> rangeCloseOpen = Range.closedOpen(2, 4);// 2 <= x < 4
		Range<Integer> rangeCloseOther = Range.closedOpen(2, 5);// 2 <= x < 5

		logger.info("rangeBase: {}, Enclose: {}, rangeClose: {}", rangeBase, rangeBase.encloses(rangeClose), rangeClose);
		logger.info("rangeBase: {}, Enclose: {}, rangeClose: {}", rangeBase, rangeBase.encloses(rangeCloseOpen),
				rangeCloseOpen);
		logger.info("rangeBase: {}, Enclose: {}, rangeClose: {}", rangeBase, rangeBase.encloses(rangeCloseOther),
				rangeCloseOther);
	}

	@Test
	public void testConnected()
	{
		log(Range.closed(3, 5).isConnected(Range.open(5, 10)));// true
		log(Range.closed(0, 9).isConnected(Range.closed(3, 4)));// true
		log(Range.closed(0, 5).isConnected(Range.closed(3, 9)));// true
		log(Range.open(3, 5).isConnected(Range.open(5, 10)));// false
		log(Range.closed(1, 5).isConnected(Range.closed(6, 10)));// false
	}

	@Test
	public void testIntersection()
	{
		log(Range.closed(3, 5).intersection(Range.open(5, 10)));// (5,5]
		log(Range.closed(0, 9).intersection(Range.closed(3, 4)));// [3,4]
		log(Range.closed(0, 5).intersection(Range.closed(3, 9)));// [3,5]
		log(Range.open(3, 5).intersection(Range.open(5, 10)));// exception
		log(Range.closed(1, 5).intersection(Range.closed(6, 10)));// exception

	}

	@Test
	public void testSpan()
	{
		log(Range.closed(3, 5).span(Range.open(5, 10)));// [3, 10)
		log(Range.closed(0, 9).span(Range.closed(3, 4)));// [0, 9]
		log(Range.closed(0, 5).span(Range.closed(3, 9)));// [0,9]
		log(Range.open(3, 5).span(Range.open(5, 10)));// (3,10)
		log(Range.closed(1, 5).span(Range.closed(6, 10)));// [1,10]
	}
}
