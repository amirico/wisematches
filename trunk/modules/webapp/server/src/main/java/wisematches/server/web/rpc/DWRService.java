/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.rpc;

/**
 * @author klimese
 */
public class DWRService {
    private String state;

    public DWRService() {
    }

    public Address getAddress() throws Exception {
        Address address = new Address();
        address.setStreet("2245 NW Overlook Drive");
        address.setCity("Portland");
        address.setState(state);
        return address;
    }

    public void setState(String state) {
        this.state = state;
    }
}
