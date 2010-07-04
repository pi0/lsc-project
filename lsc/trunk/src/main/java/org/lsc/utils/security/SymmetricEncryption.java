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
package org.lsc.utils.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.lsc.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>This new class allows symmetric encryption. You should have BouncyCastle
 * installed. Three new configuration parameters could be added to the
 * configuration:</p>
 * <ul>
 *   <li>lsc.security.encryption.keyfile: the path to the file used to
 *   encrypt/decrypt data</li>
 *   <li>lsc.security.encryption.algorithm: the algorithm to use</li>
 *   <li>lsc.security.encryption.strength: the strength in bits</li>
 * </ul>
 */
public class SymmetricEncryption {

	private static final Logger LOGGER = LoggerFactory.getLogger(SymmetricEncryption.class);

	public static final int DEFAULT_CIPHER_STRENGTH = 128;
	public static final String DEFAULT_CIPHER_ALGORITHM = "AES";
	private int strength;
	private String algorithm;
	private String keyPath;
	private Cipher cipherDecrypt;
	private Cipher cipherEncrypt;
	private Provider securityProvider;

	/**
	 * New SymmetricEncryption object with default values.
	 * @throws java.security.GeneralSecurityException
	 */
	public SymmetricEncryption() throws GeneralSecurityException {
		this(SymmetricEncryption.getDefaultKeyPath(),
						SymmetricEncryption.getDefaultAlgorithm(),
						SymmetricEncryption.getDefaultStrength());
	}

	/**
	 * New SymmetricEncryption object.
	 * @param keyPath The filename of the key to use
	 * @param algo A supported algorithm to use (see constant values defined
	 * in this class which specified supported algorithms)
	 * @param strength The encryption strength
	 * @throws java.security.GeneralSecurityException
	 */
	public SymmetricEncryption(String keyPath, String algo, int strength) throws GeneralSecurityException {
		this.securityProvider = new BouncyCastleProvider();
		this.algorithm = algo;
		this.strength = strength;
		this.keyPath = keyPath;

		Security.addProvider(this.securityProvider);
	}

	/**
	 * Encrypt bytes.
	 * @param toEncrypt
	 * @return Encrypted bytes.
	 * @throws java.security.GeneralSecurityException
	 */
	public byte[] encrypt(byte[] toEncrypt) throws GeneralSecurityException {
		return cipherEncrypt.doFinal(toEncrypt);
	}

	/**
	 * Decrypt bytes.
	 * @param toDecrypt
	 * @return Decrypted bytes.
	 * @throws java.security.GeneralSecurityException
	 */
	public byte[] decrypt(byte[] toDecrypt) throws GeneralSecurityException {
		return cipherDecrypt.doFinal(toDecrypt);
	}

	/**
	 * Generate a random key file with default value
	 * @return boolean false if an error occurred
	 * @throws NoSuchAlgorithmException 
	 * @throws NoSuchProviderException 
	 */
	public boolean generateDefaultRandomKeyFile() throws NoSuchAlgorithmException, NoSuchProviderException {
		return this.generateRandomKeyFile(this.keyPath, this.algorithm, this.strength);
	}

	/**
	 * Generate a random key file.
	 * @param keyPath The filename where to write the key
	 * @param algo The supported algorithm to use
	 * @param strength The encryption strength
	 * @return boolean false if an error occurred
	 * @throws NoSuchAlgorithmException 
	 * @throws NoSuchProviderException 
	 */
	public boolean generateRandomKeyFile(String keyPath, String algo, int strength) throws NoSuchAlgorithmException, NoSuchProviderException {
		OutputStream os = null;
		try {
			KeyGenerator kg = KeyGenerator.getInstance(algo, this.securityProvider.getName());
			SecretKey cipherKey = kg.generateKey();
			SecureRandom sr = new SecureRandom();
			kg.init(strength, sr);
			os = new FileOutputStream(keyPath);
			os.write(cipherKey.getEncoded());
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if(os != null) {
					os.close();
				}
			}
			catch (IOException e1) {
			}
		}
		return true;
	}

	/**
	 * Return the default filename of the key to use.
	 * The filename could be specified in the configuration file through the
	 * lsc.security.encryption.keyfile property.
	 * @return A filename
	 */
	public static String getDefaultKeyPath() {
		return Configuration.getString("lsc.security.encryption.keyfile",
						Configuration.getConfigurationDirectory() + "lsc.key");
	}

	/**
	 * Return the default supported algorithm to use.
	 * The algorithm could be specified in the configuration file through the
	 * lsc.security.encryption.algorithm property. See constant values defined
	 * in this class which specified supported algorithms.
	 * @return A supported algorithm
	 */
	public static String getDefaultAlgorithm() {
		return Configuration.getString("lsc.security.encryption.algorithm",
						SymmetricEncryption.DEFAULT_CIPHER_ALGORITHM);
	}

	/**
	 * Return the default encryption strength.
	 * The encryption strength could be specified in the configuration file
	 * through the lsc.security.encryption.strength property.
	 * @return int
	 */
	public static int getDefaultStrength() {
		return Configuration.getInt("lsc.security.encryption.strength",
						SymmetricEncryption.DEFAULT_CIPHER_STRENGTH);
	}

	/**
	 * Initialize encryption object from the configuration file.
	 * @return boolean (always true if no exception)
	 * @throws GeneralSecurityException 
	 */
	public boolean initialize() throws GeneralSecurityException {
		InputStream input = null;
		boolean fail = false;
		try {
			input = new FileInputStream(new File(this.keyPath));
			byte[] data = new byte[strength / Byte.SIZE];
			input.read(data);

			Key key = new SecretKeySpec(data, this.algorithm);
			this.cipherEncrypt = Cipher.getInstance(this.algorithm);
			this.cipherEncrypt.init(Cipher.ENCRYPT_MODE, key);
			this.cipherDecrypt = Cipher.getInstance(this.algorithm);
			this.cipherDecrypt.init(Cipher.DECRYPT_MODE, key);

		} catch (IOException e) {
			fail = true;
		} finally {
			try {
				if(input != null) {
					input.close();
				}
			} catch (IOException e) {
			}
		}
		
		if (fail) {
			LOGGER.error("Error reading the key for SymmetricEncryption! ({})", this.keyPath);
			return false;
		}
		
		return true;
	}

	/**
	 * This main allow user to generate random key file.
	 * @param argv
	 */
	public static void main(String argv[]) {
		try {
			Options options = new Options();
			options.addOption("f", "cfg", true, "Specify configuration directory");
			CommandLine cmdLine = new GnuParser().parse(options, argv);

			if (cmdLine.getOptions().length > 0 && cmdLine.hasOption("f")) {
				// if a configuration directory was set on command line, use it to set up Configuration
				Configuration.setUp(cmdLine.getOptionValue("f"));
			} else {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("lsc", options);
				System.exit(1);
			}
		} catch (ParseException e) {
			StringBuilder sbf = new StringBuilder();
			for(String arg : argv) {
				sbf.append(arg).append(" ");
			}
			
			LOGGER.error("Unable to parse options : {}({})", sbf.toString(), e);
			System.exit(1);
		}

		try {
			SymmetricEncryption se = new SymmetricEncryption();
			if (se.generateDefaultRandomKeyFile()) {
				LOGGER.info("Key generated: {}", SymmetricEncryption.getDefaultKeyPath());
			}
		} catch (GeneralSecurityException ex) {
			LOGGER.debug(ex.toString(), ex);
		}
	}
}