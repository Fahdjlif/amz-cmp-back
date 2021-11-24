
package tn.ittun.amzcmp.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "ACCOUNT_SINGLE_TABLE")
@EntityListeners(AuditingEntityListener.class)
public class Account
{
	@Id @GeneratedValue(strategy = GenerationType.AUTO) private Long	id;
	private String														localeCode;
	private String														host;
	private String														region;
	private String														tag;
	private String														marketPlace;
	private boolean														isActive;
	private boolean														comingSoon;

	public Long getId()
	{
		return id;
	}

	public String getLocaleCode()
	{
		return localeCode;
	}

	public void setLocaleCode(String localeCode)
	{
		this.localeCode = localeCode;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getRegion()
	{
		return region;
	}

	public void setRegion(String region)
	{
		this.region = region;
	}

	public String getTag()
	{
		return tag;
	}

	public void setTag(String tag)
	{
		this.tag = tag;
	}

	public String getMarketPlace()
	{
		return marketPlace;
	}

	public void setMarketPlace(String marketPlace)
	{
		this.marketPlace = marketPlace;
	}

	public boolean isActive()
	{
		return isActive;
	}

	public void setActive(boolean isActive)
	{
		this.isActive = isActive;
	}

	public boolean isComingSoon()
	{
		return comingSoon;
	}

	public void setComingSoon(boolean comingSoon)
	{
		this.comingSoon = comingSoon;
	}

}
