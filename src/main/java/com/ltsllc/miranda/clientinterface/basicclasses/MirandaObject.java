package com.ltsllc.miranda.clientinterface.basicclasses;

import com.google.gson.Gson;

/**
 * An Object that knows how to merge itself with another object.
 * <p>
 * <h3>Properties</h3>
 * <table border="1">
 * <th>
 * <td>Name</td>
 * <td>Type</td>
 * <td>Description</td>
 * </th>
 * <tr>
 * <td>lastChange</td>
 * <td>Long</td>
 * <td>The Java time when the object was last changed.</td>
 * </tr>
 * </table>
 */
public class MirandaObject implements Matchable {
    private static Gson gson;

    private Long lastChange;

    public static Gson getGson() {
        return gson;
    }

    public Long getLastChange() {
        return lastChange;
    }

    public void setLastChange(Long lastChange) {
        if (lastChange == null)
            this.lastChange = null;

        this.lastChange = new Long(lastChange.longValue());
    }

    @Override
    public boolean matches(Object o) {
        if (o == null)
            return false;

        if (getClass() != o.getClass())
            return false;

        MirandaObject other = (MirandaObject) o;
        return getLastChange() == other.getLastChange();
    }

    /**
     * Is the object equal to another object.
     *
     * @see Object#equals(Object)
     */
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (o == null)
            return false;

        if (o.getClass() != getClass())
            return false;

        MirandaObject other = (MirandaObject) o;

        return longObjectsAreEquivalent(getLastChange(), other.getLastChange());
    }

    /**
     * Merge with another instance of this class, using the other instance's attributes if there is
     * a difference.
     *
     * @param o The other object to merge with.
     * @exception MergeException If the other object is null or not of the same class as this instance.
     */
    public void mergeFavorOther(Object o) throws MergeException {
        if (null == o)
            throw new MergeException("null other oject");

        if (o.getClass() != getClass()) {
            throw new MergeException("Other is of wrong class.  Was expecting: " + getClass().getName() + " found: "
                    + o.getClass().getName());
        }

        MirandaObject other = (MirandaObject) o;

        if (!longObjectsAreEquivalent(getLastChange(), other.getLastChange()))
            setLastChange(other.getLastChange());
    }

    /**
     * Throw a {@link MergeException} that indicates that we are not equal to another object.
     *
     * @throws MergeException this Exception is always thrown, and it indicates that this
     *                        instance is not equal to another instance.
     */
    public void throwMergeException() throws MergeException {
        throw new MergeException("an attribute of the object to merge with was different");
    }

    /**
     * Are two strings equal?
     * <p>
     * <p>
     * This method returns true if
     * the two strings are == equivalent
     * or the two strings are both not null and {@link String#equals(Object)} returns true
     * </p>
     *
     * @param s1 The first string to be compared.
     * @param s2 The second string to be copared.
     * @return true if the strings are equivalent, false otherwise.
     */
    public static boolean stringsAreEqual(String s1, String s2) {
        if (s1 == s2)
            return true;

        if (s1 == null || s2 == null)
            return false;

        return s1.equals(s2);
    }

    /**
     * Are two Long objects equivalent?
     * <p>
     * <p>
     * This method returns true if the two longs are == equivalent or
     * the long values they contain are == equivalent.
     * </p>
     *
     * @param l1 The first Long to compare
     * @param l2 The second Long to compare
     * @return True if the two objects are equivalent, false otherwise.
     */
    public static boolean longObjectsAreEquivalent(Long l1, Long l2) {
        if (l1 == l2)
            return true;
        else if (l1 == null || l2 == null)
            return false;
        else {
            return l1.longValue() == l2.longValue();
        }
    }

    /**
     * Are two byte arrays equivalent?
     * <p>
     * <p>
     * This method returns true if the two arrays are == equivelent or if
     * they both have the same length and contain the same values.
     * </p>
     *
     * @param a1 The first byte array to compare.
     * @param a2 The second byte array to compare.
     * @return True if the arrays are equivalent.  False otherwise.
     */
    public static boolean byteArraysAreEqual(byte[] a1, byte[] a2) {
        if (a1 == a2)
            return true;

        if ((a1 == null) || (a2 == null))
            return false;

        if (a1.length != a2.length)
            return false;

        for (int i = 0; i < a1.length; i++) {
            if (a1[i] != a2[i])
                return false;
        }

        return true;
    }

    /**
     * Merge this object with another object.
     * <p>
     * <p>
     * This method makes no changes if any of the following are true:
     * </p>
     * <ul>
     * <li>The other object is null</li>
     * <li>The other object is == equivalent to this instance</li>
     * <li>The other object is {@link #equals(Object)} equivalent to this instance.</li>
     * <li>This object was changed more recently than the other object</li>
     * </ul>
     * <p>
     * <p>
     * The method overwrites the attributes of this object with the attributes of the other object if
     * </p>
     * <ul>
     *     <li>This object has never been changed and the other object has</li>
     *     <li>The other object has been chaged more recently than this object</li>
     * </ul>
     *
     * <p>
     *     This method throws an exception if
     * </p>
     * <ul>
     *     <li>The change times of both objects are the same, but the objects are not equivalent</li>
     * </ul>
     *
     * @param o The other object to merge with.
     * @throws MergeException If the two objects have the same modification time but are not equivalent.
     * @throws IllegalArgumentException If the other object is not of the same class as this object.
     */
    public void merge(Object o) throws MergeException {
        if (null == o)
            return;

        if (getClass() != o.getClass()) {
            throw new IllegalArgumentException("Wrong class.  Was expecting: " + getClass().getName()
                    + " got: " + o.getClass().getName());
        }

        MirandaObject other = (MirandaObject) o;

        if (o == this)
            return;
        else if (equals(o))
            return;
        else if (longObjectsAreEquivalent(getLastChange(), other.getLastChange()))
            throw new MergeException("two objects have the same modification time but are not eqivalent");
        else if (null == other.getLastChange())
            return;
        else if (null == getLastChange())
            mergeFavorOther(other);
        else if (other.getLastChange() < getLastChange())
            return;
        else if (other.getLastChange() > getLastChange())
            mergeFavorOther(other);
        else {
            throw new MergeException("impossible case");
        }
    }

    public String toJson () {
        return getGson().toJson(this);
    }
}
