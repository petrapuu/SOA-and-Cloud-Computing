
package com.cdyne.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ResolveIPResult" type="{http://ws.cdyne.com/}IPInformation" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "resolveIPResult"
})
@XmlRootElement(name = "ResolveIPResponse")
public class ResolveIPResponse {

    @XmlElement(name = "ResolveIPResult")
    protected IPInformation resolveIPResult;

    /**
     * Gets the value of the resolveIPResult property.
     * 
     * @return
     *     possible object is
     *     {@link IPInformation }
     *     
     */
    public IPInformation getResolveIPResult() {
        return resolveIPResult;
    }

    /**
     * Sets the value of the resolveIPResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPInformation }
     *     
     */
    public void setResolveIPResult(IPInformation value) {
        this.resolveIPResult = value;
    }

}
