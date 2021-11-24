
package tn.ittun.amzcmp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.ittun.amzcmp.constants.AmzCmpConstants;
import tn.ittun.amzcmp.entity.Account;
import tn.ittun.amzcmp.repository.AccountRepository;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController
{
	@Autowired AccountRepository accountRepository;

	@GetMapping(path = AmzCmpConstants.ACCOUNTS_GETALL_ENDPOINT)
	List<Account> getAccounts()
	{
		return accountRepository.findAll();
	}
}
