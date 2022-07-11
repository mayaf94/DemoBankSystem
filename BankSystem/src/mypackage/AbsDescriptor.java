
package mypackage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}abs-categories"/>
 *         &lt;element ref="{}abs-loans"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "absCategories",
    "absLoans"
})
@XmlRootElement(name = "abs-descriptor")
public class AbsDescriptor {

    @XmlElement(name = "abs-categories", required = true)
    protected AbsCategories absCategories;
    @XmlElement(name = "abs-loans", required = true)
    protected AbsLoans absLoans;

    /**
     * Gets the value of the absCategories property.
     * 
     * @return
     *     possible object is
     *     {@link AbsCategories }
     *     
     */
    public AbsCategories getAbsCategories() {
        return absCategories;
    }

    /**
     * Sets the value of the absCategories property.
     * 
     * @param value
     *     allowed object is
     *     {@link AbsCategories }
     *     
     */
    public void setAbsCategories(AbsCategories value) {
        this.absCategories = value;
    }

    /**
     * Gets the value of the absLoans property.
     * 
     * @return
     *     possible object is
     *     {@link AbsLoans }
     *     
     */
    public AbsLoans getAbsLoans() {
        return absLoans;
    }

    /**
     * Sets the value of the absLoans property.
     * 
     * @param value
     *     allowed object is
     *     {@link AbsLoans }
     *     
     */
    public void setAbsLoans(AbsLoans value) {
        this.absLoans = value;
    }

}
