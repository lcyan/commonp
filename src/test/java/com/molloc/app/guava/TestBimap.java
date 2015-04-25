package com.molloc.app.guava;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.molloc.app.BaseTest;

public class TestBimap extends BaseTest
{

	@Test
	public void logMapTest()
	{
		Map<Integer, String> logfileMap = Maps.newHashMap();
		logfileMap.put(1, "a.log");
		logfileMap.put(2, "b.log");
		logfileMap.put(3, "c.log");
		logger.info("logfileMap: {}", logfileMap);

		Map<String, Integer> logfileInverseMap = Maps.newHashMap();

		logfileInverseMap = getInverseMap(logfileMap);

		logger.info("logfileInverseMap: {}", logfileInverseMap);
	}

	@Test
	public void bimapTest()
	{
		BiMap<Integer, String> logfileMap = HashBiMap.<Integer, String> create();
		logfileMap.put(1, "a.log");
		logfileMap.put(2, "b.log");
		logfileMap.put(3, "c.log");
		logger.info("logfileMap: {}", logfileMap);

		BiMap<String, Integer> filelogMap = logfileMap.inverse();
		logger.info("filelogMap: {}", filelogMap);

		logfileMap.put(4, "d.log");
		logger.info("logfileMap: {}", logfileMap);
		logger.info("filelogMap: {}", filelogMap);

	}

	/**
	 * 逆转Map的key和value
	 * 
	 * @param <S>
	 * @param <T>
	 * @param map
	 * @return
	 */
	public static <S, T> Map<T, S> getInverseMap(Map<S, T> map)
	{
		Map<T, S> inverseMap = Maps.newHashMap();
		for (Entry<S, T> entry : map.entrySet())
		{
			inverseMap.put(entry.getValue(), entry.getKey());
		}
		return inverseMap;
	}
}
