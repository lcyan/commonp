package com.molloc.app.guava;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

import com.google.common.base.Throwables;
import com.molloc.app.BaseTest;

public class ThrowablesTest extends BaseTest
{
	@Test
	public void testThrowables()
	{
		try
		{
			throw new Exception();
		} catch (Throwable t)
		{
			String ss = Throwables.getStackTraceAsString(t);
			log(String.format("ss:%s" + ss));
			Throwables.propagate(t);
		}
	}

	@Test
	public void call() throws IOException
	{
		try
		{
			throw new IOException();
		} catch (Throwable t)
		{
			Throwables.propagateIfInstanceOf(t, IOException.class);
			throw Throwables.propagate(t);
		}
	}

	@Test
	public void testCheckException()
	{
		try
		{
			URL url = new URL("http://ociweb.com");
			final InputStream in = url.openStream();
			// read from the input stream
			in.close();
		} catch (Throwable t)
		{
			throw Throwables.propagate(t);
		}
	}
}
