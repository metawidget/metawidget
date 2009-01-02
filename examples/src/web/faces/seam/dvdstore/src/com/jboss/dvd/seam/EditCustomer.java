/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package com.jboss.dvd.seam;

import java.util.Map;

import javax.ejb.Local;

import org.hibernate.validator.*;
import org.metawidget.inspector.annotation.*;

@Local
public interface EditCustomer
{
    public void startEdit();

    public Map<String,Integer> getCreditCardTypes();

    public void   setPasswordVerify(String password);
    
    @UiMasked
    @NotNull
    public String getPasswordVerify();

    public boolean isValidNamePassword();

    public String saveUser();

    public void destroy();
}
