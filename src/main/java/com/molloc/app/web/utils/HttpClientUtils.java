package com.molloc.app.web.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils
{
	// 接口地址
	private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

	private static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * 
	 * 发送HTTP_POST请求
	 * 
	 * @see 该方法会自动关闭连接,释放资源
	 * 
	 * @see 该方法会自动对<code>params</code>中的[中文][|][ ]等特殊字符进行<code>URLEncoder.encode(string,encodeCharset)</code>
	 * 
	 * @param reqURL 请求地址
	 * 
	 * @param params 请求参数
	 * 
	 * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
	 * 
	 * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
	 * 
	 * @return 远程主机响应正文
	 */

	public static String sendPostRequest(String reqURL, Map<String, String> params, String encodeCharset,
			String decodeCharset)
	{

		StringUtils.defaultString(encodeCharset, DEFAULT_ENCODING);
		StringUtils.defaultString(decodeCharset, DEFAULT_ENCODING);

		String responseContent = null;

		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost(reqURL);

		List<NameValuePair> formParams = new ArrayList<NameValuePair>(); // 创建参数队列

		for (Map.Entry<String, String> entry : params.entrySet())
		{
			formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		try
		{
			httpPost.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset));

			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity entity = response.getEntity();

			if (null != entity)
			{
				responseContent = EntityUtils.toString(entity, decodeCharset);

				EntityUtils.consume(entity);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("与[{}]通信过程中发生异常,堆栈信息如下{}", reqURL, e.getMessage());

		} finally
		{
			closeQuietly(httpClient); // 关闭连接,释放资源
		}

		return responseContent;

	}

	/**
	 * 
	 * 发送HTTP_GET请求
	 * 
	 * @see 该方法会自动关闭连接,释放资源
	 * 
	 * @param requestURL 请求地址(含参数)
	 * 
	 * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
	 * 
	 * @return 远程主机响应正文
	 */

	public static String sendGetRequest(String reqURL, String decodeCharset)
	{

		StringUtils.defaultString(decodeCharset, DEFAULT_ENCODING);

		long responseLength = 0; // 响应长度

		String responseContent = null; // 响应内容

		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpGet httpGet = new HttpGet(reqURL); // 创建org.apache.http.client.methods.HttpGet

		try
		{

			HttpResponse response = httpClient.execute(httpGet); // 执行GET请求

			HttpEntity entity = response.getEntity(); // 获取响应实体

			if (null != entity)
			{

				responseLength = entity.getContentLength();

				responseContent = EntityUtils.toString(entity, decodeCharset);

				EntityUtils.consume(entity); // Consume response content

			}

			logger.info("请求地址: {}", httpGet.getURI());

			logger.info("响应状态: {}", response.getStatusLine());

			logger.info("响应长度: {}", responseLength);

			logger.info("响应内容: {}", responseContent);

		} catch (ClientProtocolException e)
		{

			logger.error("该异常通常是协议错误导致,比如构造HttpGet对象时传入的协议不对(将'http'写成'htp')或者服务器端返回的内容不符合HTTP协议要求等,堆栈信息如下.{}", e);

		} catch (IOException e)
		{

			logger.error("该异常通常是网络原因引起的,如HTTP服务器未启动等,堆栈信息如下{}", e);

		} finally
		{
			closeQuietly(httpClient); // 关闭连接,释放资源
		}

		return responseContent;

	}

	public static void closeQuietly(CloseableHttpClient httpClient)
	{
		try
		{
			if (null != httpClient)
			{
				httpClient.close();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
			logger.error("关闭httpClient异常,{}", e.getMessage());
		}

	}

	public static void main(String[] args) throws IOException
	{
		// HttpClientUtils.sendGetRequest("http://www.molloc.com", null);

		Map<String, String> params = new HashMap<String, String>();
		params.put("sessionId", "FDSFDSFDSGEWRFEWGHOFDSF");

	}

}
