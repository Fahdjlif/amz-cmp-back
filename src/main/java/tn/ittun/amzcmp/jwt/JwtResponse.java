
package tn.ittun.amzcmp.jwt;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;


public class JwtResponse implements Serializable
{
	/**
	 * 
	 */
	private static final long				serialVersionUID	= 4348305654561629546L;
	private final String					jwttoken;
	private Long							userId;
	private String							userEmail;
	private String							firstName;
	private String							lastName;
	Collection<? extends GrantedAuthority>	authorities;

	public JwtResponse(String jwttoken)
	{
		this( jwttoken, null, null, null);

	}

	public JwtResponse(String jwttoken, Collection<? extends GrantedAuthority> authorities, Long userId, String userEmail)
	{
		this.jwttoken = jwttoken;
		this.authorities = authorities;
		this.userId = userId;
		this.userEmail = userEmail;

	}

	public String getToken()
	{
		return this.jwttoken;
	}

	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return authorities;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities)
	{
		this.authorities = authorities;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getUserEmail()
	{
		return userEmail;
	}

	public void setUserEmail(String userEmail)
	{
		this.userEmail = userEmail;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

}
