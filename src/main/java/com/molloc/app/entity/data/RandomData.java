package com.molloc.app.entity.data;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 随机测试数据生成工具类.
 * 
 */
public class RandomData
{

	private static Random random = new Random();

	private static String[] emailSuffix = new String[] { "@126.com", "@molloc.com", "@163.com", "@qq.com", "@sina.com" };

	/**
	 * 返回随机ID.
	 */
	public static long randomId()
	{
		return random.nextLong();
	}

	/**
	 * 返回随机名称, prefix字符串+5位随机数字.
	 */
	public static String randomName(String prefix)
	{
		return prefix + random.nextInt(10000);
	}

	/**
	 * 返回随机名称, prefix字符串+5位随机数字.
	 */
	public static String randomEmail(String prefix)
	{
		return prefix + random.nextInt(10000) + emailSuffix[random.nextInt(emailSuffix.length)];
	}

	/**
	 * 从输入list中随机返回一个对象.
	 */
	public static <T> T randomOne(List<T> list)
	{
		Collections.shuffle(list);
		return list.get(0);
	}

	/**
	 * 从输入list中随机返回n个对象.
	 */
	public static <T> List<T> randomSome(List<T> list, int n)
	{
		Collections.shuffle(list);
		return list.subList(0, n);
	}

	/**
	 * 从输入list中随机返回随机个对象.
	 */
	public static <T> List<T> randomSome(List<T> list)
	{
		int size = random.nextInt(list.size());
		if (size == 0)
		{
			size = 1;
		}
		return randomSome(list, size);
	}
}
