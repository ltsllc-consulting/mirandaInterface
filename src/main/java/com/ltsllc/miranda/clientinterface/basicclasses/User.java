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

import com.ltsllc.common.util.Utils;
import com.ltsllc.miranda.clientinterface.MirandaException;
import com.ltsllc.miranda.clientinterface.objects.UserObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * A user of the Miranda system.
 *
 * <h3>Attributes</h3>
 * <table border="1">
 *     <th>
 *         <td>Name</td>
 *         <td>Type</td>
 *         <td>Description</td>
 *     </th>
 *     <tr>
 *         <td>name</td>
 *         <td>String</td>
 *         <td>
 *             The name of the user.
 *
 *             <p>
 *                 This must be unique across all Users in the sytem.
 *             </p>
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>category</td>
 *         <td>enum</td>
 *         <td>
 *             The category of the user.
 *
 *             <p>
 *                 Allowable values are Publisher, Subscriber and Admin.
 *                 Nobody should not be used but is there for completeness.
 *             </p>
 *
 *             <p>
 *                 Publishers can create topics and Events.
 *             </p>
 *
 *             <p>
 *                 Subscribers can create Subscriptions.
 *             </p>
 *
 *             <p>
 *                 Admins can do anything.
 *             </p>
 *
 *             <p>
 *                 Users with a category of Nobody cannot do anything.
 *             </p>
 *         </td>
 *     </tr>
 *
 *     <tr>
 *         <td>description</td>
 *         <td>String</td>
 *         <td>A user friendly description of the User.</td>
 *     </tr>
 *
 *     <tr>
 *         <td>publicKey</td>
 *         <td>PublicKey</td>
 *         <td>
 *             The public key associated with the user.
 *
 *             <p>
 *                 The public key is used during logins to encrypt the session ID sent back to the user.
 *                 The user uses their private key in all their requests.
 *             </p>
 *         </td>
 *     </tr>
 * </table>
 */
public class User extends MirandaObject {
    public enum UserTypes {
        Publisher,
        Subscriber,
        Admin,
        Nobody
    }

    private String name;
    private UserTypes category;
    private String description;
    private String publicKeyPem;
    private PublicKey publicKey;


    public String getPublicKeyPem() throws IOException {
        if (null == publicKeyPem)
            createPublicKeyPem();

        return publicKeyPem;
    }

    public void setPublicKeyPem(String publicKeyPem) {
        this.publicKeyPem = publicKeyPem;

        if (this.publicKeyPem != null)
            publicKey = null;
    }

    public UserTypes getCategory() {
        return category;
    }

    public void setCategory(UserTypes category) {
        this.category = category;
    }

    public void setCategory (String categoryString) {
        UserTypes category = UserTypes.valueOf(categoryString);
        this.category = category;
    }

    public PublicKey getPublicKey() throws IOException {
        if (null == publicKey)
            createPublicKey();

        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;

        if (this.publicKey != null)
            publicKeyPem = null;
    }

    public String getName() {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User (String name, String description) {
        this.name = name;
        this.description = description;
    }

    public User (String name, UserTypes category, String description, String publicKeyPem) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.publicKeyPem = publicKeyPem;
    }

    public User (String name, String categoryString, String description, String publicKeyPem) {
        this.name = name;

        UserTypes category = UserTypes.valueOf(categoryString);
        this.category = category;

        this.description = description;
        this.publicKeyPem = publicKeyPem;
    }


    public User (String name, UserTypes category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public User (String name, UserTypes category, String description, PublicKey publicKey) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.publicKey = publicKey;
    }

    public void createPublicKey () throws IOException {
        java.security.PublicKey jaPublicKey = Utils.pemStringToPublicKey(publicKeyPem);
        publicKey = new PublicKey(jaPublicKey);
    }

    public void createPublicKeyPem () throws IOException {
        publicKeyPem = Utils.publicKeyToPemString(publicKey.getSecurityPublicKey());
    }

    public User () {
    }

    public boolean equals (Object o) {
        if (!super.equals(o))
            return false;

        User other = (User) o;

        if (!stringsAreEqual(getName(), other.getName()))
            return false;

        if (getCategory() != other.getCategory())
            return false;

        if (!stringsAreEqual(getDescription(), other.getDescription()))
            return false;

        return true;
    }

    @Override
    public void mergeFavorOther(Object o) throws MergeException {
        super.mergeFavorOther(o);

        User other = (User) o;

        if (!stringsAreEqual(getDescription(), other.getDescription()))
            setDescription(other.getDescription());

        if (getCategory() != other.getCategory())
            setCategory(other.getCategory());

        if (!stringsAreEqual(getName(), other.getName()))
            setName(other.getName());
    }

    public static PublicKey toPublicKey (byte[] bytes) throws MirandaException {
        ObjectInputStream objectInputStream = null;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            java.security.PublicKey securityPublicKey = (java.security.PublicKey) objectInputStream.readObject();
            return new PublicKey(securityPublicKey);
        } catch (IOException | ClassNotFoundException e) {
            throw new MirandaException("Exception trying to deserialize public key", e);
        } finally {
            Utils.closeIgnoreExceptions(objectInputStream);
        }
    }

    public UserObject asUserObject () throws IOException {
        UserObject userObject = new UserObject();

        userObject.setName(getName());
        userObject.setDescription(getDescription());
        userObject.setCategory(getCategory().toString());
        userObject.setPublicKeyPem(getPublicKeyPem());

        return userObject;
    }

    public void updateFrom (UserObject userObject) throws MirandaException {
        setPublicKeyPem(userObject.getPublicKeyPem());
        setCategory(userObject.getCategory());
        setDescription(userObject.getDescription());
    }

    /*
    public void updateFrom (User other) {
        super.updateFrom(other);

        try {
            setPublicKey(other.getPublicKey());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setDescription(other.getDescription());
    }
    */
    public boolean matches (User other) {
        if (!super.matches(other))
            return false;

        return getName().equals(other.getName());
    }
}
