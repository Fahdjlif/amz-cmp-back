
package tn.ittun.amzcmp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import tn.ittun.amzcmp.constants.AmzCmpConstants;
import tn.ittun.amzcmp.entity.Email;
import tn.ittun.amzcmp.entity.Role;
import tn.ittun.amzcmp.entity.User;
import tn.ittun.amzcmp.exception.CustomException;
import tn.ittun.amzcmp.helper.TokenUtils;
import tn.ittun.amzcmp.repository.UserRepository;


@Service
public class UserDetailService implements UserDetailsService
{

	@Autowired UserRepository		userRepository;
	@Autowired private EmailService	emailService;
	@Autowired private Environment	env;

	@Override
	public User loadUserByUsername(String email) throws UsernameNotFoundException
	{
		User user = userRepository.loadUserByEmail( email);
		if( user == null)
		{ throw new UsernameNotFoundException( String.format( "User with email %s not found", email)); }
		return user;

	}

	public User createUser(User user, String locale)
	{
		Assert.notNull( user, "please provide user details");
		if( userRepository.loadUserByEmail( user.getEmail()) != null)
		{
			throw new CustomException( "user email already used", HttpStatus.NOT_ACCEPTABLE);

		}
		user.setPassword( new BCryptPasswordEncoder().encode( user.getPassword()));
		user.setIsActive( Boolean.FALSE);
		user.setRoles( List.of( Role.USER));
		String token = TokenUtils.createToken( user.getEmail());
		user.setToken( token);
		Email mail = new Email();
		mail.setMailFrom( "support@product-guides.com");
		mail.setMailTo( user.getEmail());
		mail.setMailSubject( "Account activation");
		Map<String, Object> model = new HashMap<>();
		model.put( "username", user.getFirstName() + " " + user.getLastName());
		model.put( "link", env.getProperty( "front.main.url") + "/" + locale + "/#/?token=" + token);

		mail.setModel( model);
		try
		{

			emailService.sendSimpleMessage( mail, String.format( AmzCmpConstants.USER_ACTIVATION_TEMPLATE, locale));
		}
		catch( MailSendException e)
		{
			throw new CustomException( "wrong mail address", HttpStatus.NOT_ACCEPTABLE);
		}
		return userRepository.save( user);
	}

	public String activateUserAccount(String token)
	{
		User user = userRepository.loadUserByToken( token);
		if( user != null)
		{
			if( user.getIsActive() == true)
			{ return "account already activated"; }
			user.setIsActive( Boolean.TRUE);
			userRepository.save( user);
			return "account activated";
		}
		return "error activating account";
	}

	public Page<User> findAllUser(Pageable pageable)
	{
		return userRepository.findAll( pageable);
	}

	public long getUsersCount()
	{
		return userRepository.count();
	}

}
