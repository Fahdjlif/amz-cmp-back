
package tn.ittun.amzcmp.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "PRODUCT_SINGLE_TABLE")
@EntityListeners(AuditingEntityListener.class)
public class Product
{
	@Id @GeneratedValue(strategy = GenerationType.AUTO) private Long													id;
	private String																										asin;
	private String																										productName;
	private boolean																										isFavorite;
	@Column @ElementCollection(targetClass = String.class) private List<String>											images;
	@Column @ElementCollection(targetClass = BuyingInfo.class, fetch = FetchType.EAGER) private Collection<BuyingInfo>	buyingInfo	= new ArrayList<>();

	public Long getId()
	{
		return id;
	}

	public String getAsin()
	{
		return asin;
	}

	public void setAsin(String asin)
	{
		this.asin = asin;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public List<String> getImages()
	{
		return images;
	}

	public void setImages(List<String> images)
	{

		this.images = images;
	}

	public Collection<BuyingInfo> getBuyingInfo()
	{
		return buyingInfo;
	}

	public void setBuyingInfo(List<BuyingInfo> buyingInfo)
	{
		this.buyingInfo = buyingInfo;
	}

	public boolean isFavorite()
	{
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite)
	{
		this.isFavorite = isFavorite;
	}

}
