
package tn.ittun.amzcmp.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;


public class Email implements Serializable
{
	private static final long				serialVersionUID	= 2204734841572455923L;

	private String							mailFrom;
	private String							mailTo;
	private String							mailCc;
	private String							mailBcc;
	private String							mailSubject;
	private String							mailContent;
	private String							contentType;
	private Map<String, ClassPathResource>	attachments;
	private Map<String, Object>				model;

	public Email()
	{
		contentType = "text/plain";
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public String getMailBcc()
	{
		return mailBcc;
	}

	public void setMailBcc(String mailBcc)
	{
		this.mailBcc = mailBcc;
	}

	public String getMailCc()
	{
		return mailCc;
	}

	public void setMailCc(String mailCc)
	{
		this.mailCc = mailCc;
	}

	public String getMailFrom()
	{
		return mailFrom;
	}

	public void setMailFrom(String mailFrom)
	{
		this.mailFrom = mailFrom;
	}

	public String getMailSubject()
	{
		return mailSubject;
	}

	public void setMailSubject(String mailSubject)
	{
		this.mailSubject = mailSubject;
	}

	public String getMailTo()
	{
		return mailTo;
	}

	public void setMailTo(String mailTo)
	{
		this.mailTo = mailTo;
	}

	public Date getMailSendDate()
	{
		return new Date();
	}

	public String getMailContent()
	{
		return mailContent;
	}

	public void setMailContent(String mailContent)
	{
		this.mailContent = mailContent;
	}

	public Map<String, ClassPathResource> getAttachments()
	{
		return attachments;
	}

	public void setAttachments(Map<String, ClassPathResource> attachments)
	{
		this.attachments = attachments;
	}

	public Map<String, Object> getModel()
	{
		return model;
	}

	public void setModel(Map<String, Object> model)
	{
		this.model = model;
	}

}