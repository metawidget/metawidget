//$Id: Hotel.groovy 4698 2007-04-18 06:40:06Z ebernard $
package org.jboss.seam.example.groovy

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

import org.hibernate.validator.Length
import org.hibernate.validator.NotNull
import org.jboss.seam.annotations.Name

import org.metawidget.inspector.annotation.*;
import org.metawidget.inspector.faces.*;

@Entity
@Name("hotel")
class Hotel implements Serializable
{
   @Id @GeneratedValue
   Long id

   @Length(max=50) @NotNull
   String name

   @Length(max=100) @NotNull
   @UiComesAfter( [ "name" ] )
   String address

   @Length(max=40) @NotNull
   @UiComesAfter( [ "address" ] )
   String city

   @Length(min=2, max=10) @NotNull
   @UiComesAfter( [ "city" ] )
   String state

   @Length(min=4, max=6) @NotNull
   @UiComesAfter( [ "state" ] )
   String zip

   @Length(min=2, max=40) @NotNull
   @UiComesAfter( [ "zip" ] )
   String country

   @Column(precision=6, scale=2)
   @UiComesAfter( [ "country" ])
   @UiFacesNumberConverter(type="currency",currencySymbol="\$")
   @UiLabel("Nightly rate")
   BigDecimal price

   @Override
   String toString()
   {
      return "Hotel(${name},${address},${city},${zip})"
   }
}
