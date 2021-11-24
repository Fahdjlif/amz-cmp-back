
package tn.ittun.amzcmp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.amazon.paapi5.v1.ApiClient;

import tn.ittun.amzcmp.repository.AccountRepository;


@Service
public class ApiClientService
{

	@Autowired Environment			environment;
	@Autowired AccountRepository	accountRepository;

	public ApiClient getApiClientForLocale(String localeCode)
	{
		ApiClient client = new ApiClient();
		tn.ittun.amzcmp.entity.Account account = accountRepository.loadAccountByLocaleCode( localeCode);
		String accessKey = environment.getProperty( "amazon.accessKey");
		String secretKey = environment.getProperty( "amazon.secretKey");
		client.setAccessKey( accessKey);
		client.setSecretKey( secretKey);
		client.setRegion( account.getRegion());
		client.setHost( account.getHost());
		return client;

	}
}
