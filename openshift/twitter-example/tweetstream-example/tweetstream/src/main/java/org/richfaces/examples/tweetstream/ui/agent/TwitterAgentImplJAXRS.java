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

package org.richfaces.examples.tweetstream.ui.agent;

import org.richfaces.examples.tweetstream.dataserver.source.TweetStream;
import org.richfaces.examples.tweetstream.domain.HashTag;
import org.richfaces.examples.tweetstream.domain.Tweet;
import org.richfaces.examples.tweetstream.domain.Tweeter;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;

import org.jboss.logging.Logger;


/**
 * Responsible for all client data.  The source injection is key and is the point where we can swap between data
 * sources.
 * <p/>
 * This class primarily loads initial content, and provides to the UI.  The source is responsible for kicking off any
 * listeners and/or push updates.
 *
 * @author <a href="mailto:jbalunas@redhat.com">Jay Balunas</a>
 */
@Named("twitterAgentJAXRS")
@SessionScoped
@Path("/twitter")
public class TwitterAgentImplJAXRS implements TwitterAgent, Serializable
{
   private Tweet selectedTweet;

   @Inject
   Logger log;
   
   @Inject
   private TweetStream source;

   @GET
   @Produces("application/json")
   @Path("hello")
   public String sayHello() {
	  System.out.println("HERE I AM");
	  log.info("LOG MESSAGE");
	  log.info("" + source);
	  return "{ \"hello\" : \"world from TwitterAgentImplJAXRS " + new java.util.Date() + "\"}";
   }
   
   public void updateTweets()
   {
      source.getTwitterSource().fetchContent();
   }

   public void refreshList(){
      source.getTwitterSource().refreshList();
   }

   @Override
   public String getSearchTerm()
   {
      return source.getTwitterSource().getSearchTerm();
   }

   @Override 
   @GET
   @Produces("application/json")
   @Path("tweets")
   public List<Tweet> getTweets()
   {
      return source.getTwitterSource().getTweets();
   }

   @Override 
   @GET
   @Produces("application/json")
   @Path("tweeters")   
   public List<Tweeter> getTweeters()
   {
      return source.getTwitterSource().getTopTweeters();
   }

   @Override 
   @GET
   @Produces("application/json")
   @Path("hashtags")   
   public List<HashTag> getHashtags()
   {
      return source.getTwitterSource().getTopHashtags();
   }

   @GET
   @Produces("application/json")
   @Path("selectedtweet")
   public Tweet getSelectedTweet()
   {
      return selectedTweet;
   }

   public void setSelectedTweet(Tweet selectedTweet)
   {
      this.selectedTweet = selectedTweet;
   }


}
