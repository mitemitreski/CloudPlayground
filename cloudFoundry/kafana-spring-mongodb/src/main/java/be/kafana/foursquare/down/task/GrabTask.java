package be.kafana.foursquare.down.task;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import be.kafana.foursquare.down.VenueGrabber;
import be.kafana.foursquare.down.data.Photo;
import be.kafana.foursquare.down.data.Venue;

import com.google.gson.Gson;

@Service
public class GrabTask {

  @Autowired(required = true)
  private MongoDbFactory mongoDbFactory;
  @Autowired(required = true)
  private VenueGrabber venueGrabber;
  @Autowired(required = true)
  private MongoTemplate mongoTemplate;


  private static final Log LOGGER = LogFactory.getLog(GrabTask.class);

  public void doDownload() {
    // FIXME
  }

  @Scheduled(cron = "0 59 14 * * *")
  public void doGrab() {
    // part of SK
    grabAndSaveVenues(41.996243, 21.336708, 42.008999, 21.413612);
  }

  private void grabAndSaveVenues(double startLatitiude,
      double startLongitude,
      double endLatitude,
      double endLongitude) {
    Gson gson = new Gson();
    LOGGER.info("Started grabbing ");
    createCollection(Venue.class);
    Set<Venue> venues = venueGrabber.grab(startLatitiude, startLongitude, endLatitude,
        endLongitude, 0.01, 100000);
    for (Iterator<Venue> iterator = venues.iterator(); iterator.hasNext();) {
      Venue venue = iterator.next();
      mongoTemplate.save(venue);
      System.out.println(gson.toJson(venue));
    }

    LOGGER.info("Finished grabbing");

  }

  public void createCollection(Class<?> clazz) {
    if (!mongoTemplate.collectionExists(clazz)) {
      mongoTemplate.createCollection(clazz);

    }

  }

  public void grabAndSavePhotos() {
    LOGGER.info("Started grabbing ");
    createCollection(Photo.class);
  }


}
