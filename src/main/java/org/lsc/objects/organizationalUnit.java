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
package org.lsc.objects;

import java.util.List;

/**
 * LDAP organizationalUnit objectClass representation.
 */
public class organizationalUnit extends top {

    /**
     * Default constructor.
     */
    public organizationalUnit() {
        super();
		objectClass.add("organizationalUnit");
	}

    /** Multivalued attribute : ou. */
    private List ou;

    /** Multivalued attribute : userPassword. */
    private List userPassword;

    /** Multivalued attribute : searchGuide. */
    private List searchGuide;

    /** Multivalued attribute : seeAlso. */
    private List seeAlso;

    /** Multivalued attribute : businessCategory. */
    private List businessCategory;

    /** Multivalued attribute : x121Address. */
    private List x121Address;

    /** Multivalued attribute : registeredAddress. */
    private List registeredAddress;

    /** Multivalued attribute : destinationIndicator. */
    private List destinationIndicator;

    /** Multivalued attribute : telexNumber. */
    private List telexNumber;

    /** Multivalued attribute : teletexTerminalIdentifier. */
    private List teletexTerminalIdentifier;

    /** Multivalued attribute : telephoneNumber. */
    private List telephoneNumber;

    /** Multivalued attribute : internationaliSDNNumber. */
    private List internationaliSDNNumber;

    /** Multivalued attribute : facsimileTelephoneNumber. */
    private List facsimileTelephoneNumber;

    /** Multivalued attribute : street. */
    private List street;

    /** Multivalued attribute : postOfficeBox. */
    private List postOfficeBox;

    /** Multivalued attribute : postalCode. */
    private List postalCode;

    /** Multivalued attribute : postalAddress. */
    private List postalAddress;

    /** Multivalued attribute : physicalDeliveryOfficeName. */
    private List physicalDeliveryOfficeName;

    /** Multivalued attribute : st. */
    private List st;

    /** Multivalued attribute : l. */
    private List l;

    /** Multivalued attribute : description. */
    private List description;

    /** Monovalued attribute : preferredDeliveryMethod. */
    private String preferredDeliveryMethod;

    /**
     * ou getter.
     * @return ou values
     */
    public final List getOu() {
        return ou;
    }

    /**
     * ou setter.
     * @param values ou values
     */
    public final void setOu(final List values) {
        ou = values;
    }

    /**
     * userPassword getter.
     * @return userPassword values
     */
    public final List getUserPassword() {
        return userPassword;
    }

    /**
     * userPassword setter.
     * @param values userPassword values
     */
    public final void setUserPassword(final List values) {
        userPassword = values;
    }

    /**
     * searchGuide getter.
     * @return searchGuide values
     */
    public final List getSearchGuide() {
        return searchGuide;
    }

    /**
     * searchGuide setter.
     * @param values searchGuide values
     */
    public final void setSearchGuide(final List values) {
        searchGuide = values;
    }

    /**
     * seeAlso getter.
     * @return seeAlso values
     */
    public final List getSeeAlso() {
        return seeAlso;
    }

    /**
     * seeAlso setter.
     * @param values seeAlso values
     */
    public final void setSeeAlso(final List values) {
        seeAlso = values;
    }

    /**
     * businessCategory getter.
     * @return businessCategory values
     */
    public final List getBusinessCategory() {
        return businessCategory;
    }

    /**
     * businessCategory setter.
     * @param values businessCategory values
     */
    public final void setBusinessCategory(final List values) {
        businessCategory = values;
    }

    /**
     * x121Address getter.
     * @return x121Address values
     */
    public final List getX121Address() {
        return x121Address;
    }

    /**
     * x121Address setter.
     * @param values x121Address values
     */
    public final void setX121Address(final List values) {
        x121Address = values;
    }

    /**
     * registeredAddress getter.
     * @return registeredAddress values
     */
    public final List getRegisteredAddress() {
        return registeredAddress;
    }

    /**
     * registeredAddress setter.
     * @param values registeredAddress values
     */
    public final void setRegisteredAddress(final List values) {
        registeredAddress = values;
    }

    /**
     * destinationIndicator getter.
     * @return destinationIndicator values
     */
    public final List getDestinationIndicator() {
        return destinationIndicator;
    }

    /**
     * destinationIndicator setter.
     * @param values destinationIndicator values
     */
    public final void setDestinationIndicator(final List values) {
        destinationIndicator = values;
    }

    /**
     * telexNumber getter.
     * @return telexNumber values
     */
    public final List getTelexNumber() {
        return telexNumber;
    }

    /**
     * telexNumber setter.
     * @param values telexNumber values
     */
    public final void setTelexNumber(final List values) {
        telexNumber = values;
    }

    /**
     * teletexTerminalIdentifier getter.
     * @return teletexTerminalIdentifier values
     */
    public final List getTeletexTerminalIdentifier() {
        return teletexTerminalIdentifier;
    }

    /**
     * teletexTerminalIdentifier setter.
     * @param values teletexTerminalIdentifier values
     */
    public final void setTeletexTerminalIdentifier(final List values) {
        teletexTerminalIdentifier = values;
    }

    /**
     * telephoneNumber getter.
     * @return telephoneNumber values
     */
    public final List getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * telephoneNumber setter.
     * @param values telephoneNumber values
     */
    public final void setTelephoneNumber(final List values) {
        telephoneNumber = values;
    }

    /**
     * internationaliSDNNumber getter.
     * @return internationaliSDNNumber values
     */
    public final List getInternationaliSDNNumber() {
        return internationaliSDNNumber;
    }

    /**
     * internationaliSDNNumber setter.
     * @param values internationaliSDNNumber values
     */
    public final void setInternationaliSDNNumber(final List values) {
        internationaliSDNNumber = values;
    }

    /**
     * facsimileTelephoneNumber getter.
     * @return facsimileTelephoneNumber values
     */
    public final List getFacsimileTelephoneNumber() {
        return facsimileTelephoneNumber;
    }

    /**
     * facsimileTelephoneNumber setter.
     * @param values facsimileTelephoneNumber values
     */
    public final void setFacsimileTelephoneNumber(final List values) {
        facsimileTelephoneNumber = values;
    }

    /**
     * street getter.
     * @return street values
     */
    public final List getStreet() {
        return street;
    }

    /**
     * street setter.
     * @param values street values
     */
    public final void setStreet(final List values) {
        street = values;
    }

    /**
     * postOfficeBox getter.
     * @return postOfficeBox values
     */
    public final List getPostOfficeBox() {
        return postOfficeBox;
    }

    /**
     * postOfficeBox setter.
     * @param values postOfficeBox values
     */
    public final void setPostOfficeBox(final List values) {
        postOfficeBox = values;
    }

    /**
     * postalCode getter.
     * @return postalCode values
     */
    public final List getPostalCode() {
        return postalCode;
    }

    /**
     * postalCode setter.
     * @param values postalCode values
     */
    public final void setPostalCode(final List values) {
        postalCode = values;
    }

    /**
     * postalAddress getter.
     * @return postalAddress values
     */
    public final List getPostalAddress() {
        return postalAddress;
    }

    /**
     * postalAddress setter.
     * @param values postalAddress values
     */
    public final void setPostalAddress(final List values) {
        postalAddress = values;
    }

    /**
     * physicalDeliveryOfficeName getter.
     * @return physicalDeliveryOfficeName values
     */
    public final List getPhysicalDeliveryOfficeName() {
        return physicalDeliveryOfficeName;
    }

    /**
     * physicalDeliveryOfficeName setter.
     * @param values physicalDeliveryOfficeName values
     */
    public final void setPhysicalDeliveryOfficeName(final List values) {
        physicalDeliveryOfficeName = values;
    }

    /**
     * st getter.
     * @return st values
     */
    public final List getSt() {
        return st;
    }

    /**
     * st setter.
     * @param values st values
     */
    public final void setSt(final List values) {
        st = values;
    }

    /**
     * l getter.
     * @return l values
     */
    public final List getL() {
        return l;
    }

    /**
     * l setter.
     * @param values l values
     */
    public final void setL(final List values) {
        l = values;
    }

    /**
     * description getter.
     * @return description values
     */
    public final List getDescription() {
        return description;
    }

    /**
     * description setter.
     * @param values description values
     */
    public final void setDescription(final List values) {
        description = values;
    }

    /**
     * Default preferredDeliveryMethod getter.
     * @return preferredDeliveryMethod value
     */
    public final String getPreferredDeliveryMethod() {
        return preferredDeliveryMethod;
    }

    /**
     * preferredDeliveryMethod setter.
     * @param value preferredDeliveryMethod value
     */
    public final void setPreferredDeliveryMethod(final String value) {
        this.preferredDeliveryMethod = value;
    }

}