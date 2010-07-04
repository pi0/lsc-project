/*
 ****************************************************************************
 * Ldap Synchronization Connector provides tools to synchronize
 * electronic identities from a list of data sources including
 * any database with a JDBC connector, another LDAP directory,
 * flat files...
 *
 *                  ==LICENSE NOTICE==
 * 
 * Copyright (c) 2010, LSC Project 
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
 *               (c) 2008 - 2010 LSC Project
 *         Sebastien Bahloul <seb@lsc-project.org>
 *         Thomas Chemineau <thomas@lsc-project.org>
 *         Jonathan Clarke <jon@lsc-project.org>
 *         Remy-Christophe Schermesser <rcs@lsc-project.org>
 ****************************************************************************
 */
package org.lsc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchResult;

import org.lsc.jndi.JndiModificationType;
import org.lsc.jndi.JndiModifications;
import org.lsc.jndi.JndiServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to manage numeric sequences
 * via LDAP Directory entries storage
 * @author Sebastien Bahloul &gt;sbahloul@linagora.com&lt;
 */
public class SequencesFactory {

	/** the factory instance */
	private static SequencesFactory instance;
	/** the sequences cache */
	private Map<String, Sequence> sequences;
	/** the local Log4J logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(SequencesFactory.class);

	/**
	 * The local constructor
	 */
	private SequencesFactory() {
		sequences = new HashMap<String, Sequence>();
	}

	/**
	 * Get the factory instance (if needed create and initialize it)
	 * @return the instance
	 */
	public static SequencesFactory getInstance() {
		if (instance == null) {
			LOGGER.info("Initializing the sequences factory.");
			instance = new SequencesFactory();
		}
		return instance;
	}

	/**
	 * Get the next value for this sequence
	 * 
	 * @param dn DN where the sequence is stored in the directory
	 * @param attributeName The attribute name the sequence is stored in
	 * @return The next value, a negative value means an error
	 */
	public static int getNextValue(String dn, String attributeName) {
		String hash = getHash(dn, attributeName);
		LOGGER.debug("Getting the next value for the following sequence {}", hash);

		Sequence sq = getInstance().getSequence(dn, attributeName, hash);
		if (sq == null) {
			LOGGER.debug("Couldn't get the sequence {}. Returning -1.", hash);
			return -1;
		}

		return sq.getNextValue();
	}

	/**
	 * Get the current value for this sequence
	 * 
	 * @param dn DN where the sequence is stored in the directory
	 * @param attributeName The attribute name the sequence is stored in
	 * @return the current value, a negative value means an error
	 */
	public static int getCurrentValue(String dn, String attributeName) {
		String hash = getHash(dn, attributeName);
		LOGGER.debug("Getting the current value for the following sequence {}", hash);

		Sequence sq = getInstance().getSequence(dn, attributeName, hash);
		if (sq == null) {
			LOGGER.debug("Couldn't get the sequence {}. Returning -1.", hash);
			return -1;
		}

		return sq.getCurrentValue();
	}

	/**
	 * Private local method to get a sequence
	 * @param dn DN where the sequence is stored in the directory
	 * @param attributeName The attribute name the sequence is stored in
	 * @param hash A unique identifier for this sequence. See {@link #getHash(String, String)}.
	 * @return Sequence A Sequence object representing this entry
	 */
	private Sequence getSequence(String dn, String attributeName, String hash) {
		if (sequences.containsKey(hash)) {
			return sequences.get(hash);
		} else {
			Sequence seq = new Sequence();
			if (!seq.load(dn, attributeName, 0)) {
				return null;
			}
			sequences.put(hash, seq);
			return seq;	
		}
	}

	private static String getHash(String dn, String attributeName) {
		if (dn == null || attributeName == null) {
			return null;
		}
		return attributeName + "/" + dn;
	}
}

class Sequence {

	private static final Logger LOGGER = LoggerFactory.getLogger(Sequence.class);

	/** The entry distinguish name */
	private String dn;
	/** The attribute name */
	private String attributeName;
	/** The value */
	private int value;

	public boolean load(String dn, String attributeName, int serialNumber) {
		if (attributeName == null || dn == null || dn.indexOf('=') == -1) {
			return false;
		}
		setAttributeName(attributeName);
		setDn(dn);

		return readValue();
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	private void setDn(String value) {
		dn = value;
	}

	public int getCurrentValue() {
		return value;
	}

	private synchronized void setValue(int value) {
		this.value = value;
	}

	private boolean readValue() {
		try {
			SearchResult sr = JndiServices.getDstInstance().readEntry(getDn(), false);
			if (sr.getAttributes().get(getAttributeName()) != null && sr.getAttributes().get(getAttributeName()).size() > 0) {
				setValue(Integer.parseInt((String) sr.getAttributes().get(getAttributeName()).get()));
				return true;
			}
		}
		catch (NamingException e) {
			LOGGER.debug(e.toString(), e);
			// fall-thru to default failure exit
		}
		
		LOGGER.error("Failed to get the current value for the sequence {}/{}", getDn(), getAttributeName());
		return false;
	}
	
	/**
	 * Return the updated in directory new value
	 * @return Next value to set, or -1 if an error occurred
	 */
	public synchronized int getNextValue() {
		int newValue = 0;
		try {
			if (!readValue()) {
				return -1;
			}
			
			newValue = getCurrentValue() + 1;
			
			Attribute newValueAttribute = new BasicAttribute(getAttributeName());
			newValueAttribute.clear();
			newValueAttribute.add("" + newValue);

			// prepare modifications to be written to the directory
			JndiModifications jm = new JndiModifications(JndiModificationType.MODIFY_ENTRY);
			jm.setDistinguishName(getDn());
			List<ModificationItem> mi = new ArrayList<ModificationItem>();
			mi.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, newValueAttribute));
			jm.setModificationItems(mi);

			JndiServices.getDstInstance().apply(jm);
		} catch (NamingException e) {
			LOGGER.error("Failed to update the directory for the value of the sequence {}/{}", getDn(), getAttributeName());
			return -1;
		}

		setValue(newValue);
		return newValue;
	}

	public String getDn() {
		return dn;
	}
}
