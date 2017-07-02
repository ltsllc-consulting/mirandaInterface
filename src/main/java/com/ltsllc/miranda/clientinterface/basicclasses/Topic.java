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

/**
 * Created by Clark on 1/5/2017.
 */
public class Topic extends MirandaObject {
    public enum RemotePolicies {
        Immediate,
        Acknowledged,
        Written
    }


    private static Gson ourGson = new Gson();

    private String name;
    private String owner;
    private RemotePolicies remotePolicy;

    public RemotePolicies getRemotePolicy() {
        return remotePolicy;
    }

    public void setRemotePolicy(RemotePolicies remotePolicy) {
        this.remotePolicy = remotePolicy;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Topic (String name) {
        this.name = name;
    }

    public Topic (String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    public void updateFrom (Topic other) {
        setOwner(other.getOwner());
        setRemotePolicy(other.getRemotePolicy());
    }

    public boolean matches (Topic other) {
        if (!super.matches(other))
            return false;

        return getName().equals(other.getName()) && getOwner().equals(other.getOwner());
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o))
            return false;

        Topic other = (Topic) o;

        if (!stringsAreEqual(getName(), other.getName()))
            return false;

        if (!stringsAreEqual(getOwner(), other.getOwner()))
            return false;

        if (getRemotePolicy() != other.getRemotePolicy())
            return false;

        return true;
    }

    @Override
    public void mergeFavorOther(Object o) throws MergeException {
        super.mergeFavorOther(o);

        Topic other = (Topic) o;

        if (!stringsAreEqual(getName(), other.getName()))
            setName(other.getName());

        if (!stringsAreEqual(getOwner(), other.getOwner()))
            setOwner(other.getOwner());

        if (getRemotePolicy() != other.getRemotePolicy())
            setRemotePolicy(other.getRemotePolicy());
    }

    public String toJson() {
        return ourGson.toJson(this);
    }

    private static final String[] NAMES = {
            "whatever",
            "users",
            "subjects",
            "books",
            "people",
            "cars",
            "locations",
            "cats",
            "dogs",
            "gerbils"
    };

    private static final String[] OWNERS = {
            "whatever",
            "joe",
            "princess",
            "arnold",
            "sam",
            "steve",
            "hedi",
            "mushroom",
            "oscar",
            "clark"
    };

    public static Topic random (ImprovedRandom improvedRandom) {
        String name = NAMES[improvedRandom.nextIndex(NAMES)];
        String owner = OWNERS[improvedRandom.nextIndex(OWNERS)];

        return new Topic(name, owner);
    }
}
