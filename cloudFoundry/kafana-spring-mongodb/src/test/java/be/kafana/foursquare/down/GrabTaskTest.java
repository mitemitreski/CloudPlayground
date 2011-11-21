package be.kafana.foursquare.down;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.kafana.foursquare.down.task.GrabTask;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:root-test-context.xml"})
public class GrabTaskTest {

  @Autowired
  GrabTask task = new GrabTask();

  @Test
  public void aTest() {

    task.doGrab();
  }

}
