//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.26 at 09:30:16 PM CET 
//


package sh.exec.keywordharvester.service.impl.veryrelated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResultType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Text" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HowRelated" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="Popularity" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultType", propOrder = {
    "text",
    "howRelated",
    "popularity"
})
public class ResultType {

    @XmlElement(name = "Text", required = true)
    protected String text;
    @XmlElement(name = "HowRelated")
    protected double howRelated;
    @XmlElement(name = "Popularity")
    protected double popularity;

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the howRelated property.
     * 
     */
    public double getHowRelated() {
        return howRelated;
    }

    /**
     * Sets the value of the howRelated property.
     * 
     */
    public void setHowRelated(double value) {
        this.howRelated = value;
    }

    /**
     * Gets the value of the popularity property.
     * 
     */
    public double getPopularity() {
        return popularity;
    }

    /**
     * Sets the value of the popularity property.
     * 
     */
    public void setPopularity(double value) {
        this.popularity = value;
    }

}