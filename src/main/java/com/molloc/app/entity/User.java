package com.molloc.app.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@XmlRootElement(name = "user")
@Entity
@Table(name = "ss_user")
public class User
{
	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private int age;

	private String email;

	private String loginName;

	private String nickName;

	private String loginPwd;

	private String salt;

	private String state;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@OrderBy()
	private Date createdTime;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getAge()
	{
		return age;
	}

	public void setAge(int age)
	{
		this.age = age;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getLoginName()
	{
		return loginName;
	}

	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
	}

	public String getNickName()
	{
		return nickName;
	}

	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}

	public String getLoginPwd()
	{
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd)
	{
		this.loginPwd = loginPwd;
	}

	public String getSalt()
	{
		return salt;
	}

	public void setSalt(String salt)
	{
		this.salt = salt;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8:00")
	public Date getCreatedTime()
	{
		return createdTime;
	}

	public void setCreatedTime(Date createdTime)
	{
		this.createdTime = createdTime;
	}

}
