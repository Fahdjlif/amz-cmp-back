
package tn.ittun.amzcmp.helper;

import java.util.UUID;


public class TokenUtils
{
	public static String createToken(String username)
	{
		UUID uuid = UUID.randomUUID();
		return uuid.toString().substring( 0, 20);
	}

}
