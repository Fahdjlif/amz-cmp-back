
package tn.ittun.amzcmp.constants;

public final class AmzCmpConstants
{
	public static final String	LOGIN_ENDPOINT					= "/login";
	public static final String	SIGNUP_ENDPOINT					= "/signup";
	public static final String	ACTIVATE_ENDPOINT				= "/activate/{token}";
	public static final String	USERS_ENDPOINT					= "/user";
	public static final String	CREATE_USER_ENDPOINT			= "/create/user";
	public static final String	USERS_ENDPOINT_ID				= "/user/{id}";
	public static final String	USERS__FAVORITE_ENDPOINT		= "/user/{id}/favorite";
	public static final String	USERS__SAVED_SEARCH_ENDPOINT	= "/user/{id}/search";
	public static final String	ACCOUNTS_GETALL_ENDPOINT		= "/account/getall";
	public static final String	USER_ACTIVATION_TEMPLATE		= "user-activate-account-%s.ftl";
}
