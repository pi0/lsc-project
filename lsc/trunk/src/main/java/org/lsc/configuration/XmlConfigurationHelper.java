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
package org.lsc.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javanet.staxutils.IndentingXMLStreamWriter;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.FileUtils;
import org.lsc.configuration.objects.Audit;
import org.lsc.configuration.objects.Conditions;
import org.lsc.configuration.objects.Connection;
import org.lsc.configuration.objects.LscConfiguration;
import org.lsc.configuration.objects.Task;
import org.lsc.configuration.objects.audit.Csv;
import org.lsc.configuration.objects.audit.Ldif;
import org.lsc.configuration.objects.connection.Database;
import org.lsc.configuration.objects.connection.directory.Ldap;
import org.lsc.configuration.objects.security.Encryption;
import org.lsc.configuration.objects.security.Security;
import org.lsc.configuration.objects.services.DstLdap;
import org.lsc.configuration.objects.services.SrcLdap;
import org.lsc.configuration.objects.syncoptions.ForceSyncOptions;
import org.lsc.configuration.objects.syncoptions.PBSOAttribute;
import org.lsc.configuration.objects.syncoptions.PropertiesBasedSyncOptions;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.StaxWriter;

/**
 * Load/dump a configuration from/to XML file
 * 
 * @author rschermesser
 */
public class XmlConfigurationHelper {

	public static final String LSC_CONF_XML = "lsc.xml";
	public static final String LSC_NAMESPACE = "http://lsc-project.org/XSD/lsc-core-1.0.xsd"; 
	private XStream xstream;

	/**
	 * Initiate helper by adding XML aliases
	 */
	public XmlConfigurationHelper() {

		QNameMap qnm = new QNameMap();
		qnm.setDefaultNamespace(LSC_NAMESPACE);
		qnm.setDefaultPrefix("");

		xstream = new XStream(new Sun14ReflectionProvider(), new StaxDriver(qnm) {
			public HierarchicalStreamWriter createWriter(OutputStream out) {
				try {
					return new StaxWriter(this.getQnameMap(), new IndentingXMLStreamWriter(getOutputFactory().createXMLStreamWriter(out)), true, false);
				} catch (XMLStreamException e) {
					throw new StreamException(e);
				}
			}
		});

		xstream.setMode(XStream.ID_REFERENCES);
	
		xstream.processAnnotations(new Class[] { LscConfiguration.class, Connection.class, Ldap.class, Database.class, SrcLdap.class, 
				DstLdap.class, Csv.class, Ldif.class, Audit.class, Task.class, Conditions.class, PropertiesBasedSyncOptions.class, 
				ForceSyncOptions.class, Security.class, Encryption.class, PBSOAttribute.class });
//		xstream.autodetectAnnotations(true);

		// Set generic type
		xstream.aliasType("url", String.class);

	}

	/**
	 * Load an XML file to the object
	 * 
	 * @param filename
	 *            filename to read from
	 * @return the completed configuration object
	 * @throws FileNotFoundException
	 *             thrown if the file can not be accessed (either because of a
	 *             misconfiguration or due to a rights issue)
	 */
	public LscConfiguration getConfiguration(String filename)
			throws FileNotFoundException {
		return (LscConfiguration) xstream.fromXML(new FileInputStream(filename));
	}

	/**
	 * Dump the object to an XML file (by overriding if necessary)
	 * 
	 * @param filename
	 *            filename to write to
	 * @param lscConf
	 *            configuration object
	 * @throws FileNotFoundException
	 *             thrown if the file can not be accessed (either because of a
	 *             misconfiguration or due to a rights issue)
	 */
	public void saveConfiguration(String filename, LscConfiguration lscConf)
			throws IOException {
		File existing = new File(filename);
		if(existing.exists()) {
			File backup = new File(existing + ".bak");
			if(backup.exists()) {
				backup.delete();
			}
			FileUtils.copyFile(existing, backup);
		}
		xstream.toXML(lscConf, new FileOutputStream(filename, false));
	}
}