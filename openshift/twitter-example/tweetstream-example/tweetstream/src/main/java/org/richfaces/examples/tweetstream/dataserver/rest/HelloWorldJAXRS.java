package org.richfaces.examples.tweetstream.dataserver.rest;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

@Path("/helloworldjaxrs")
public class HelloWorldJAXRS {
	@GET
	@Produces("application/json")
	public String sayHello() {
		return "{ \"hello\" : \"world " + new java.util.Date() + "\"}";
	}
}
