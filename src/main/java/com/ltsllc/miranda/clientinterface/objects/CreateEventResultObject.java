package com.ltsllc.miranda.clientinterface.objects;

import com.ltsllc.miranda.clientinterface.objects.ResultObject;

/**
 * Created by Clark on 6/8/2017.
 */
public class CreateEventResultObject extends ResultObject {
    private String guid;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
