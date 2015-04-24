package com.molloc.app.guava;

import java.util.Map;

import org.assertj.core.util.Maps;
import org.junit.Test;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.molloc.app.BaseTest;

public class TestMultiset extends BaseTest
{
	// Multiset 允许重复但不保证顺序

	String strWorld = "wer|dffd|ddsa|dfd|dreg|de|dr|ce|ghrt|cf|gt|ser|tg|ghrt|cf|gt|"
			+ "ser|tg|gt|kldf|dfg|vcd|fg|gt|ls|lser|dfr|wer|dffd|ddsa|dfd|dreg|de|dr|"
			+ "ce|ghrt|cf|gt|ser|tg|gt|kldf|dfg|vcd|fg|gt|ls|lser|dfr";

	@Test
	public void testWorldCount()
	{

		String[] words = strWorld.split("\\|");

		Map<String, Integer> countMap = Maps.newHashMap();

		for (String word : words)
		{
			Integer count = countMap.get(word);
			if (null == count)
			{
				countMap.put(word, 0);
			} else
			{
				countMap.put(word, count + 1);
			}
		}

		logger.info("countMap: {}", countMap);
	}

	@Test
	public void testMultsetWordCount()
	{

		strWorld = "wer|dfd|dd|dfd|dda|de|dr";

		Multiset<String> worldsMultiset = HashMultiset.create(Splitter.on("|").split(strWorld));

		for (String key : worldsMultiset.elementSet())
		{
			logger.debug("{} count: {}", key, worldsMultiset.count(key));
		}

		if (!worldsMultiset.contains("peida"))
		{
			worldsMultiset.add("peida", 10);
		}

		log(Strings.repeat("-----", 40));
		for (Multiset.Entry<String> entry : worldsMultiset.entrySet())
		{
			logger.debug("{} count: {}", entry.getElement(), entry.getCount());
		}
		log(Strings.repeat("-----", 40));
		if (worldsMultiset.contains("peida"))
		{
			worldsMultiset.setCount("peida", 23);
		}
		for (Multiset.Entry<String> entry : worldsMultiset.entrySet())
		{
			logger.debug("{} count: {}", entry.getElement(), entry.getCount());
		}
		log(Strings.repeat("-----", 40));
		if (worldsMultiset.contains("peida"))
		{
			worldsMultiset.setCount("peida", 23, 45);
		}
		for (Multiset.Entry<String> entry : worldsMultiset.entrySet())
		{
			logger.debug("{} count: {}", entry.getElement(), entry.getCount());
		}
		log(Strings.repeat("-----", 40));
		if (worldsMultiset.contains("peida"))
		{
			// 44跟已经在set中的数量不一致的话,不会更新成功成67
			worldsMultiset.setCount("peida", 44, 67);
		}
		for (Multiset.Entry<String> entry : worldsMultiset.entrySet())
		{
			logger.debug("{} count: {}", entry.getElement(), entry.getCount());
		}
	}
}
