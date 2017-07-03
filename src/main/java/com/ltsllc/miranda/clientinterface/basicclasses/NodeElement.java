/*
 * Copyright 2017 Long Term Software LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ltsllc.miranda.clientinterface.basicclasses;

import com.google.gson.Gson;
import com.ltsllc.common.util.ImprovedRandom;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Clark on 1/21/2017.
 */
public class NodeElement extends MirandaObject {
    private static SimpleDateFormat ourSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd@HH:mm:ss.SSS");

    private String dns;
    private int port;
    private String description;
    private long lastConnected;

    public long getLastConnected() {
        return lastConnected;
    }

    public void setLastConnected(long lastConnected) {
        this.lastConnected = lastConnected;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public NodeElement (String dns, int port, String description) {
        this.dns = dns;
        this.port = port;
        this.description = description;
        this.lastConnected = -1;
    }

    public boolean equals(Object o) {
        if (!equals(o))
            return false;

        NodeElement other = (NodeElement) o;

        if (!stringsAreEqual(getDescription(), other.getDescription()))
            return false;

        if (!stringsAreEqual(getDns(), other.getDns()))
            return false;

        if (getPort() != other.getPort())
            return false;

        if (getLastConnected() != other.getLastConnected())
            return false;

        return true;
    }

    /**
     * Overwrite this object's attributes with another object's attributes.
     *
     * @param o The other object to merge with.
     * @throws MergeException
     * @see MirandaObject
     */
    public void mergeFavorOther (Object o) throws MergeException {
        super.mergeFavorOther(o);

        NodeElement other = (NodeElement) o;

        if (!stringsAreEqual(getDns(), other.getDns()))
            setDns(other.getDns());

        if (!stringsAreEqual(getDescription(), other.getDescription()))
            setDescription(other.getDescription());

        if (getPort() != other.getPort())
            setPort(other.getPort());

        if (getLastConnected() != other.getLastConnected())
            setLastConnected(other.getLastConnected());
    }

    public boolean hasTimedout (long timeout) {
        Date date = new Date();
        long now = date.getTime();
        long timeSinceLastConnect = now - getLastConnected();
        return timeSinceLastConnect >= timeout;
    }

    /**
     * false.
     *
     * Objects of this class do not expire.  Instead, during a health check,
     * an element may have not connected in an acceptable time frame and
     * therefore get dropped.
     *
     * @param time
     * @return
     */
    public boolean expired (long time) {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("NodeElement {");
        stringBuilder.append("dns: ");
        stringBuilder.append(getDns());
        stringBuilder.append(", port: ");
        stringBuilder.append(getPort());
        stringBuilder.append(", last connect: ");
        Date date = new Date(getLastConnected());
        stringBuilder.append(0 == getLastConnected() ? "never" : ourSimpleDateFormat.format(date));
        stringBuilder.append(", description: ");
        stringBuilder.append(getDescription());
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    private static final String[] FISRT_DOMAINS = {
            "foo",
            "bar",
            "whatever",
            "nerts",
            "liwipi",
            "fred",
            "node",
            "junk",
            "curley",
            "amy"
    };

    private static final String[] MIDDLE_DOMIANS = {
            "krabnebula",
            "ever",
            "intermediate",
            "google",
            "amazon",
            "bell",
            "uswaste",
            "meetup",
            "experts",
            "beginners"
    };

    private static final String[] LAST_DOMAINS = {
            "com",
            "net",
            "org"
    };

    public static String randomDnsName (ImprovedRandom random) {
        int numberOfDomains = random.nextInt(2, 6);
        StringBuffer sb = new StringBuffer();
        sb.append (FISRT_DOMAINS[random.nextIndex(FISRT_DOMAINS)]);

        int i = 1;
        while (i < (numberOfDomains - 1)) {
            sb.append(".");
            sb.append(MIDDLE_DOMIANS[random.nextIndex(MIDDLE_DOMIANS)]);
            i++;
        }

        sb.append(".");
        sb.append(LAST_DOMAINS[random.nextIndex(LAST_DOMAINS)]);

        return sb.toString();
    }

    public static NodeElement random (ImprovedRandom random) {
        String dns = randomDnsName(random);
        int port = 1 + random.nextInt(65535);

        return new NodeElement(dns, port, "a node");
    }

    public boolean equivalent (Object o) {
        if (null == o || !(o instanceof NodeElement))
            return false;

        NodeElement other = (NodeElement) o;
        return matches(other);
    }

    public void updateFrom (NodeElement other) {
        setLastConnected(other.getLastConnected());
        setDescription(other.getDescription());
    }

    public boolean matches (NodeElement other) {
        return getDns().equals(other.getDns()) && getPort() == other.getPort();
    }
}
