package com.molloc.app.jodd;

import jodd.mail.Email;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;

import org.junit.Before;
import org.junit.Test;

public class TestEmailWithJodd
{
	private SmtpServer smtpServer;

	@Before
	public void init()
	{
		smtpServer = SmtpServer.create("smtp.qq.com").authenticateWith("username", "password");
	}

	@Test
	public void testSendEmail()
	{
		Email email = Email.create().from("290236573@qq.com").to("yanleichang@sinosoft.com.cn").subject("Hello HTML!")
				.addHtml("<b>HTML</b> message");
		SendMailSession session = smtpServer.createSession();
		session.open();
		session.sendMail(email);
		session.close();
	}
}
