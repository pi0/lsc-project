/*
 ****************************************************************************
 * Ldap Synchronization Connector provides tools to synchronize
 * electronic identities from a list of data sources including
 * any database with a JDBC connector, another LDAP directory,
 * flat files...
 *
 *                  ==LICENSE NOTICE==
 * 
 * Copyright (c) 2008, LSC Project 
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
 *               (c) 2008 - 2009 LSC Project
 *         Sebastien Bahloul <seb@lsc-project.org>
 *         Thomas Chemineau <thomas@lsc-project.org>
 *         Jonathan Clarke <jon@lsc-project.org>
 *         Remy-Christophe Schermesser <rcs@lsc-project.org>
 ****************************************************************************
 */
package org.lsc.jndi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

import org.lsc.LscAttributes;
import org.lsc.beans.IBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a generic but configurable implementation to read data from the destination directory.
 * 
 * You can specify where (baseDn) and what (filterId & attr) information will be read on which type of entries
 * (filterAll and attrId).
 * 
 * @author Sebastien Bahloul &lt;seb@lsc-project.org&gt;
 */
public class SimpleJndiDstService extends AbstractSimpleJndiService implements IJndiWritableService {

	/**
	 * Preceding the object feeding, it will be instantiated from this class.
	 */
	private Class<IBean> beanClass;

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleJndiDstService.class);

	/**
	 * Constructor adapted to the context properties and the bean class name to instantiate.
	 * 
	 * @param props
	 *            the properties used to identify the directory parameters and context
	 * @param beanClassName
	 *            the bean class name that will be instantiated and feed up
	 */
	@SuppressWarnings({ "unchecked" })
	public SimpleJndiDstService(final Properties props, final String beanClassName) {
		super(props);
		try {
			this.beanClass = (Class<IBean>) Class.forName(beanClassName);
		} catch (ClassNotFoundException e) {
			LOGGER.error("Bean class {} not found. Check this class name really exists.", beanClassName);
			throw new RuntimeException(e);
		}
	}

	/**
	 * The simple object getter according to its identifier.
	 * 
	 * @param pivotName Name of the entry to be returned, which is the name returned by
	 *            {@link #getListPivots()} (used for display only)
	 * @param pivotAttributes Map of attribute names and values, which is the data identifier in the
	 *            source such as returned by {@link #getListPivots()}. It must identify a unique
	 *            entry in the source.
	 * @return The bean, or null if not found
	 * @throws NamingException May throw a {@link NamingException} if the object is not found in the
	 *             directory, or if more than one object would be returned.
	 */
	public final IBean getBean(String pivotName, LscAttributes pivotAttributes) throws NamingException {
		try {
			SearchResult srObject = get(pivotName, pivotAttributes);
			Method method = beanClass.getMethod("getInstance", 
							new Class[] { SearchResult.class, String.class, Class.class });
			return (IBean) method.invoke(null, new Object[] { srObject, getBaseDn(), beanClass });
		} catch (SecurityException e) {
			LOGGER.error("Unable to get static method getInstance on {} ! This is probably a programmer's error ({})",
							beanClass.getName(), e.toString());
			LOGGER.debug(e.toString(), e);
		} catch (NoSuchMethodException e) {
			LOGGER.error("Unable to get static method getInstance on {} ! This is probably a programmer's error ({})",
							beanClass.getName(), e.toString());
			LOGGER.debug(e.toString(), e);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Unable to get static method getInstance on {} ! This is probably a programmer's error ({})",
							beanClass.getName(), e.toString());
			LOGGER.debug(e.toString(), e);
		} catch (IllegalAccessException e) {
			LOGGER.error("Unable to get static method getInstance on {} ! This is probably a programmer's error ({})",
							beanClass.getName(), e.toString());
			LOGGER.debug(e.toString(), e);
		} catch (InvocationTargetException e) {
			LOGGER.error("Unable to get static method getInstance on {} ! This is probably a programmer's error ({})",
							beanClass.getName(), e.toString());
			LOGGER.debug(e.toString(), e);
		}
		return null;
	}

	/**
	 * Destination LDAP Services getter.
	 * 
	 * @return the Destination JndiServices object used to apply directory operations
	 */
	public final JndiServices getJndiServices() {
		return JndiServices.getDstInstance();
	}

	/**
	 * Returns a list of all the objects' identifiers.
	 * 
	 * @return Map of all entries names that are returned by the directory with an associated map of
	 *         attribute names and values (never null)
	 * @throws NamingException May throw a {@link NamingException} if an error occurs while
	 *             searching the directory.
	 */
	public Map<String, LscAttributes> getListPivots() throws NamingException {
		return this.getListPivots(getJndiServices());
    }

	/**
	 * Apply directory modifications.
	 *
	 * @param jm Modifications to apply in a {@link JndiModifications} object.
	 * @return Operation status
	 * @throws CommunicationException If the connection to the service is lost,
	 * and all other attempts to use this service should fail.
	 */
	public boolean apply(JndiModifications jm) throws CommunicationException {
		return getJndiServices().apply(jm);
	}
}