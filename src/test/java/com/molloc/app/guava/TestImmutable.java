package com.molloc.app.guava;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.molloc.app.BaseTest;

public class TestImmutable extends BaseTest
{

	@Test
	public void testJDKImmutable()
	{
		List<String> list = Lists.newArrayList();
		list.add("a");
		list.add("b");
		list.add("c");

		log(list);

		List<String> unmodifiableList = Collections.unmodifiableList(list);
		log(unmodifiableList);

		List<String> unmodifiableList1 = Collections.unmodifiableList(Arrays.asList("a", "b", "c"));
		log(unmodifiableList1);

		String temp = unmodifiableList.get(1);
		log(String.format("unmodifiableList [1] ： %s", temp));

		list.add("baby");

		logger.info("list add a item after list: {}", list);
		logger.info("list add a item after unmodifiableList: {}", unmodifiableList);

		unmodifiableList1.add("bb");
		logger.info("unmodifiableList1 add a item after list: {}", unmodifiableList1);

		unmodifiableList.add("cc");
		logger.info("unmodifiableList add a item after list: {}", unmodifiableList);
	}

	@Test
	public void testGuavaImmutable()
	{
		List<String> list = Lists.newArrayList();

		list.add("a");
		list.add("b");
		list.add("c");

		logger.info("list ： {}", list);

		ImmutableList<String> imlist = ImmutableList.copyOf(list);
		logger.info("imlist: {}", imlist);

		ImmutableList<String> imOflist = ImmutableList.of("peida", "jerry", "harry");
		logger.info("imOflist：{}", imOflist);

		ImmutableSortedSet<String> imSortedSet = ImmutableSortedSet.of("a", "b", "c", "a", "d", "b");
		logger.info("imSortedSet：{}", imSortedSet);

		list.add("baby");
		logger.info("list add a item after list: {}", list);
		logger.info("list add a item after imlist: {}", imlist);

		ImmutableSet<Color> imColorSet = ImmutableSet.<Color> builder().add(new Color(0, 255, 255))
				.add(new Color(0, 191, 255)).build();

		logger.info("imColorSet: {}", imColorSet);
	}

	@Test
	public void testCotyOf()
	{
		ImmutableSet<String> imSet = ImmutableSet.of("peida", "jerry", "harry", "lisa");
		logger.info("imSet： {}", imSet);

		ImmutableList<String> imlist = ImmutableList.copyOf(imSet);
		logger.info("imlist： {}", imlist);

		ImmutableSortedSet<String> imSortSet = ImmutableSortedSet.copyOf(imSet);
		logger.info("imSortSet： {}", imSortSet);

		List<String> list = Lists.newArrayList();
		for (int i = 0; i < 20; i++)
		{
			list.add(i + "x");
		}

		logger.info("list： {}", list);
		ImmutableList<String> imInfolist = ImmutableList.<String> copyOf(list.subList(2, 18));
		logger.info("imInfolist： {}", imInfolist);
		int imInfolistSize = imInfolist.size();
		logger.info("imInfolistSize：{}", imInfolistSize);
		ImmutableSet<String> imInfoSet = ImmutableSet.copyOf(imInfolist.subList(2, imInfolistSize - 3));
		logger.info("imInfoSet：{}", imInfoSet);
	}

}
