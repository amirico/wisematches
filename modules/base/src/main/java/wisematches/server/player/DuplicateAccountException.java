package wisematches.server.player;

import java.util.Arrays;

/**
 * This exception is thrown is you try register or modify an account and any of the account's attributes
 * already registred.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DuplicateAccountException extends AccountException {
	private final String[] fieldNames;

	public DuplicateAccountException(Player player, String... fieldNames) {
		super("DuplicateAccount: " + Arrays.toString(fieldNames), player);
		if (fieldNames != null) {
			this.fieldNames = fieldNames.clone();
		} else {
			this.fieldNames = null;
		}
	}

	public DuplicateAccountException(Throwable cause, Player player, String... fieldNames) {
		super("DuplicateAccount: " + Arrays.toString(fieldNames), cause, player);
		if (fieldNames != null) {
			this.fieldNames = fieldNames.clone();
		} else {
			this.fieldNames = null;
		}
	}

	/**
	 * Returns the names of fields which already registered and can't be duplicate.
	 *
	 * @return the duplicate field names.
	 */
	public String[] getFieldNames() {
		if (fieldNames != null) {
			return fieldNames.clone();
		}
		return null;
	}
}
