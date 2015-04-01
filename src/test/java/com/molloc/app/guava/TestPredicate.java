package com.molloc.app.guava;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class TestPredicate
{
	@Test
	public void testLengthLessThan10()
	{
		Predicate<String> lengthLessThan10 = new Predicate<String>()
		{

			@Override
			public boolean apply(String input)
			{
				return input.length() < 10;
			}
		};

		Assert.assertFalse(lengthLessThan10.apply("lessThan10"));
	}
	
	@Test
	public void testLengthLessThen() {
		Function<Integer, Predicate<String>> lengthLessThen = new Function<Integer, Predicate<String>>()
		{

			@Override
			public Predicate<String> apply(final Integer len)
			{
				return new Predicate<String>()
				{

					@Override
					public boolean apply(String str)
					{
						return str.length() < len;
					}
				};
			}
		};
		
		Assert.assertFalse(lengthLessThen.apply(10).apply("lessThan10"));
	}
}
