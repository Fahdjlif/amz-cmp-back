
package tn.ittun.amzcmp.entity;

import java.io.Serializable;


public class PasswordChangeTO implements Serializable
{
	String	oldPassword;
	String	newPassword;

	public String getOldPassword()
	{
		return oldPassword;
	}

	public void setOldPassword(String oldPassword)
	{
		this.oldPassword = oldPassword;
	}

	public String getNewPassword()
	{
		return newPassword;
	}

	public void setNewPassword(String newPassword)
	{
		this.newPassword = newPassword;
	}

}
