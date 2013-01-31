package wisematches.playground;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MockPlayerHand extends AbstractPlayerHand {
	public MockPlayerHand(short points, short oldRating, short newRating) {
		super(new MockPlayer(1), points, oldRating, newRating);
	}
}
