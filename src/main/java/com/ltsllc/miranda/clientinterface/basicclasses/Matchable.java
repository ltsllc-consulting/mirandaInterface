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

/**
 * An object that can match another object.
 *
 * <p>
 *     Two objects are match equivalent if their primary keys match. For example,
 *     two {@link Event}s match if their guids match.  Two {@link NodeElement}s match
 *     if they have the same DNS names and the same port number.
 * </p>
 *
 * <p>
 *     The primary difference between the match methods and the {@link Object#equals(Object)}
 *     method is that two ojects are equals equivalent if all their attributes are
 *     equals equivalent whereas two objects are match equivalent if only the
 *     attributes that make up their primary keys are equals equivalent.
 * </p>
 */
public interface Matchable {
    /**
     * Is this object match equivalent to another object?
     *
     * <p>
     *     To be match equivalent, the other object should not be null and should
     *     be able to return true to the instanceof operator.
     * </p>
     *
     * @param o The other object to compare against.
     *
     * @return true if the two objects are match equivalent, false otherwise.
     */
    public boolean matches(Object o);
}
