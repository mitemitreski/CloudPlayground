package be.kafana.foursquare.down.task;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import be.kafana.foursquare.down.VenueGrabber;
import be.kafana.foursquare.down.data.Venue;

import com.google.gson.Gson;

@Service
public class GrabTask {

  @Autowired(required = true)
  MongoDbFactory mongoDbFactory;
  @Autowired(required = true)
  VenueGrabber venueGrabber;

  @Autowired(required = true)
  MongoTemplate mongoTemplate;

  public void doDownload() {
    // FIXME
  }

  @Scheduled(cron = "0 59 14 * * *")
  public void doGrab() {

    // part of SK
    grabAndSave(41.996243, 21.336708, 42.008999, 21.413612);
  }

  private void grabAndSave(double startLatitiude,
      double startLongitude,
      double endLatitude,
      double endLongitude) {
    // FIXME
    Logger.getLogger(getClass().getName().toString()).log(Level.SEVERE,
        "Started grabbing Macedonia");

    if (!mongoTemplate.collectionExists(Venue.class)) {
      mongoTemplate.createCollection(Venue.class);
    }

    Set<Venue> venues = venueGrabber.grab(startLatitiude, startLongitude, endLatitude,
        endLongitude, 0.01, 100000);
    Gson gson = new Gson();
    for (Iterator<Venue> iterator = venues.iterator(); iterator.hasNext();) {
      Venue venue = iterator.next();
      mongoTemplate.save(venue);
      System.out.println(gson.toJson(venue));
    }

    Logger.getLogger(getClass().getName().toString()).log(Level.SEVERE,
        "Started grabbing Macedonia");

  }
}
