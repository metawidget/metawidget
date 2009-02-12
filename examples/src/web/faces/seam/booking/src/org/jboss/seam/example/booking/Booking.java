//$Id: Booking.java 5579 2007-06-27 00:06:49Z gavin $
package org.jboss.seam.example.booking;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;
import org.jboss.seam.annotations.Name;

import org.metawidget.inspector.annotation.*;
import org.metawidget.inspector.faces.*;

@Entity
@Name("booking")
public class Booking implements Serializable
{
   private Long id;
   private User user;
   private Hotel hotel;
   private Date checkinDate;
   private Date checkoutDate;
   private String creditCard;
   private String creditCardName;
   private int creditCardExpiryMonth;
   private int creditCardExpiryYear;
   private boolean smoking;
   private int beds;
   
   public Booking() {}
   
   public Booking(Hotel hotel, User user)
   {
      this.hotel = hotel;
      this.user = user;
   }
   
   @Transient
   @UiHidden   
   @UiFacesNumberConverter(type="currency",currencySymbol="$")
   @UiLabel("Total payment")
   public BigDecimal getTotal()
   {
      return hotel.getPrice().multiply( new BigDecimal( getNights() ) );
   }

   @Transient
   @UiHidden
   public int getNights()
   {
      return (int) ( checkoutDate.getTime() - checkinDate.getTime() ) / 1000 / 60 / 60 / 24;
   }

   @Id @GeneratedValue
   public Long getId()
   {
      return id;
   }
   public void setId(Long id)
   {
      this.id = id;
   }
   
   @NotNull
   @Basic @Temporal(TemporalType.DATE) 
   @UiComesAfter( "total" )
   @UiFacesDateTimeConverter( pattern = "MM/dd/yyyy" )
   public Date getCheckinDate()
   {
      return checkinDate;
   }
   public void setCheckinDate(Date datetime)
   {
      this.checkinDate = datetime;
   }

   @ManyToOne @NotNull
   @UiHidden
   public Hotel getHotel()
   {
      return hotel;
   }
   public void setHotel(Hotel hotel)
   {
      this.hotel = hotel;
   }
   
   @ManyToOne @NotNull
   @UiHidden
   public User getUser()
   {
      return user;
   }
   public void setUser(User user)
   {
      this.user = user;
   }
   
   @Basic @Temporal(TemporalType.DATE) 
   @NotNull
   @UiComesAfter( "checkinDate" )
   @UiFacesDateTimeConverter( pattern = "MM/dd/yyyy" )
   public Date getCheckoutDate()
   {
      return checkoutDate;
   }
   public void setCheckoutDate(Date checkoutDate)
   {
      this.checkoutDate = checkoutDate;
   }
   
   @NotNull(message="Credit card number is required")
   @Length(min=16, max=16, message="Credit card number must 16 digits long")
   @Pattern(regex="^\\d*$", message="Credit card number must be numeric")
   @UiComesAfter( "smoking" )
   @UiLabel( "Credit card #" )
   public String getCreditCard()
   {
      return creditCard;
   }

   public void setCreditCard(String creditCard)
   {
      this.creditCard = creditCard;
   }
   
   @Transient
   @UiHidden   
   public String getDescription()
   {
      DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
      return hotel==null ? null : hotel.getName() + 
            ", " + df.format( getCheckinDate() ) + 
            " to " + df.format( getCheckoutDate() );
   }

   @UiLookup( value = { "true", "false" }, labels = { "Smoking", "Non Smoking" } )
   @UiFacesComponent( "javax.faces.HtmlSelectOneRadio" )
   @UiComesAfter( "beds" )
   public boolean isSmoking()
   {
      return smoking;
   }

   public void setSmoking(boolean smoking)
   {
      this.smoking = smoking;
   }
   
   @UiLabel( "Room preference" )
   @UiComesAfter( "checkoutDate" )
   @UiLookup( value = { "1", "2", "3" }, labels = { "One king-size bed", "Two double beds", "Three beds" } )
   public int getBeds()
   {
      return beds;
   }

   public void setBeds(int beds)
   {
      this.beds = beds;
   }
   @NotNull(message="Credit card name is required")
   @Length(min=3, max=70)
   @UiComesAfter( "creditCard" )
   public String getCreditCardName()
   {
      return creditCardName;
   }

   public void setCreditCardName(String creditCardName)
   {
      this.creditCardName = creditCardName;
   }

   @UiLookup( value = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }, labels = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" } )
   @UiComesAfter( "creditCardName" )
   @UiLabel( "Expiry month" )
   public int getCreditCardExpiryMonth()
   {
      return creditCardExpiryMonth;
   }

   public void setCreditCardExpiryMonth(int creditCardExpiryMonth)
   {
      this.creditCardExpiryMonth = creditCardExpiryMonth;
   }

   @UiComesAfter( "creditCardExpiryMonth" )   
   @UiLookup( { "2005", "2006", "2007", "2008", "2009" } )
   @UiLabel( "Expiry year" )
   public int getCreditCardExpiryYear()
   {
      return creditCardExpiryYear;
   }

   public void setCreditCardExpiryYear(int creditCardExpiryYear)
   {
      this.creditCardExpiryYear = creditCardExpiryYear;
   }
   
   @Override
   public String toString()
   {
      return "Booking(" + user + ","+ hotel + ")";
   }

}
