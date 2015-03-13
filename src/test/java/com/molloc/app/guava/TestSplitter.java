package com.molloc.app.guava;

import org.junit.Test;

import com.google.common.base.Splitter;
import com.molloc.app.BaseTest;

public class TestSplitter extends BaseTest
{

	@Test
	public void TestOne()
	{
		log(Splitter.on(' ').split("1 2 3"));
	}
	@Test
	public void TestMapSplitter ()
	{
		log(Splitter.on("#").withKeyValueSeparator(":").split("1:2#3:4"));
	}

	@Test
	public void TestPattern()
	{
		log(Splitter.onPattern("\\s+").split("1 \t   2 3"));
		log(Splitter.fixedLength(3).split("1 2 3"));// ["1 2", " 3"]
	}
}
