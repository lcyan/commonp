package com.molloc.app.guava;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

public class TestJoiner
{
	Logger logger = LoggerFactory.getLogger(TestJoiner.class);

	Long[] arr = null;
	Long[] arrContainsNull = null;

	@Before
	public void setUp() throws Exception
	{
		arr = new Long[] { 1L, 2L, 5L, 8L, 10L };
		arrContainsNull = new Long[] { 1L, null, 5L, null, 10L };
	}

	@Test
	public void testOne()
	{
		log(Joiner.on(' ').join(arr));
	}

	@Test
	public void testTwo()
	{
		StringBuilder sb = new StringBuilder("result:");
		Joiner.on(" ").appendTo(sb, arr);
		log(sb.toString());// result:1 2 3
	}

	@Test
	public void testSkipNulls()
	{
		log(Joiner.on(' ').skipNulls().join(arrContainsNull));
	}

	@Test
	public void testUseForNull()
	{
		log(Joiner.on(' ').useForNull("None").join(arrContainsNull));
	}

	@Test
	public void testMapJoiner()
	{
		log(Joiner.on('#').withKeyValueSeparator("=").join(ImmutableMap.of(1, 2, 3, 4)));
	}

	public void log(String log)
	{
		logger.info(log);
	}
}
