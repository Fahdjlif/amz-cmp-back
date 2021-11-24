
package tn.ittun.amzcmp.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "BUYING_INFO_SINGLE_TABLE")
@EntityListeners(AuditingEntityListener.class)
public class BuyingInfo
{
	@Id @GeneratedValue(strategy = GenerationType.AUTO) private Long	id;
	private String														price;
	private BigDecimal													amount;
	private String														buyUrl;
	private String														flag;
	private String														currency;

	public Long getId()
	{
		return id;
	}

	public String getPrice()
	{
		return price;
	}

	public void setPrice(String price)
	{
		this.price = price;
	}

	public String getBuyUrl()
	{
		return buyUrl;
	}

	public void setBuyUrl(String buyUrl)
	{
		this.buyUrl = buyUrl;
	}

	public BigDecimal getAmount()
	{
		return amount;
	}

	public void setAmount(BigDecimal amount)
	{
		this.amount = amount;
	}

	public String getFlag()
	{
		return flag;
	}

	public void setFlag(String flag)
	{
		this.flag = flag;
	}

	public String getCurrency()
	{
		return currency;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

}
