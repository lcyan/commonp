package com.molloc.app.fastjson;

import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.molloc.app.BaseTest;
import com.molloc.app.entity.User;

public class FastJsonTest extends BaseTest
{
	private static final SerializerFeature[] features = { SerializerFeature.WriteMapNullValue, // 输出空置字段
			SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
			SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
			SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
			SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
	};

	@Test
	public void testToJson()
	{
		Map<String, Object> maps = Maps.newLinkedHashMap();

		List<Object> emptyList = Lists.emptyList();
		List<Object> nullList = null;
		List<? extends Object> hasElementList = Lists.newArrayList(1L, 2L, 4, "aa");

		User user = new User();
		user.setId(null);

		Double nullDouble = null;
		Double hasValueDobule = 2D;

		Boolean nullBoolean = null;
		boolean trueBoolean = true;

		maps.put("emptyList", emptyList);
		maps.put("nullList", nullList);
		maps.put("hasElementList", hasElementList);
		maps.put("nullDouble", nullDouble);

		maps.put("hasValueDobule", hasValueDobule);
		maps.put("nullBoolean", nullBoolean);
		maps.put("trueBoolean", trueBoolean);
		maps.put("user", user);

		// logger.debug("{}", JSON.toJSONString(maps));
		// logger.debug("{}", JSON.toJSONString(maps, SerializerFeature.WriteMapNullValue));
		// logger.debug("{}", JSON.toJSONString(maps, SerializerFeature.WriteNullBooleanAsFalse));
		logger.debug("{}", JSON.toJSONString(maps, features));
	}
}
