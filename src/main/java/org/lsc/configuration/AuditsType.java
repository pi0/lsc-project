//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.17 at 11:13:13 PM CEST 
//


package org.lsc.configuration;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for auditsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="auditsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="csvAudit" type="{http://lsc-project.org/XSD/lsc-core-2.0.xsd}csvAuditType"/>
 *         &lt;element name="ldifAudit" type="{http://lsc-project.org/XSD/lsc-core-2.0.xsd}ldifAuditType"/>
 *         &lt;element name="pluginAudit" type="{http://lsc-project.org/XSD/lsc-core-2.0.xsd}pluginAuditType"/>
 *       &lt;/choice>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "auditsType", propOrder = {
    "csvAuditOrLdifAuditOrPluginAudit"
})
public class AuditsType {

    @XmlElements({
        @XmlElement(name = "ldifAudit", type = LdifAuditType.class),
        @XmlElement(name = "csvAudit", type = CsvAuditType.class),
        @XmlElement(name = "pluginAudit", type = PluginAuditType.class)
    })
    protected List<AuditType> csvAuditOrLdifAuditOrPluginAudit;
    @XmlAttribute
    protected String id;

    /**
     * Gets the value of the csvAuditOrLdifAuditOrPluginAudit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the csvAuditOrLdifAuditOrPluginAudit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCsvAuditOrLdifAuditOrPluginAudit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LdifAuditType }
     * {@link CsvAuditType }
     * {@link PluginAuditType }
     * 
     * 
     */
    public List<AuditType> getCsvAuditOrLdifAuditOrPluginAudit() {
        if (csvAuditOrLdifAuditOrPluginAudit == null) {
            csvAuditOrLdifAuditOrPluginAudit = new ArrayList<AuditType>();
        }
        return this.csvAuditOrLdifAuditOrPluginAudit;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}