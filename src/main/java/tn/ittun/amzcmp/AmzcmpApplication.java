
package tn.ittun.amzcmp;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import tn.ittun.amzcmp.entity.Account;
import tn.ittun.amzcmp.repository.AccountRepository;


@SpringBootApplication
public class AmzcmpApplication extends SpringBootServletInitializer implements CommandLineRunner
{
	@Autowired AccountRepository accountRepository;

	public static void main(String[] args)
	{

		new SpringApplicationBuilder( AmzcmpApplication.class)

			.sources( AmzcmpApplication.class)

			.properties( getProperties())

			.run( args);

	}

	@Override

	protected SpringApplicationBuilder configure(SpringApplicationBuilder springApplicationBuilder)
	{

		return springApplicationBuilder

			.sources( AmzcmpApplication.class)

			.properties( getProperties());

	}

	static Properties getProperties()
	{

		Properties props = new Properties();

		props.put( "spring.config.location", "/home/ubuntu/props/application.properties");

		return props;

	}

	@Override
	public void run(String... args) throws Exception
	{
		if( accountRepository.loadAccountByLocaleCode( "USA") == null)
		{
			Account account = new Account();
			account.setLocaleCode( "USA");
			account.setActive( Boolean.TRUE);
			account.setHost( "webservices.amazon.com");
			account.setMarketPlace( "www.amazon.com");
			account.setRegion( "us-east-1");
			account.setTag( "toponlinepr06-20");
			accountRepository.save( account);
		}
		if( accountRepository.loadAccountByLocaleCode( "DE") == null)
		{
			Account account = new Account();
			account.setLocaleCode( "DE");
			account.setActive( Boolean.TRUE);
			account.setHost( "webservices.amazon.de");
			account.setMarketPlace( "www.amazon.de");
			account.setRegion( "eu-west-1");
			account.setTag( "toumix2308-21");
			accountRepository.save( account);
		}
		// test coming soon
		if( accountRepository.loadAccountByLocaleCode( "IT") == null)
		{
			Account account = new Account();
			account.setLocaleCode( "IT");
			account.setActive( Boolean.FALSE);
			account.setHost( "webservices.amazon.it");
			account.setMarketPlace( "www.amazon.it");
			account.setRegion( "eu-west-1");
			account.setTag( "toumix2308-21");
			account.setComingSoon( Boolean.TRUE);
			accountRepository.save( account);
		}

	}

}
