/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.examples.tweetstream.ui.useragent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/** @author jbalunas@redhat.com */
@Named("userAgent")
@RequestScoped
public class UserAgentProcessor
{
   private String userAgentStr;
   private String httpAccept;
   private UAgentInfo uAgentTest;

   public UserAgentProcessor()
   {
   }

   @PostConstruct
   public void init()
   {
      FacesContext context = FacesContext.getCurrentInstance();
      HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
      userAgentStr = request.getHeader("user-agent");
      httpAccept = request.getHeader("Accept");
      uAgentTest = new UAgentInfo(userAgentStr, httpAccept);
   }

   public boolean isPhone()
   {
      //Detects a whole tier of phones that support similar functionality as the iphone
      return uAgentTest.detectTierIphone();
   }

   public boolean isTablet()
   {
      //will detect ipads, xooms, blackberry tablets, but not galaxy - they use a strange user-agent
      return uAgentTest.detectTierTablet();
   }


}
