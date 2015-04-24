package com.molloc.app.guava;

import java.util.Set;

import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.molloc.app.BaseTest;

public class TestOptional extends BaseTest
{

	@Test
	public void testNull()
	{
//		int age;
//		log("user age:" + age);
//
//		long money;
//		money = 10L;
//		log("user money" + money);
//
//		String name;
//		log("user name:" + name);
	}

	@Test
	public void testNullObject()
	{
		if (null instanceof java.lang.Object)
		{
			log("null属于java.lang.Object类型");
		} else
		{
			log("null不属于java.lang.Object类型");
		}
	}

	@Test
	public void testOptional()
	{
		Optional<Integer> possible = Optional.of(6);
		Optional<Integer> absentOptional = Optional.absent();
		Optional<Integer> NullableOpt = Optional.fromNullable(null);
		Optional<Integer> NoNullableOpt = Optional.fromNullable(10);
		if (possible.isPresent())
		{
			log("possible isPresent:" + possible.isPresent());
			log("possible value:" + possible.get());
		}

		if (absentOptional.isPresent())
		{
			log("absentOpt isPresent:" + absentOptional.isPresent());
		}

		if (NullableOpt.isPresent())
		{
			log("NullableOpt isPresent:" + NullableOpt.isPresent());
		}

		if (NoNullableOpt.isPresent())
		{
			log("NoNullableOpt isPresent:" + NoNullableOpt.isPresent());
		}
	}

	@Test
	public void testMethodReturn()
	{
		Optional<Long> value = method();
		if (value.isPresent())
		{
			log("获得返回值: " + value.get());
		} else
		{
			log("获得返回值: " + value.or(-12L));
		}

		log("获得返回值 orNull: " + value.orNull());

		log(Strings.repeat("---", 40));

		Optional<Long> valueNoNull = methodNoNull();
		if (valueNoNull.isPresent())
		{
			Set<Long> set = valueNoNull.asSet();
			log("获得返回值 set 的 size : " + set.size());
			log("获得返回值: " + valueNoNull.get());
		} else
		{
			log("获得返回值: " + valueNoNull.or(-12L));
		}

		log("获得返回值 orNull: " + valueNoNull.orNull());
	}

	private Optional<Long> method()
	{
		return Optional.fromNullable(null);
	}

	private Optional<Long> methodNoNull()
	{
		return Optional.fromNullable(15L);
	}
}
