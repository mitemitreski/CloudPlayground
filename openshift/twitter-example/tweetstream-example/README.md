TweetStream on OpenShift
=========================

This is an application that pulls content from twitter and supports displaying it on
multiple devices.  The backend and web interface runs on OpenShift.

The steps for deploying this application are simple.  The only preqrequisite is to
have your own Twitter API access credentials.

Step 1: Configure Twitter4j
---------------------------
Everything is setup and configured for twitter4j except the API 
credentials.  We can not provide public credentials for accessing 
twitter API's via twitter4j.  This mean you will need to get and 
create your own credentials following the twitter4j and twitter 
instructions.

This is really not too hard, but does require some effort.  Because 
these steps change from time to time you should see 
http://twitter4j.org/en/configuration.html and 
https://dev.twitter.com/pages/auth for the details.

Basically you will need to login to https://dev.twitter.com and create an application.
Once you have an application registered with Twitter you will be able to create the
oauth keys on the application details page.

Step 2: Running on OpenShift
----------------------------

Create an account at http://openshift.redhat.com/

Create a jbossas-7.0 application

    rhc-create-app -a tweet -t jbossas-7.0

Add this upstream seambooking repo

    cd tweet
    git remote add upstream -m master git://github.com/openshift/tweetstream-example.git
    git pull -s recursive -X theirs upstream master
    
Edit tweetstream/src/main/resources/twitter4j.properties and add twitter API keys

    oauth.consumerKey=
    oauth.consumerSecret=
    oauth.accessToken=
    oauth.accessTokenSecret=
    
Commit this edit

    git commit -a -m "add my API creds for twitter"

Then push the repo upstream

    git push

That's it, you can now checkout your application at:

    http://tweet-$yourlogin.rhcloud.com
