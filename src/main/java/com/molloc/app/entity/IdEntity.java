package com.molloc.app.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 统一定义id的entity基类.
 * 
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 * Oracle需要每个Entity独立定义id的SEQUCENCE时，不继承于本类而改为实现一个Idable的接口。
 * 
 */
// JPA 基类的标识

@MappedSuperclass
public abstract class IdEntity<ID extends Serializable>
{

	/**
	 * 
	 */
	protected ID id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public ID getId()
	{
		return id;
	}

	public void setId(ID id)
	{
		this.id = id;
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}

}
