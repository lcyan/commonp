package com.molloc.app.guava;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.molloc.app.BaseTest;

public class TestCacheLoading extends BaseTest
{

	private static Cache<String, String> cacheFromCallable = null;

	/**
	 * 对需要采用延迟的可以采用这个机制(泛型方式封装)
	 * 
	 * @return
	 * @throws Exception
	 */
	public static <K, V> Cache<K, V> callableCached() throws Exception
	{
		Cache<K, V> cache = CacheBuilder.newBuilder().maximumSize(10000).expireAfterWrite(10, TimeUnit.MINUTES).build();
		return cache;
	}

	private String getCallableCache(final String username)
	{
		try
		{
			// Callable只有在缓存值不存在时，才会调用
			return cacheFromCallable.get(username, new Callable<String>()
			{

				@Override
				public String call() throws Exception
				{
					logger.info("{} from db", username);
					return String.format("Hello %s !", username);
				}
			});
		} catch (ExecutionException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Test
	public void testCallableCached() throws Exception
	{
		final String u1Name = "peida";
		final String u2Name = "jerry";
		final String u3Name = "lisa";

		cacheFromCallable = callableCached();

		logger.info("peida: {}", getCallableCache(u1Name));
		logger.info("jerry: {}", getCallableCache(u2Name));
		logger.info("lisa: {}", getCallableCache(u3Name));
		logger.info("peida: {}", getCallableCache(u1Name));
	}

	@Test
	public void testLoadingCache() throws Exception
	{
		LoadingCache<String, String> cacheBuilder = CacheBuilder.newBuilder().build(new CacheLoader<String, String>()
		{

			@Override
			public String load(String key) throws Exception
			{
				String strProValue = String.format("hello%s!", key);
				return strProValue;
			}
		});

		logger.info("jerry values : {}", cacheBuilder.get("jerry"));
		logger.info("jerry values : {}", cacheBuilder.get("jerry"));
		logger.info("peida values : {}", cacheBuilder.get("peida"));
		logger.info("peida values : {}", cacheBuilder.get("peida"));
		logger.info("lisa values : {}", cacheBuilder.get("lisa"));
		cacheBuilder.put("harry", "ssdded");
		logger.info("harry values : {}", cacheBuilder.get("harry"));
	}

	@Test
	public void testCallableCache() throws Exception
	{
		Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(1000).build();

		String resultValue = cache.get("jerry", new Callable<String>()
		{

			@Override
			public String call() throws Exception
			{
				String strProValue = "hello " + "jerry" + "!";
				return strProValue;
			}
		});
		logger.info("jerry values : {}", resultValue);
		resultValue = cache.get("peida", new Callable<String>()
		{
			@Override
			public String call()
			{
				String strProValue = "hello " + "peida" + "!";
				return strProValue;
			}
		});
		logger.info("peida values : {}", resultValue);
	}

	/**
	 * 不需要延迟处理(泛型方式的封装)
	 * 
	 * @param cacheLoader
	 * @return
	 */
	public <K, V> LoadingCache<K, V> cached(CacheLoader<K, V> cacheLoader)
	{
		LoadingCache<K, V> cache = CacheBuilder.newBuilder().maximumSize(2).weakKeys().softValues()
				.refreshAfterWrite(120, TimeUnit.SECONDS).expireAfterWrite(10, TimeUnit.MINUTES)
				.removalListener(new RemovalListener<K, V>()
				{
					@Override
					public void onRemoval(RemovalNotification<K, V> notification)
					{
						logger.info(String.format("%s被移除", notification.getKey()));
					}
				}).build(cacheLoader);
		return cache;
	}

	/**
	 * 通过key获取value
	 * e.g. commonCache.get(key); return String
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public LoadingCache<String, String> commonCache(final String key) throws Exception
	{
		LoadingCache<String, String> commonCache = cached(new CacheLoader<String, String>()
		{

			@Override
			public String load(String key) throws Exception
			{
				return String.format("hello%s!", key);
			}

		});

		return commonCache;
	}

	@Test
	public void testCache() throws Exception
	{
		LoadingCache<String, String> commonCache = commonCache("peida");
		logger.info("perda: {}", commonCache.get("perda"));
		commonCache.get("harry");
		logger.info("harry: {}", commonCache.get("harry"));
		commonCache.get("lisa");
		logger.info("lisa: {}", commonCache.get("lisa"));
	}
}
