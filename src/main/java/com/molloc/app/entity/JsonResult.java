package com.molloc.app.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * web层返回的json对象
 *
 */
@XmlRootElement(name = "jsonResult")
public class JsonResult implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4757288033112816628L;

	private int result = 0;// 处理成功返回0，其他错误
	private int action;// 下一步处理类型0 停留在当前页面 1 跳转到指定页面
	private String message;// 如果message不为空，则页面弹出消息框
	/**
	 * 如果message不为空，弹出消息框的类型
	 * alert:直接调用
	 * model:遮窗弹出
	 */
	private String msgmode = "alert";
	private String errmsg;

	private String url;

	private Map<String, Object> data = new HashMap<String, Object>();

	public void set(String key, Object value)
	{
		data.put(key, value);
	}

	public Map<String, Object> getData()
	{
		return data;
	}

	public int getResult()
	{
		return result;
	}

	public void setResult(int result)
	{
		this.result = result;
	}

	public int getAction()
	{
		return action;
	}

	public void setAction(int action)
	{
		this.action = action;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getMsgmode()
	{
		return msgmode;
	}

	public void setMsgmode(String msgmode)
	{
		this.msgmode = msgmode;
	}

	public String getErrmsg()
	{
		return errmsg;
	}

	public void setErrmsg(String errmsg)
	{
		this.errmsg = errmsg;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
		this.action = 1;
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}
}
