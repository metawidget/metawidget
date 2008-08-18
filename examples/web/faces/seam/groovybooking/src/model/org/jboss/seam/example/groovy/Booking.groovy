//$Id: Booking.groovy 4698 2007-04-18 06:40:06Z ebernard $
package org.jboss.seam.example.groovy

import java.text.DateFormat
import javax.persistence.Basic
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Transient

import org.hibernate.validator.Length
import org.hibernate.validator.NotNull
import org.hibernate.validator.Pattern
import org.jboss.seam.annotations.Name

import org.metawidget.inspector.annotation.*;
import org.metawidget.inspector.faces.*;

@Entity
@Name("booking")
class Booking implements Serializable
{
   @Id @GeneratedValue
   Long id

   @ManyToOne @NotNull
   @UiHidden
   User user

   @ManyToOne @NotNull
   @UiHidden
   Hotel hotel

   @NotNull
   @Basic @Temporal(TemporalType.DATE)
   @UiComesAfter( [ "total" ] ) 
   @UiFacesDateTimeConverter( pattern = "MM/dd/yyyy" )
   Date checkinDate

   @Basic @Temporal(TemporalType.DATE)
   @NotNull
   @UiComesAfter( [ "checkinDate" ] )
   @UiFacesDateTimeConverter( pattern = "MM/dd/yyyy" )
   Date checkoutDate

   @NotNull(message="Credit card number is required")
   @Length(min=16, max=16, message="Credit card number must 16 digits long")
   @Pattern(regex=/^\d*$/, message="Credit card number must be numeric")
   @UiComesAfter( [ "smoking" ] )
   @UiLabel( "Credit card #" )
   String creditCard

   @NotNull(message="Credit card name is required")
   @Length(min=3, max=70)
   @UiComesAfter( [ "creditCard" ] )
   String creditCardName

   @UiLookup( value = [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" ], labels = [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" ] )
   @UiComesAfter( [ "creditCardName" ] )
   @UiLabel( "Expiry month" )
   int creditCardExpiryMonth

   @UiComesAfter( [ "creditCardExpiryMonth" ] )   
   @UiLookup( [ "2005", "2006", "2007", "2008", "2009" ] )
   @UiLabel( "Expiry year" )
   int creditCardExpiryYear

   @UiLookup( value = [ "true", "false" ], labels = [ "Smoking", "Non Smoking" ] )
   @UiFacesComponent( "javax.faces.HtmlSelectOneRadio" )
   @UiComesAfter( [ "beds" ] )
   boolean smoking

   @UiLabel( "Room preference" )
   @UiComesAfter( [ "checkoutDate" ] )
   @UiLookup( value = [ "1", "2", "3" ], labels = [ "One king-size bed", "Two double beds", "Three beds" ] )
   int beds

   Booking() {}

   Booking(Hotel hotel, User user)
   {
      this.hotel = hotel
      this.user = user
   }

   @Transient
   @UiHidden   
   @UiFacesNumberConverter(type="currency",currencySymbol="\$")
   @UiLabel("Total payment")   
   BigDecimal getTotal()
   {
      return hotel.price * getNights()
   }

   @Transient
   @UiHidden
   int getNights()
   {
      return (int) ( ( checkoutDate.time - checkinDate.time ) / 1000 / 60 / 60 / 24 )
   }

   @Transient
   @UiHidden
   String getDescription()
   {
      DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM)
      return hotel ?
            "${hotel.name}, ${df.format( checkinDate )} to ${df.format(checkoutDate)}" :
            null
   }

   @Override
   String toString()
   {
      return "Booking(" + user + ","+ hotel + ")"
   }

}
