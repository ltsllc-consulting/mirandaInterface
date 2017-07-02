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

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by Clark on 1/5/2017.
 */

/**
 * A successful delivery of an {@link Event} to a {@lin Subscription}.
 */
public class Delivery extends MirandaObject {
    private static SecureRandom ourRandom = new SecureRandom();

    private String guid;
    private String attemptId;
    private long delivered;
    private String subscription;

    public Delivery (Event event, long delivered, Subscription subscription) {
        this.attemptId = event.getGuid();
        this.guid = UUID.randomUUID().toString();
        this.delivered = delivered;
        this.subscription = subscription.getName();
    }

    public Delivery (String deliveryId, String eventId, long time, String subscription) {
        this.guid = deliveryId;
        this.attemptId = eventId;
        this.delivered = time;
        this.subscription = subscription;
    }

    public String getAttemptId() {
        return attemptId;
    }

    public long getDelivered() {
        return delivered;
    }

    public String getGuid() {
        return guid;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setAttemptId(String attemptId) {
        this.attemptId = attemptId;
    }

    public void setDelivered(long delivered) {
        this.delivered = delivered;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public boolean equalTo (Delivery other) {
        if (!stringsAreEqual(other.getGuid(), getGuid()))
            return false;

        if (!stringsAreEqual(other.getAttemptId(), getAttemptId()))
            return false;

        if (other.getDelivered() != getDelivered())
            return false;

        if (!stringsAreEqual(other.getSubscription(), getSubscription()))
            return false;

        return true;
    }

    public void mergeFavorOther(Delivery other) {
        if (getDelivered() != other.getDelivered())
            setDelivered(other.getDelivered());

        if (!stringsAreEqual(getGuid(), other.getGuid()))
            setGuid(other.getGuid());

        if (!stringsAreEqual(getAttemptId(), other.getAttemptId()))
            setAttemptId(other.getAttemptId());

        if (!stringsAreEqual(getSubscription(), other.getSubscription()))
            setSubscription(other.getSubscription());
    }

    /**
     *  Like the {@link Event} it is based on, an object of this class never expires.
     *
     * @param time The time to compare to.
     * @return This method always returns flse.
     */
    public boolean expired(long time) {
        return false;
    }

    public void updateFrom (Delivery other) {
        throw new IllegalStateException("updateFrom is not applicable for Delivery");
    }

    public boolean matches (Delivery other) {
        return getGuid().equals(other.getGuid());
    }

    public static Delivery createRandomDelivery () {
        String guid = UUID.randomUUID().toString();
        String eventId = UUID.randomUUID().toString();
        long deliveryTime = ourRandom.nextLong();
        String subscriptionId = UUID.randomUUID().toString();

        Delivery delivery = new Delivery(guid, eventId, deliveryTime, subscriptionId);
        return delivery;
    }
}
