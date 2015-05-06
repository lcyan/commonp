package com.molloc.app.guava;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.molloc.app.BaseTest;

public class TestStrings extends BaseTest
{
	private static final String string = "test";// 测试用字符串

	@Test
	public void testCharsets()
	{
		log(Charsets.UTF_8);
		log(Charsets.ISO_8859_1);
		log(Charsets.US_ASCII);
		log(Charsets.UTF_16);
		log(Charsets.UTF_16BE);
		log(Charsets.UTF_16LE);

		byte[] bytes;

		try
		{
			// 如果手动指定编码，这里可能会有一个不支持编码的异常
			bytes = string.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		// 使用string.getByters(Charsets.UTF_8)避免了此异常
		// 不过没有GBK编码，可能是guava开发组的人忘记了
		bytes = string.getBytes(Charsets.UTF_8);

		log(bytes);
	}

	@Test
	public void testStr()
	{
		// 我们需要将测试字符串变成一个长度为6的字符串，位数不够的话填充字符c
		// 这样的需求，经常出现在有金额表示的时候，如4.0补充到4.000
		// 传统的做法，我们需要循环追加，如下
		StringBuilder sb = new StringBuilder(string);
		char c = 's';
		for (int i = 0; i < 6 - string.length(); i++)
		{
			sb.append(c);
		}
		log(sb.toString());// testss
		// guava中提供了padEnd（param1,length,param2）方法
		// 将param1的长度改变为length，长度不足的话向后追加字符param2
		log(Strings.padEnd(string, 6, 's'));
		// 这里如果param1的长度大于length，则默认返回原字符串param1
		log(Strings.padEnd(string, 2, c));// test
		// 与之类似，也有padStart()方法，向前追加，长度超出默认返回原字符
		log(Strings.padStart(string, 6, c));// sstest
		// 另外，还有几个常用的方法
		// nullToEmpty：将null值转为空字符串
		log(Strings.nullToEmpty(null));// ""
		// emptyToNull：将空字符串转为null
		log(Strings.emptyToNull(""));// null
		// isNullOrEmpty：判断字符串为null或空字符串
		log(Strings.isNullOrEmpty(null));// true
		log(Strings.isNullOrEmpty(" ")); //false
		// repeat：用于将指定字符串循环拼接多次返回
		log(Strings.repeat(string, 3));// testtesttest

		// 另外，有两个方法用来进行字符串的比较
		// commonSuffix：返回两个字符串中相同的后缀部分
		log(Strings.commonSuffix("nihaoma?", "nibuhaoma?")); // haoma?
		// commonPrefix：返回两个字符串中相同的前缀部分
		log(Strings.commonPrefix("nihaoma?", "nibuhaoma?")); // ni
	}
}
