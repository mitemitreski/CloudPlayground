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
package org.richfaces.examples.tweetstream.dataserver.source;

import org.jboss.logging.Logger;
import org.richfaces.examples.tweetstream.dataserver.cache.CacheBuilder;
import org.richfaces.examples.tweetstream.dataserver.cache.InfinispanCacheBuilder;
import org.richfaces.examples.tweetstream.dataserver.cache.SimpleCacheBuilder;
import org.richfaces.examples.tweetstream.dataserver.jms.PublishController;
import org.richfaces.examples.tweetstream.dataserver.listeners.CacheUpdateListener;
import org.richfaces.examples.tweetstream.dataserver.listeners.TweetStreamListener;
import org.richfaces.examples.tweetstream.domain.HashTag;
import org.richfaces.examples.tweetstream.domain.Tweet;
import org.richfaces.examples.tweetstream.domain.Tweeter;
import org.richfaces.examples.tweetstream.domain.TwitterAggregate;
import org.richfaces.examples.tweetstream.util.TwitterAggregateUtil;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
// added for making this a JAX-RS endpoint
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * An implementation of the TwitterSource interface that will generate twitter content locally, not relying on outside
 * sources.
 *
 * @author <a href="mailto:jbalunas@redhat.com">Jay Balunas</a>
 */
@TwitterLocal
@ApplicationScoped
@Path("/tweets")
public class TwitterSourceLocal implements TwitterSource
{
   //If this changes must also update TweetStreamListener
//   private static final String[] TRACK = {"java", "jboss", "richfaces", "jbw", "judcon"};
   private static final String TRACK = "java jboss richfaces jbw judcon";

   @Inject
   Logger log;

   @Inject
   CacheBuilder cacheBuilder;

   @Inject
   TweetStreamListener tweetListener;

   Long lastSearch = -1l;


   private TwitterAggregate twitterAggregate;

   PublishController pubControl = new PublishController();
   
   @PostConstruct
   public void init()
   {
      System.out.println("&&&&&&&&&&& Init\n\n\n\n\n");
      //Fetch the initial content
      //rich:push not working on android 2.2 phones
      //  so we need to fetch initial content
      fetchContent();
      System.out.println("&&&&&&&&&&& after fetchContent\n\n\n\n\n");
      //TODO TEMP - just have them empty to start
      twitterAggregate = new TwitterAggregate();
      twitterAggregate.setTweets(new ArrayList<Tweet>());
      twitterAggregate.setTopHashTags(new ArrayList<HashTag>());
      twitterAggregate.setTopTweeters(new ArrayList<Tweeter>());

      // add the listener that checks hi new data has been added.
      //cacheBuilder.getCache().addListener(new CacheUpdateListener());

      //Populate cache with seed data from this class
      cacheBuilder.getCache().put("tweetaggregate", twitterAggregate);

      //Start the twitter streaming
      tweetListener.startTwitterStream();
      pubControl.publishView(twitterAggregate);
      log.info("Initialization of twitter source local complete");
   }

   @Override
   public String getSearchTerm()
   {
      return twitterAggregate.getFilter();
   }

   @GET
   @Produces("application/json")
   @Path("tweets")
   public List<Tweet> getTweets()
   {     
	  System.out.println("&&&&&&&&&&& getTweets\n\n\n\n\n");
	  if (twitterAggregate != null)
		  System.out.println("twitterAggregate.getTweets() = " + twitterAggregate.getTweets() + "\n\n" );
      return twitterAggregate.getTweets();
   }

   @GET
   @Produces("application/json")
   @Path("tweeters")
   public List<Tweeter> getTopTweeters()
   {
		  System.out.println("&&&&&&&&&&& getTopTweeters\n\n\n\n\n");

	   return twitterAggregate.getTopTweeters();
   }

   @GET
   @Produces("application/json")
   @Path("hashtags")
   public List<HashTag> getTopHashtags()
   {
		  System.out.println("&&&&&&&&&&& getTopHashTags\n\n\n\n\n");
      
	   return twitterAggregate.getTopHashTags();
   }

   @Override
   public TwitterAggregate getTwitterAggregate()
   {
		  System.out.println("&&&&&&&&&&& getTwitterAggregate\n\n\n\n\n");
	   
      return twitterAggregate;
   }

   @Override
   public void fetchContent()
   {
      //Check if updating content is needed
      //If not skip because this can be called on every page load
		  System.out.println("&&&&&&&&&&& fetchContent\n\n\n\n\n");	   
      if(false){
//      if (performSearch())
         {
            twitterAggregate = new TwitterAggregate();

            Twitter twitter = new TwitterFactory().getInstance();
            List<twitter4j.Tweet> t4jTweets = null;
            try
            {
               Query query = new Query(TRACK);
               QueryResult result = twitter.search(query);
               t4jTweets = result.getTweets();
               for (twitter4j.Tweet t4jTweet : t4jTweets)
               {
                  log.info("@" + t4jTweet.getFromUser() + " - " + t4jTweet.getText());

                  //Create a local tweet object from the t4j
                  Tweet tweet = new Tweet();
                  tweet.setText(t4jTweet.getText());
                  tweet.setId(t4jTweet.getFromUserId());
                  tweet.setProfileImageUrl(t4jTweet.getProfileImageUrl());
                  tweet.setScreenName(t4jTweet.getFromUser());
                  tweet.setHashTags(TwitterAggregateUtil.pullHashtags(t4jTweet.getText()));

                  twitterAggregate = TwitterAggregateUtil.updateTwitterAggregate(tweet, twitterAggregate);
               }
            }
            catch (TwitterException te)
            {
               te.printStackTrace();
               log.info("Failed to search tweets: " + te.getMessage());
            }
         }
      }

   }

   @Override
   public void refreshList()
   {
	   System.out.println("&&&&&&&&&&& refreshList\n\n\n\n\n");	 
      twitterAggregate = (TwitterAggregate)cacheBuilder.getCache().get("tweetaggregate");
//      twitterAggregate.setTweets(tweetAggregate.getTweets());
   }

   private boolean performSearch()
   {
	   System.out.println("&&&&&&&&&&& performSearch\n\n\n\n\n");	 
      if (lastSearch > 0)
      {
         long current = new Date().getTime();
         if (current - lastSearch > 5000)
         {
            log.debug("Enough time past - fetching new data--" + current + "-" + lastSearch + "=" + (current - lastSearch));
            lastSearch = current;
            return true;
         }
         else
         {
            log.debug("NOT enough time past - NOT fetching new data--" + current + "-" + lastSearch + "=" + (current - lastSearch));
            return false;
         }
      }
      else
      {
         lastSearch = new Date().getTime();
         log.debug("First time through - fetching new data");
         return true;
      }
   }

   @GET
   @Produces("application/json")
   @Path("hello")
   public String sayHello() {
	  System.out.println("HERE I AM");
	  //init();
	  log.info("LOG MESSAGE");
	  return "{ \"hello\" : \"world3 " + new java.util.Date() + "\"}";
   }
   
   @GET
   @Produces("application/json")
   @Path("aggregate")	
   public TwitterAggregate getAggregate() {		
		return this.getTwitterAggregate();
   }
}
