package wisematches.client.gwt.app.client.settings;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class SettingsTest {
/*
    private SettingsManagerTool managerTool;

    private ParameterInfo[] infos = new ParameterInfo[]{
            new ParameterInfo("test1", "t1", int.class, 1, true),
            new ParameterInfo("test2", "t2", int.class, 2, false),
            new ParameterInfo("test3", "t3", long.class, 2L, false),
            new ParameterInfo("test4", "t4", String.class, "test", false),
            new ParameterInfo("test5", "t5", boolean.class, true, true),
            new ParameterInfo("test6", "t6", int[].class, new int[]{1, 2}, true),
            new ParameterInfo("test7", "t7", long[].class, new long[]{3, 4}, false),
            new ParameterInfo("test8", "t8", String[].class, new String[]{"a", "b"}, false),
            new ParameterInfo("test9", "t9", boolean[].class, true)
    };

	@Before
    protected void setUp() throws Exception {
        managerTool = createStrictMock(SettingsManagerTool.class);
    }

	@Test
    public void test_separateString() {
        Settings s = new Settings(managerTool, "test", infos, null, null);

        final String[] strings = s.separateString("a=1:b=2:c=1|2|3|4|:d=this is test", ":");
        Assert.assertEquals("a=1", strings[0]);
        Assert.assertEquals("b=2", strings[1]);
        Assert.assertEquals("c=1|2|3|4|", strings[2]);
        Assert.assertEquals("d=this is test", strings[3]);

        final String[] strings2 = s.separateString("1|2|3|4|", "|");
        Assert.assertEquals("1", strings2[0]);
        Assert.assertEquals("2", strings2[1]);
        Assert.assertEquals("3", strings2[2]);
        Assert.assertEquals("4", strings2[3]);

        final String[] strings3 = s.separateString("1|2|3|4", "|");
        Assert.assertEquals("1", strings3[0]);
        Assert.assertEquals("2", strings3[1]);
        Assert.assertEquals("3", strings3[2]);
        Assert.assertEquals("4", strings3[3]);
    }

    public void test_decodeSettings() {
        Settings s = new Settings(managerTool, "test", infos, "t1=3:t3=5:t4=this is test:t5=0:t6=1|2|3|:t7=4|5|6|:t8=asd|qwe asd|tyu|:t9=1|0|1|", null);

        Assert.assertEquals(3, s.getInt("test1"));
        Assert.assertEquals(2, s.getInt("test2"));
        Assert.assertEquals(5L, s.getLong("test3"));
        Assert.assertEquals("this is test", s.getString("test4"));
        Assert.assertEquals(false, s.getBoolean("test5"));
        Assert.assertArrayEquals(new int[]{1, 2, 3}, s.getIntArray("test6"));
        Assert.assertArrayEquals(new long[]{4, 5, 6}, s.getLongArray("test7"));
        Assert.assertArrayEquals(new String[]{"asd", "qwe asd", "tyu"}, s.getStringArray("test8"));
        Assert.assertTrue(s.getBooleanArray("test9")[0]);
        Assert.assertFalse(s.getBooleanArray("test9")[1]);
        Assert.assertTrue(s.getBooleanArray("test9")[2]);

        Assert.assertEquals("t1=3:t5=0:t6=1|2|3|:t9=1|0|1|", s.getServerSettings());
        Assert.assertEquals("t2=2:t3=5:t4=this is test:t7=4|5|6|:t8=asd|qwe asd|tyu|", s.getClientSettings());

        s = new Settings(managerTool, "test", infos, "t6=null:t7=null:t8=null:t9=null", null);

        Assert.assertNull(s.getIntArray("test6"));
        Assert.assertNull(s.getLongArray("test7"));
        Assert.assertNull(s.getStringArray("test8"));
        Assert.assertNull(s.getBooleanArray("test9"));

        Assert.assertEquals("t1=1:t5=1:t6=null:t9=null", s.getServerSettings());
        Assert.assertEquals("t2=2:t3=2:t4=test:t7=null:t8=null", s.getClientSettings());
    }

    public void test_encodeSettings() {
        Settings s = new Settings(managerTool, "test", infos, "", null);

        s.setInt("test1", 3);
        s.setInt("test2", 12);
        s.setLong("test3", 5L);
        s.setString("test4", "this is test");
        s.setBoolean("test5", false);
        s.setIntArray("test6", new int[]{1, 2, 3});
        s.setLongArray("test7", new long[]{4, 5, 6});
        s.setStringArray("test8", new String[]{"asd", "qwe asd", "tyu"});
        s.setBooleanArray("test9", new boolean[]{true, false, true});

        Assert.assertEquals("t1=3:t5=0:t6=1|2|3|:t9=1|0|1|", s.getServerSettings());
        Assert.assertEquals("t2=12:t3=5:t4=this is test:t7=4|5|6|:t8=asd|qwe asd|tyu|", s.getClientSettings());

        Assert.assertEquals(3, s.getInt("test1"));
        Assert.assertEquals(12, s.getInt("test2"));
        Assert.assertEquals(5L, s.getLong("test3"));
        Assert.assertEquals("this is test", s.getString("test4"));
        Assert.assertEquals(false, s.getBoolean("test5"));
        Assert.assertArrayEquals(new int[]{1, 2, 3}, s.getIntArray("test6"));
        Assert.assertArrayEquals(new long[]{4, 5, 6}, s.getLongArray("test7"));
        Assert.assertArrayEquals(new String[]{"asd", "qwe asd", "tyu"}, s.getStringArray("test8"));
        Assert.assertTrue(s.getBooleanArray("test9")[0]);
        Assert.assertFalse(s.getBooleanArray("test9")[1]);
        Assert.assertTrue(s.getBooleanArray("test9")[2]);


        s.setLongArray("test7", null);
        Assert.assertEquals("t1=3:t5=0:t6=1|2|3|:t9=1|0|1|", s.getServerSettings());
        Assert.assertEquals("t2=12:t3=5:t4=this is test:t7=null:t8=asd|qwe asd|tyu|", s.getClientSettings());

        Assert.assertNull(s.getLongArray("test7"));
    }

    public void test_changeValue() {
        Settings s = new Settings(managerTool, "test", infos, null, null);

        try {
            s.setInt("test5", 1);
            fail("IllegalArgumentException must be here: test5 is boolean");
        } catch (IllegalArgumentException ex) {
            ;
        }

        replay(managerTool);
        s.setBoolean("test5", Boolean.TRUE);
        verify(managerTool);

        reset(managerTool);
        managerTool.settingsChanged(s, infos[4]);
        replay(managerTool);
        s.setBoolean("test5", Boolean.FALSE);
        verify(managerTool);

        Assert.assertFalse(s.getBoolean("test5"));
    }
*/
}
