package com.molloc.app.guava;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.primitives.Ints;
import com.molloc.app.BaseTest;

public class TestImmutable extends BaseTest
{
	@Test
	public void testGuava() throws Exception
	{
		ImmutableList<String> of = ImmutableList.of("a", "b", "c", "d");
		logger.info("{}", of);
		ImmutableMap<String, String> map = ImmutableMap.of("key1", "value1", "key2", "value2");
		logger.info("{}", map);
		int a = 1;
		int b = 3;
		logger.info("{}", Ints.compare(a, b));

		File file = new File("D:\\source\\molloc数据库密码和ftp密码.txt");

		logger.info("{}", Files.readLines(file, StandardCharsets.UTF_8));

		List<Integer> list = ImmutableList.of(1, 2, 3, 4);
		int[] array2 = Ints.toArray(list);
		logger.info("{}", array2);

		String digit = CharMatcher.DIGIT.retainFrom("some text 89983 and more");
		logger.info("{}", digit);
		String str = CharMatcher.DIGIT.removeFrom("some text 89983 and more");
		logger.info("{}", str);

		String testString = "foo , what,,,more,";
		logger.info("{}", Strings.repeat("*", 40));
		Iterable<String> split = Splitter.on(",").omitEmptyStrings().trimResults().split(testString);
		for (String it : split)
		{
			logger.info("{}", it);
		}
		logger.info("{}", Strings.repeat("*", 40));
		int[] array = { 1, 2, 3, 4, 5 };
		logger.info("has Elements {}", Ints.contains(array, 2));
		logger.info("{}", Strings.repeat("*", 40));
		int indexOf = Ints.indexOf(array, 4);
		int max = Ints.max(array);
		int min = Ints.min(array);
		int[] concat = Ints.concat(array, array2);
		logger.info("indexOf {}, max {}, min {}, concat {}", indexOf, max, min, concat);

		Map<String, Double> usPriceMap = Maps.newHashMap();
		usPriceMap.put("apple", 20D);
		usPriceMap.put("orange", 30D);
		usPriceMap.put("rice", 40D);

		Map<String, Double> cnPriceMap = Maps.transformValues(usPriceMap, new Function<Double, Double>()
		{
			double eurToUsd = 3;

			@Override
			public Double apply(Double input)
			{
				return eurToUsd * input;
			}
		});
		logger.info("{}", cnPriceMap);
		logger.info("{}", Strings.repeat("*", 40));
	//	List<String> names = ImmutableList.of("Aleksander", "Jaran", "Integrasco", "Guava", "Java");
	}
}
