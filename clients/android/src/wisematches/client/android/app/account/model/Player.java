package wisematches.client.android.app.account.model;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Player {
	long getId();

	String getNickname();


	String getType();

	boolean isOnline();

	String getMembership();
}
