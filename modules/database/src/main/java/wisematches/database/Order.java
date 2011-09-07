package wisematches.database;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Order extends org.hibernate.criterion.Order {
	/**
	 * Constructor for Order.
	 */
	protected Order(String propertyName, boolean ascending) {
		super(propertyName, ascending);
	}
}
