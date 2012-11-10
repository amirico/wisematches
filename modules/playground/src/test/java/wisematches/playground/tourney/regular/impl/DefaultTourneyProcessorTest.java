package wisematches.playground.tourney.regular.impl;

import org.junit.Test;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultTourneyProcessorTest {
    private DefaultTourneyProcessor tourneyProcessor = new DefaultTourneyProcessor();

    public DefaultTourneyProcessorTest() {
    }

    @Test
    public void testCronExpression() throws ParseException {
        CronExpression e = new CronExpression("0 0 0 ? * 2#1");
        System.out.println(e.getNextValidTimeAfter(new Date()));
    }

    @Test
    public void testSplitByGroups() throws Exception {

        final List<long[]> l1 = tourneyProcessor.splitByGroups(Arrays.asList(101L, 102L));
        assertEquals(1, l1.size());
        assertArrayEquals(new long[]{101L, 102L}, l1.get(0));

        final List<long[]> l2 = tourneyProcessor.splitByGroups(Arrays.asList(101L, 102L, 103L));
        assertEquals(1, l2.size());
        assertArrayEquals(new long[]{101L, 102L, 103L}, l2.get(0));

        final List<long[]> l3 = tourneyProcessor.splitByGroups(Arrays.asList(101L, 102L, 103L, 104L));
        assertEquals(1, l3.size());
        assertArrayEquals(new long[]{101L, 102L, 103L, 104L}, l3.get(0));

        final List<long[]> l4 = tourneyProcessor.splitByGroups(Arrays.asList(101L, 102L, 103L, 104L, 105L));
        assertEquals(2, l4.size());
        assertArrayEquals(new long[]{101L, 102L, 103L}, l4.get(0));
        assertArrayEquals(new long[]{104L, 105L}, l4.get(1));

        final List<long[]> l5 = tourneyProcessor.splitByGroups(Arrays.asList(101L, 102L, 103L, 104L, 105L, 106L));
        assertEquals(2, l5.size());
        assertArrayEquals(new long[]{101L, 102L, 103L}, l5.get(0));
        assertArrayEquals(new long[]{104L, 105L, 106L}, l5.get(1));

        final List<long[]> l6 = tourneyProcessor.splitByGroups(Arrays.asList(101L, 102L, 103L, 104L, 105L, 106L, 107L));
        assertEquals(2, l6.size());
        assertArrayEquals(new long[]{101L, 102L, 103L, 104L}, l6.get(0));
        assertArrayEquals(new long[]{105L, 106L, 107L}, l6.get(1));

        final List<long[]> l7 = tourneyProcessor.splitByGroups(Arrays.asList(101L, 102L, 103L, 104L, 105L, 106L, 107L, 108L));
        assertEquals(2, l7.size());
        assertArrayEquals(new long[]{101L, 102L, 103L, 104L}, l7.get(0));
        assertArrayEquals(new long[]{105L, 106L, 107L, 108L}, l7.get(1));

        final List<long[]> l8 = tourneyProcessor.splitByGroups(Arrays.asList(101L, 102L, 103L, 104L, 105L, 106L, 107L, 108L, 109L));
        assertEquals(3, l8.size());
        assertArrayEquals(new long[]{101L, 102L, 103L}, l8.get(0));
        assertArrayEquals(new long[]{104L, 105L, 106L}, l8.get(1));
        assertArrayEquals(new long[]{107L, 108L, 109L}, l8.get(2));

        final List<long[]> l9 = tourneyProcessor.splitByGroups(Arrays.asList(101L, 102L, 103L, 104L, 105L, 106L, 107L, 108L, 109L, 110L));
        assertEquals(3, l9.size());
        assertArrayEquals(new long[]{101L, 102L, 103L, 104L}, l9.get(0));
        assertArrayEquals(new long[]{105L, 106L, 107L}, l9.get(1));
        assertArrayEquals(new long[]{108L, 109L, 110L}, l9.get(2));

        final List<long[]> l10 = tourneyProcessor.splitByGroups(Arrays.asList(101L, 102L, 103L, 104L, 105L, 106L, 107L, 108L, 109L, 110L, 111L));
        assertEquals(3, l10.size());
        assertArrayEquals(new long[]{101L, 102L, 103L, 104L}, l10.get(0));
        assertArrayEquals(new long[]{105L, 106L, 107L, 108L}, l10.get(1));
        assertArrayEquals(new long[]{109L, 110L, 111L}, l10.get(2));

        final List<long[]> l11 = tourneyProcessor.splitByGroups(Arrays.asList(101L, 102L, 103L, 104L, 105L, 106L, 107L, 108L, 109L, 110L, 111L, 112L));
        assertEquals(3, l11.size());
        assertArrayEquals(new long[]{101L, 102L, 103L, 104L}, l11.get(0));
        assertArrayEquals(new long[]{105L, 106L, 107L, 108L}, l11.get(1));
        assertArrayEquals(new long[]{109L, 110L, 111L, 112L}, l11.get(2));

        final List<long[]> l12 = tourneyProcessor.splitByGroups(Arrays.asList(101L, 102L, 103L, 104L, 105L, 106L, 107L, 108L, 109L, 110L, 111L, 112L, 113L));
        assertEquals(4, l12.size());
        assertArrayEquals(new long[]{101L, 102L, 103L, 104L}, l12.get(0));
        assertArrayEquals(new long[]{105L, 106L, 107L}, l12.get(1));
        assertArrayEquals(new long[]{108L, 109L, 110L}, l12.get(2));
        assertArrayEquals(new long[]{111L, 112L, 113L}, l12.get(3));

        final List<Long> players = new ArrayList<Long>();
        for (long i = 0; i < 13487; i++) {
            players.add(i);
        }
        final List<long[]> l13 = tourneyProcessor.splitByGroups(players);
        assertEquals(3372, l13.size());
        assertEquals(4, l13.get(3370).length);
        assertEquals(3, l13.get(3371).length);
    }
}
