
package tn.ittun.amzcmp.constants;

import java.util.Arrays;


public enum Currency
{
	USD( "USD"), EUR( "EUR"), GBP( "GBP");

	private final String id;

	private Currency(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public static Currency getById(final String id)
	{
		return Arrays.stream( Currency.values()).filter( t -> t.getId() == id).findFirst().get();
	}
}
