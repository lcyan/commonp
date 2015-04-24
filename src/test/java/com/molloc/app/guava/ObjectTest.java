package com.molloc.app.guava;

import java.util.Comparator;

import org.junit.Test;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.molloc.app.BaseTest;

public class ObjectTest extends BaseTest
{

	@Test
	public void equalsTest()
	{
		log(Objects.equal("a", "b"));
		log(Objects.equal("a", null));
		log(Objects.equal(null, "a"));
		log(Objects.equal(null, null));
		logLine();
	}

	@Test
	public void equalsPersonTest()
	{
		log(Objects.equal(new Person("p1", 123), new Person("p1", 123)));
		Person person = new Person("lcyan", 22);
		log(Objects.equal(person, person));
		logLine();
	}

	@Test
	public void hashCodeTest()
	{
		log(Objects.hashCode("a"));
		log(Objects.hashCode("a", "b", "c"));
		log(Objects.hashCode("a", "b"));
		Person person = new Person("lcyan", 22);
		log(Objects.hashCode(person));
		log(Objects.hashCode(person));
		logLine();
	}

	@Test
	public void toStringTest()
	{
		log(MoreObjects.toStringHelper(this).add("x", 1).toString());
		log(MoreObjects.toStringHelper(Person.class).add("x", 1).toString());
		Person person = new Person("lcyan", 22);
		log(MoreObjects.toStringHelper(Person.class).add("name", person.name).add("age", person.age).toString());
		logLine();
	}
}

class Person
{
	public String name;
	public int age;

	Person(String name, int age)
	{
		this.name = name;
		this.age = age;
	}
}

class Student implements Comparable<Student>
{
	public String name;
	public int age;
	public int score;

	Student(String name, int age, int score)
	{
		this.name = name;
		this.age = age;
		this.score = score;
	}

	@Override
	public int compareTo(Student other)
	{
		return ComparisonChain.start().compare(name, other.name).compare(age, other.age)
				.compare(score, other.score, Ordering.natural().nullsLast()).result();
	}
}

class StudentComparator implements Comparator<Student>
{
	@Override
	public int compare(Student s1, Student s2)
	{
		return ComparisonChain.start().compare(s1.name, s2.name).compare(s1.age, s2.age).compare(s1.score, s2.score)
				.result();
	}
}
