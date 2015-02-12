package com.molloc.app.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@XmlRootElement(name = "user")
public class User
{

	// FIXME id

	private Long id;

	@NotEmpty(message = "{user.name.not.empty}")
	private String name;

	@Min(value = 1, message = "{user.age.invalid}")
	@Max(value = 200, message = "{user.age.invalid}")
	private int age;

	@NotEmpty(message = "{user.email.not.empty}")
	@Email(message = "{user.email.invalid}")
	private String email;

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

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

}
