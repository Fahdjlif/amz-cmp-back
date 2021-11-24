
package tn.ittun.amzcmp.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import tn.ittun.amzcmp.entity.Email;


@Service
public class EmailService
{
	@Autowired private JavaMailSender	javaMailSender;
	@Autowired private Configuration	freemarkerConfig;

	public void sendSimpleMessage(Email mail, String template)
	{
		sendSimpleMessage( mail, template, Locale.ENGLISH);
	}

	public void sendSimpleMessage(Email mail, String template, Locale locale)
	{
		try
		{
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper( message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

			Template t = freemarkerConfig.getTemplate( template, locale);
			String html = FreeMarkerTemplateUtils.processTemplateIntoString( t, mail.getModel());
			helper.setText( html, true);
			if( mail.getAttachments() != null)
			{
				for( Entry<String, ClassPathResource> attachement : mail.getAttachments().entrySet())
				{
					helper.addInline( attachement.getKey(), attachement.getValue());
				}
			}

			helper.setTo( mail.getMailTo());
			helper.setSubject( mail.getMailSubject());
			helper.setFrom( mail.getMailFrom());

			javaMailSender.send( message);
		}
		catch( MessagingException | IOException | TemplateException e)
		{
			e.printStackTrace();
		}
	}
}
