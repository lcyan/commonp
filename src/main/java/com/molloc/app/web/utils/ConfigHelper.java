package com.molloc.app.web.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @CopyRight: SinoSoft.
 * @Description:配置文件加载，支持多配置文件，支持重新加载 .
 * @Author: yanleichang.
 * @Create: 2014年8月15日.
 */
public class ConfigHelper
{
	private static final Logger logger = LoggerFactory.getLogger(ConfigHelper.class);

	private Properties prop;

	private Map<String, Properties> configs = new Hashtable<String, Properties>();

	private static final String defaultFileName = "application.properties";

	private static ConfigHelper config = new ConfigHelper();// single

	private ConfigHelper()
	{
		try
		{
			prop = loadProperties(defaultFileName);
			logger.info("成功加载默认配置文件--> {}:{}", defaultFileName, prop.toString());
		} catch (Exception ex)
		{
			logger.error("加载默认配置文件失败", ex);
		}
	}

	/**
	 * 重载配置文件
	 * 
	 * @throws Exception
	 */
	public void reload() throws Exception
	{
		String fileName = null;
		prop = loadProperties(defaultFileName);
		logger.info("成功加载默认配置文件--> {}:{}", defaultFileName, prop.toString());
		Iterator<String> it = configs.keySet().iterator();
		while (it.hasNext())
		{
			fileName = it.next();
			Properties p1 = loadProperties(fileName);
			logger.info("成功加载配置文件--> {}:{}", fileName, p1.toString());
			configs.put(fileName, p1);
		}
	}

	/**
	 * 加载配置文件.
	 * 
	 * @param fileName
	 *            配置文件名.
	 * @return 返回Propertyies对象.
	 * @throws Exception
	 *             异常.
	 */
	private Properties loadProperties( String fileName ) throws Exception
	{
		InputStream in = null;
		try
		{
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			Properties p = new Properties();
			p.load(in);
			return p;
		} finally
		{
			if (in != null)
			{
				in.close();
			}
		}
	}

	/**
	 * 根据key在默认配置文件中取值
	 * 
	 * @param key
	 * @return
	 */
	public String getConfig( String key )
	{
		return prop.getProperty(key);
	}

	/**
	 * 根据key在制定的配置文件中取值
	 * 
	 * @param key
	 * @param fileName
	 * @return
	 */
	public String getConfig( String key, String fileName )
	{
		try
		{
			Properties p1 = configs.get(fileName);
			if (null == p1)
			{
				InputStream in = null;
				try
				{
					in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
					logger.info("成功加载 --> {}", fileName);
					p1 = new Properties();
					p1.load(in);
					configs.put(fileName, p1);
				} catch (Exception e)
				{
					logger.warn("{}", e);
				} finally
				{
					if (in != null)
						try
						{
							in.close();
						} catch (Exception ex)
						{
							// do nothing...
						}
				}
				configs.put(fileName, p1);
			}
			return p1 != null ? p1.getProperty(key) : null;
		} catch (Exception e)
		{
			logger.warn("{}", e.getMessage());
			return null;
		}
	}

	/**
	 * 获取配置文件名获取Properties
	 * 
	 * @param filename
	 * @return
	 */
	public Properties getProperties( String filename )
	{
		if (filename == null)
		{
			return prop;
		}
		return configs.get(filename);
	}

	public static ConfigHelper getInstance()
	{
		return config;
	}

	/**
	 * @Description:根据key,返回Map
	 * @param string
	 * @return:
	 * 
	 *          eg:key=60001101|60001102
	 *          60001101=手机客户端
	 *          60001102=短信
	 */
	public Map<String, String> buildMapByKey( String key )
	{
		Map<String, String> retMap = new HashMap<String, String>();
		String parentKey = config.getConfig(key);
		if (StringUtils.isNotBlank(parentKey))
		{
			String[] keys = StringUtils.split(config.getConfig(key), "|");
			for (String temp : keys)
			{
				retMap.put(temp, config.getConfig(temp));
			}
		}
		return retMap;
	}
}
