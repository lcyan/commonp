package com.molloc.app.guava;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.molloc.app.BaseTest;

public class TestCharMatcher extends BaseTest
{
	private static String SEQUENCE = "HELLO   RealFighter ~!@#$%^&*() ，,.。   \n123(*&gS   你好\t234啊   abc  \n";

	@Test
	public void tetCharMatch()
	{
		// 原始字符串
		log(SEQUENCE);
		// 使用JAVA_ISO_CONTROL去除所有控制字符\n\t
		String str = CharMatcher.JAVA_ISO_CONTROL.removeFrom(SEQUENCE);
		log(str);
		// 筛选出所有的字母(包含中文)或数字
		str = CharMatcher.JAVA_LETTER_OR_DIGIT.retainFrom(str);
		log(str);
		// 匹配sequence中的数字并全部替换成*号
		str = CharMatcher.JAVA_DIGIT.replaceFrom(SEQUENCE, "*");
		log(str);
		// 去除首尾连续匹配到的大写字符
		str = CharMatcher.JAVA_UPPER_CASE.trimLeadingFrom(SEQUENCE);
		log(str);
		// 去除尾部连续匹配到的大写字符
		str = CharMatcher.JAVA_LOWER_CASE.trimTrailingFrom(SEQUENCE);
		log(str);
		// 将匹配到的大写字符替换成问号
		str = CharMatcher.JAVA_UPPER_CASE.collapseFrom(SEQUENCE, '?');
		log(str);
		// 去除首尾空白符后将匹配到的小写字符替换为问号
		str = CharMatcher.JAVA_LOWER_CASE.trimAndCollapseFrom(SEQUENCE, '?');
		log(str);
	}
}
