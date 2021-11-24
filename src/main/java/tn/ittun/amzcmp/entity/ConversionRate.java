
package tn.ittun.amzcmp.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversionRate
{
	private String		base;
	private Date		date;
	private JsonNode	rates;

	public String getBase()
	{
		return base;
	}

	public void setBase(String base)
	{
		this.base = base;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public JsonNode getRates()
	{
		return rates;
	}

	public void setRates(JsonNode rates)
	{
		this.rates = rates;
	}

}
