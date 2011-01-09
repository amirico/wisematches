package wisematches.server.core.sessions.impl;

import wisematches.server.core.sessions.PlayerSessionBean;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ValidInterface1 extends PlayerSessionBean {
    int getInt();

    void setInt(int i);

    long getLong();

    void setLong(long l);
}
