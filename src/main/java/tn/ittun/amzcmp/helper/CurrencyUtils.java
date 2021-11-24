
package tn.ittun.amzcmp.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import tn.ittun.amzcmp.entity.BuyingInfo;
import tn.ittun.amzcmp.entity.ConversionRate;


public class CurrencyUtils
{
	private static final String CONVERSION_API_URL = "https://api.exchangeratesapi.io/latest?base=%s";

	public static void convertPrices(List<BuyingInfo> buyingInfos, String preferedCurrency)
	{
		RestTemplate restTemplate = new RestTemplate();
		ConversionRate rates = restTemplate.getForObject( String.format( CONVERSION_API_URL, preferedCurrency), ConversionRate.class);
		try
		{
			buyingInfos.forEach( bi -> convert( bi, preferedCurrency, rates));
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}

	}

	private static void convert(BuyingInfo buyingInfo, String preferedCurrency, ConversionRate rates)
	{

		if( !buyingInfo.getCurrency().contentEquals( preferedCurrency) && buyingInfo.getAmount() != null)
		{
			double conversionRate = rates.getRates().findValue( buyingInfo.getCurrency()).asDouble();
			buyingInfo.setAmount( buyingInfo.getAmount().divide( new BigDecimal( conversionRate), 3, RoundingMode.HALF_UP));
		}
	}

	public static int ConvertSingle(int price, String currency, String preferedCurrency)
	{
		RestTemplate restTemplate = new RestTemplate();
		ConversionRate rates = restTemplate.getForObject( String.format( CONVERSION_API_URL, preferedCurrency), ConversionRate.class);
		if( !currency.contentEquals( preferedCurrency))
		{
			float conversionRate = rates.getRates().findValue( currency).asInt();
			return (int)Math.ceil( price / conversionRate);
		}
		return price;
	}
}
