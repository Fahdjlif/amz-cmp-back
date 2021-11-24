
package tn.ittun.amzcmp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("USER")
@Table(name = "USER_SINGLE_TABLE")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable, UserDetails
{

	private static final long																				serialVersionUID		= 5159062422885402464L;

	public static final String																				PASSOWRD_FIELD			= "password";
	public static final String																				EMAIL_FIELD				= "email";
	public static final String																				PHONE_FIELD				= "phoneNumber";
	@Id @GeneratedValue(strategy = GenerationType.AUTO) private Long										id;

	// @ValidPassword(message = "{validator.valid.password}")
	private String																							password;

	// @ValidEmail(message = "{validator.valid.email}")
	private String																							email;
	@Column private String																					firstName;
	@Column private String																					lastName;
	@Column @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER) private Collection<Role>	roles					= new ArrayList<>();
	@OneToMany(orphanRemoval = true) private Set<Product>													favoriteProducts;
	@OneToMany(orphanRemoval = true) private Set<SavedSearch>												savedSearchs;
	@Column(columnDefinition = "boolean default false") private Boolean										isActive				= false;
	@Column(columnDefinition = "boolean default true") private Boolean										isAccountNonExpired		= true;
	@Column(columnDefinition = "boolean default true") private Boolean										isAccountNonLocked		= true;
	@Column(columnDefinition = "boolean default true") private Boolean										isCredentialsNonExpired	= true;

	@Column private String																					token;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
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

	public Boolean getIsActive()
	{
		return isActive;
	}

	public void setIsActive(Boolean isActive)
	{
		this.isActive = isActive;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return this.roles;
	}

	public void setRoles(Collection<Role> roles)
	{
		this.roles = roles;
	}

	@Override
	public String getUsername()
	{
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired()
	{
		return this.isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return this.isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return this.isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled()
	{
		return this.isActive;
	}

	public Set<Product> getFavoriteProducts()
	{
		return favoriteProducts;
	}

	public void setFavoriteProducts(Set<Product> favoriteProducts)
	{
		this.favoriteProducts = favoriteProducts;
	}

	public Set<SavedSearch> getSavedSearchs()
	{
		return savedSearchs;
	}

	public void setSavedSearchs(Set<SavedSearch> savedSearchs)
	{
		this.savedSearchs = savedSearchs;
	}

}
