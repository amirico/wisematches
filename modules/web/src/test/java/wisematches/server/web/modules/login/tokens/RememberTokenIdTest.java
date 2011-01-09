package wisematches.server.web.modules.login.tokens;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RememberTokenIdTest extends TestCase {
    private static final int BYTE_MASK = 0xFF;

    public void test_decodeAndEncode() {
        final int i = RememberTokenId.decode("123.255.129.123");
        assertEquals(123, i & BYTE_MASK);
        assertEquals(129, (i >> 8) & BYTE_MASK);
        assertEquals(255, (i >> 16) & BYTE_MASK);
        assertEquals(123, (i >> 32) & BYTE_MASK);

        assertEquals("123.255.129.123", RememberTokenId.encode(i));
    }
}
