
package tn.ittun.amzcmp.entity;

import java.util.List;


public class SearchFilter
{
	private String			keyword;
	private String			availability;
	private String			condition;
	private String			currencyOfPreference;
	private String			localeCode;
	private String			primaryCountry;
	private String			sortBy;
	private List<String>	deliveryFlags;
	private int				itemCount;
	private int				itemPage;
	private int				maxPrice;
	private int				minPrice;
	private int				minReviewsRating;
	private List<String>	showPricesFromList;

	public SearchFilter()
	{
		this.itemCount = 9;
		this.minPrice = 0;
	}

	public String getKeyword()
	{
		return keyword;
	}

	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}

	public String getAvailability()
	{
		return availability;
	}

	public void setAvailability(String availability)
	{
		this.availability = availability;
	}

	public String getCondition()
	{
		return condition;
	}

	public void setCondition(String condition)
	{
		this.condition = condition;
	}

	public String getCurrencyOfPreference()
	{
		return currencyOfPreference;
	}

	public void setCurrencyOfPreference(String currencyOfPreference)
	{
		this.currencyOfPreference = currencyOfPreference;
	}

	public String getLocaleCode()
	{
		return localeCode;
	}

	public void setLocaleCode(String localeCode)
	{
		this.localeCode = localeCode;
	}

	public String getSortBy()
	{
		return sortBy;
	}

	public void setSortBy(String sortBy)
	{
		this.sortBy = sortBy;
	}

	public List<String> getDeliveryFlags()
	{
		return deliveryFlags;
	}

	public void setDeliveryFlags(List<String> deliveryFlags)
	{
		this.deliveryFlags = deliveryFlags;
	}

	public int getItemCount()
	{
		return itemCount;
	}

	public void setItemCount(int itemCount)
	{
		this.itemCount = itemCount;
	}

	public int getItemPage()
	{
		return itemPage;
	}

	public void setItemPage(int itemPage)
	{
		this.itemPage = itemPage;
	}

	public int getMaxPrice()
	{
		return maxPrice;
	}

	public void setMaxPrice(int maxPrice)
	{
		this.maxPrice = maxPrice;
	}

	public int getMinPrice()
	{
		return minPrice;
	}

	public void setMinPrice(int minPrice)
	{
		this.minPrice = minPrice;
	}

	public int getMinReviewsRating()
	{
		return minReviewsRating;
	}

	public void setMinReviewsRating(int minReviewsRating)
	{
		this.minReviewsRating = minReviewsRating;
	}

	public List<String> getShowPricesFromList()
	{
		return showPricesFromList;
	}

	public void setShowPricesFromList(List<String> showPricesFromList)
	{
		this.showPricesFromList = showPricesFromList;
	}

	public String getPrimaryCountry()
	{
		return primaryCountry;
	}

	public void setPrimaryCountry(String primaryCountry)
	{
		this.primaryCountry = primaryCountry;
	}

}
