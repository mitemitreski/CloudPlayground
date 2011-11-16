package org.cloudfoundry.services;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import be.kafana.foursquare.down.VenueGrabber;
import be.kafana.foursquare.down.data.Venue;

import com.google.gson.Gson;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	@Autowired(required = false)
	MongoDbFactory mongoDbFactory;
	@Autowired(required = true)
	VenueGrabber venueGrabber;

	@Autowired(required = false)
	MongoTemplate mongoTemplate;

	@Autowired(required = false)
	@Qualifier(value = "serviceProperties")
	Properties serviceProperties;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String home(Model model) {
		List<String> services = new ArrayList<String>();
		if (mongoDbFactory != null) {
			services.add("MongoDB: "
					+ mongoDbFactory.getDb().getMongo().getAddress());
		}
		Random generator = new Random();
		Person p = new Person("Joe Cloud-" + generator.nextInt(100),
				generator.nextInt(100));
		mongoTemplate.save(p);
		List<Person> people = mongoTemplate.find(
				new Query(where("age").lt(100)), Person.class);

		model.addAttribute("people", people);
		model.addAttribute("services", services);
		model.addAttribute("serviceProperties", getServicePropertiesAsList());

		String environmentName = (System.getenv("VCAP_APPLICATION") != null) ? "Cloud"
				: "Local";
		model.addAttribute("environmentName", environmentName);
		return "home";
	}

	@RequestMapping("/env")
	public void env(HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.println("System Properties:");
		for (Map.Entry<Object, Object> property : System.getProperties()
				.entrySet()) {
			out.println(property.getKey() + ": " + property.getValue());
		}
		out.println();
		out.println("System Environment:");
		for (Map.Entry<String, String> envvar : System.getenv().entrySet()) {
			out.println(envvar.getKey() + ": " + envvar.getValue());
		}
	}

	@RequestMapping("/service-properties")
	public void services(HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		if (serviceProperties != null) {
			out.println("Cloud Service Properties:");
			// Map envMap = System.getenv();
			for (Object key : serviceProperties.keySet()) {
				out.println(key + ": " + serviceProperties.get(key));
			}
		} else {
			out.println("No Cloud Service Properties found.  Check configuration file for <cloud:service-properties/> element");
		}
		out.println(")<a href=\"/\">Return to previous page.</a>");
		out.println();
	}

	private List<String> getServicePropertiesAsList() {
		List<String> propList = new ArrayList<String>();
		if (serviceProperties != null) {
			for (Object key : serviceProperties.keySet()) {
				propList.add(key + ": " + serviceProperties.get(key));
			}
		}
		return propList;
	}

	/*
	 * 
	 * 42.252918,19.988708 41.191056,23.27362
	 */
	@RequestMapping("/grab")
	public void grabData(HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();

		if (!mongoTemplate.collectionExists(Venue.class)) {
			mongoTemplate.createCollection(Venue.class);
		}
		// 42.378836,23.119812
		Set<Venue> venues = venueGrabber.grab(40.884448, 20.543518, 42.317939,
				23.108826, 30000, 100000);
		Gson gson = new Gson();
		for (Iterator<Venue> iterator = venues.iterator(); iterator.hasNext();) {
			Venue venue = iterator.next();
			mongoTemplate.save(venue);
			out.println(gson.toJson(venue));
		}

		out.println("inserted " + venues.size() + " entries");

	}

	@RequestMapping("/deleteAll")
	public void deleteAll(HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		long count = mongoTemplate.execute(Person.class,
				new CollectionCallback<Long>() {
					@Override
					public Long doInCollection(DBCollection collection)
							throws MongoException, DataAccessException {
						return collection.count();
					}
				});
		out.println("Deleted " + count + " entries");
		mongoTemplate.dropCollection("Person");
	}

}
