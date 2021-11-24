
package tn.ittun.amzcmp.entity;

import java.util.List;


public class ProductSearchResult
{
	private Long			resultCount;
	private int				itemsCount;
	private boolean			hasMore;
	private List<Product>	products;

	public Long getResultCount()
	{
		return resultCount;
	}

	public void setResultCount(Long resultCount)
	{
		this.resultCount = resultCount;
	}

	public int getItemsCount()
	{
		return itemsCount;
	}

	public void setItemsCount(int itemsCount)
	{
		this.itemsCount = itemsCount;
	}

	public boolean isHasMore()
	{
		return hasMore;
	}

	public void setHasMore(boolean hasMore)
	{
		this.hasMore = hasMore;
	}

	public List<Product> getProducts()
	{
		return products;
	}

	public void setProducts(List<Product> products)
	{
		this.products = products;
	}

}
