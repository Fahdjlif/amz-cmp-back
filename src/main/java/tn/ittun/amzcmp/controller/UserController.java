
package tn.ittun.amzcmp.controller;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tn.ittun.amzcmp.constants.AmzCmpConstants;
import tn.ittun.amzcmp.entity.PasswordChangeTO;
import tn.ittun.amzcmp.entity.Product;
import tn.ittun.amzcmp.entity.SavedSearch;
import tn.ittun.amzcmp.entity.User;
import tn.ittun.amzcmp.exception.CustomException;
import tn.ittun.amzcmp.exception.ResourceNotFoundException;
import tn.ittun.amzcmp.jwt.JwtResponse;
import tn.ittun.amzcmp.jwt.JwtTokenProvider;
import tn.ittun.amzcmp.repository.BuyingInfoRepository;
import tn.ittun.amzcmp.repository.ProductRepository;
import tn.ittun.amzcmp.repository.SavedSearchRepository;
import tn.ittun.amzcmp.repository.UserRepository;
import tn.ittun.amzcmp.service.UserDetailService;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController
{
	@Autowired UserRepository					userRepository;
	@Autowired ProductRepository				productRepository;
	@Autowired SavedSearchRepository			searchRepository;
	@Autowired BuyingInfoRepository				buyingInfoRepository;
	@Autowired private AuthenticationManager	authenticationManager;
	@Autowired private UserDetailService		userDetailsService;
	@Autowired private JwtTokenProvider			jwtTokenProvider;

	@PostMapping(AmzCmpConstants.LOGIN_ENDPOINT)
	public JwtResponse signin(@RequestHeader("Authorization") String authorization)
	{
		String credentials = new String( Base64.decodeBase64( authorization.getBytes()));
		String username = credentials.split( ":")[0];
		String password = credentials.split( ":")[1];
		try
		{
			User user = userDetailsService.loadUserByUsername( username);
			Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
			authenticationManager.authenticate( new UsernamePasswordAuthenticationToken( username, password, authorities));
			JwtResponse jwtResponse = jwtTokenProvider.createToken( username, authorities);
			jwtResponse.setAuthorities( authorities);
			jwtResponse.setUserId( user.getId());
			jwtResponse.setUserEmail( user.getEmail());
			jwtResponse.setFirstName( user.getFirstName());
			jwtResponse.setLastName( user.getLastName());
			return jwtResponse;
		}
		catch( AuthenticationException e)
		{
			throw new CustomException( "Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@GetMapping("/check")
	public void isConnected()
	{
		// nothing to do
	}

	/**
	 * 
	 * @return all users
	 */

	@GetMapping(AmzCmpConstants.USERS_ENDPOINT)
	public Page<User> getAllUsers(@RequestParam(required = false, defaultValue = "0") String page,
		@RequestParam(required = false, defaultValue = "30") String size)
	{
		Pageable pageable = PageRequest.of( Integer.parseInt( page), Integer.parseInt( size));
		try
		{
			return userRepository.findAll( pageable);

		}
		catch( Exception e)
		{
			throw new CustomException( e.getMessage(), HttpStatus.NO_CONTENT);
		}

	}

	/**
	 * create a new user, can't be null
	 * 
	 * @param user
	 * @return created user
	 */
	@PostMapping(AmzCmpConstants.CREATE_USER_ENDPOINT)
	public User createUser(@RequestBody User user, @RequestParam String locale)
	{
		Assert.notNull( user, "please provide user details");
		try
		{
			User existing = userDetailsService.loadUserByUsername( user.getEmail());
			if( existing != null)
			{ throw new CustomException( "email used", HttpStatus.NO_CONTENT); }

		}
		catch( UsernameNotFoundException e)
		{
			try
			{
				return userDetailsService.createUser( user, locale);
			}
			catch( CustomException emailException)
			{
				throw emailException;
			}

		}
		return null;

	}

	/**
	 * 
	 * @param userId
	 * @return user with the specified id
	 */
	@GetMapping(AmzCmpConstants.USERS_ENDPOINT_ID)
	public User getUserById(@PathVariable(value = "id") Long userId)
	{
		return userRepository.findById( userId).orElseThrow( () -> new ResourceNotFoundException( "User", "id", userId));
	}

	/**
	 * 
	 * @param userId
	 * @param favoriteAsin
	 * @return
	 */
	@DeleteMapping(AmzCmpConstants.USERS__FAVORITE_ENDPOINT)
	public void removeFavorite(@PathVariable(value = "id") Long userId, @RequestParam(required = true) String favoriteAsin)
	{

		User user = userRepository.findById( userId).orElseThrow( () -> new ResourceNotFoundException( "User", "id", userId));

		Set<Product> favoriteProducts = user.getFavoriteProducts();
		favoriteProducts.removeIf( pr -> pr.getAsin().contentEquals( favoriteAsin));
		user.setFavoriteProducts( favoriteProducts);
		userRepository.save( user);
	}

	@PostMapping(AmzCmpConstants.USERS__FAVORITE_ENDPOINT)
	public void addFavorite(@PathVariable(value = "id") Long userId, @RequestBody Product product)
	{

		User user = userRepository.findById( userId).orElseThrow( () -> new ResourceNotFoundException( "User", "id", userId));
		product.getBuyingInfo().forEach( b -> buyingInfoRepository.save( b));
		product = productRepository.save( product);
		user.getFavoriteProducts().add( product);
		userRepository.save( user);
	}

	@GetMapping(AmzCmpConstants.USERS__FAVORITE_ENDPOINT)
	public List<Product> getUserFavorites(@PathVariable(value = "id") Long userId)
	{

		User user = userRepository.findById( userId).orElseThrow( () -> new ResourceNotFoundException( "User", "id", userId));
		return user.getFavoriteProducts().stream().map( p -> productRepository.getOne( p.getId())).collect( Collectors.toList());
	}

	@DeleteMapping(AmzCmpConstants.USERS__SAVED_SEARCH_ENDPOINT)
	public void removeSearch(@PathVariable(value = "id") Long userId, @RequestParam(required = true) Long searchId)
	{

		User user = userRepository.findById( userId).orElseThrow( () -> new ResourceNotFoundException( "User", "id", userId));

		Set<SavedSearch> savedSearchs = user.getSavedSearchs();
		savedSearchs.removeIf( p -> p.getId() == searchId);
		user.setSavedSearchs( savedSearchs);
		userRepository.save( user);
	}

	@PostMapping(AmzCmpConstants.USERS__SAVED_SEARCH_ENDPOINT)
	public void addSearch(@PathVariable(value = "id") Long userId, @RequestBody SavedSearch savedSearch)
	{

		User user = userRepository.findById( userId).orElseThrow( () -> new ResourceNotFoundException( "User", "id", userId));
		SavedSearch savedSearchPersisted = searchRepository.save( savedSearch);
		user.getSavedSearchs().add( savedSearchPersisted);
		userRepository.save( user);
	}

	@GetMapping(AmzCmpConstants.USERS__SAVED_SEARCH_ENDPOINT)
	public List<SavedSearch> getUserSavedSearchs(@PathVariable(value = "id") Long userId)
	{

		User user = userRepository.findById( userId).orElseThrow( () -> new ResourceNotFoundException( "User", "id", userId));
		return user.getSavedSearchs().stream().map( p -> searchRepository.getOne( p.getId())).collect( Collectors.toList());
	}

	/**
	 * 
	 * @param userId
	 * @param userDetails
	 * @return
	 * @return updated user
	 */
	@PutMapping(AmzCmpConstants.USERS_ENDPOINT_ID)
	public JwtResponse updateUser(@PathVariable(value = "id") Long userId, @RequestBody PasswordChangeTO passwordChangeTO)
	{

		User user = userRepository.findById( userId).orElseThrow( () -> new ResourceNotFoundException( "User", "id", userId));
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		if( !bCryptPasswordEncoder.matches( passwordChangeTO.getOldPassword(), user.getPassword()))
		{ throw new CustomException( "Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY); }
		user.setPassword( new BCryptPasswordEncoder().encode( passwordChangeTO.getNewPassword()));
		userRepository.save( user);
		try
		{
			Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken( user.getUsername(), passwordChangeTO.getNewPassword(), authorities));
			JwtResponse jwtResponse = jwtTokenProvider.createToken( user.getUsername(), authorities);
			jwtResponse.setAuthorities( authorities);
			jwtResponse.setUserId( user.getId());
			jwtResponse.setUserEmail( user.getEmail());
			return jwtResponse;
		}
		catch( AuthenticationException e)
		{
			throw new CustomException( "Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(AmzCmpConstants.USERS_ENDPOINT_ID)
	public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long userId)
	{
		User user = userRepository.findById( userId).orElseThrow( () -> new ResourceNotFoundException( "User", "id", userId));

		userRepository.delete( user);

		return ResponseEntity.ok().build();
	}

	/**
	 * sign up for a normal user
	 * 
	 * @param user
	 * @return created user
	 */
	@PostMapping(AmzCmpConstants.SIGNUP_ENDPOINT)
	public void userSignUp(@RequestBody User user, @RequestParam String locale)
	{
		Assert.notNull( user, "please provide user details");
		userDetailsService.createUser( user, locale);
	}

	@GetMapping(AmzCmpConstants.ACTIVATE_ENDPOINT)
	public ResponseEntity<?> activateUserAccount(@PathVariable(value = "token") String token)
	{
		Assert.notNull( token, "please provide a valid token");
		String status = userDetailsService.activateUserAccount( token);
		if( status.equals( "account activated"))
		{
			return ResponseEntity.ok().build();
		}
		else if( status.equals( "account already activated"))
		{ return new ResponseEntity<>( status, HttpStatus.BAD_GATEWAY); }
		return new ResponseEntity<>( status, HttpStatus.BAD_GATEWAY);

	}

}
