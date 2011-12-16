/*
 ****************************************************************************
 * Ldap Synchronization Connector provides tools to synchronize
 * electronic identities from a list of data sources including
 * any database with a JDBC connector, another LDAP directory,
 * flat files...
 *
 *                  ==LICENSE NOTICE==
 * 
 * Copyright (c) 2008 - 2011 LSC Project 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:

 *    * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of the LSC Project nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *                  ==LICENSE NOTICE==
 *
 *               (c) 2008 - 2011 LSC Project
 *         Sebastien Bahloul <seb@lsc-project.org>
 *         Thomas Chemineau <thomas@lsc-project.org>
 *         Jonathan Clarke <jon@lsc-project.org>
 *         Remy-Christophe Schermesser <rcs@lsc-project.org>
 ****************************************************************************
 */
package org.lsc.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;

import org.lsc.LscDatasets;

/**
 * General interface of a bean. A bean in LSC describes an object in a
 * source or destination. It has nothing to do with Java beans (in fact,
 * LSC beans do not have any get- or set- methods).
 * 
 * <p>
 * An object is described in this bean as datasets (set of named values)
 * and main identifier.
 * </p>
 * 
 * <p>
 * Most methods are convenience methods to get the values of each dataset.
 * </p>
 * 
 * @author Sebastien Bahloul &lt;seb@lsc-project.org&gt;
 * @author Jonathan Clarke &lt;jonathan@phillipoux.net&gt;
 */
public interface IBean extends Cloneable, Serializable {

	/**
	 * Get an attribute from its name.
	 * 
	 * @param id
	 *            the name
	 * @return the LDAP attribute
	 * @deprecated
	 */
	Attribute getAttributeById(final String id);

	Set<Object> getDatasetById(final String id);

	/**
	 * Get an attribute from its name as a Set.
	 *
	 * @param id the name
	 * @return the LDAP attribute
	 * @deprecated
	 */
	Set<Object> getAttributeAsSetById(final String id);

	/**
	 * Get the <b>first</b> value of an attribute from its name
	 * 
	 * @param id
	 *            The attribute name (case insensitive)
	 * @return String The first value of the attribute, or the empty string ("")
	 * @throws NamingException
	 * @deprecated
	 */
	public String getAttributeFirstValueById(final String id)
					throws NamingException;

	/**
	 * Get the <b>first</b> value of an attribute from its name
	 * 
	 * @param id
	 *            The attribute name (case insensitive)
	 * @return String The first value of the attribute, or the empty string ("")
	 * @throws NamingException
	 */
	public String getDatasetFirstValueById(final String id)
					throws NamingException;

	/**
	 * Get all values of an attribute from its name
	 * 
	 * @param id
	 *            The attribute name (case insensitive)
	 * @return List<String> List of attribute values, or an empty list
	 * @throws NamingException
	 * @deprecated
	 */
	public List<String> getAttributeValuesById(final String id)
					throws NamingException;

	/**
	 * Get the attributes name.
	 * 
	 * @return a set containing all the attributes name
	 */
	Set<String> getAttributesNames();

	/**
	 * Set an attribute.
	 * 
	 * @param attr
	 *            the attribute to set
	 */
	void setAttribute(Attribute attr);

	/**
	 * Set an attribute.
	 *
	 * @param attrName The attribute name.
	 * @param attrValues A set of values for the attribute.
	 */
	void setAttribute(String attrName, Set<Object> attrValues);

	/**
	 * Get the distinguished name.
	 * 
	 * @return the distinguished name
	 */
	String getDistinguishedName();

	/**
	 * Set the distinguished name.
	 * 
	 * @param dn The distinguishedName to set
	 */
	void setDistinguishName(String dn);

	void setDatasets(LscDatasets datasets);
	
	LscDatasets datasets();
	
	String getMainIdentifier();

	void setMainIdentifier(String mainIdentifier);

	/**
	 * Clone this object.
	 * 
	 * @return Object
	 * @throws CloneNotSupportedException 
	 */
	IBean clone() throws CloneNotSupportedException;
}
