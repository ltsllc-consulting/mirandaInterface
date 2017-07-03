package com.ltsllc.miranda.clientinterface.test;

/**
 * Created by ltsllc on 7/3/2017.
 */
public class TestCase {
    public boolean byteArraysAreEqual (byte[] a1, byte[] a2) {
        if (a1 == a2)
            return true;

        if (null == a1 || null == a2)
            return false;

        if (a1.length != a2.length)
            return false;

        for (int i = 0; i < a1.length; i++) {
            if (a1[i] != a2[i])
                return false;
        }

        return true;
    }


    public void reset () {}
    public void setup () {}
}
