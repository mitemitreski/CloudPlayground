package be.kafana.foursquare.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Resource hellper class for simple post to URL / return String
 * 
 * @author mite
 * 
 */
public class GetWebResources {

  /**
   * @param httpurl
   * @return response after GET Request
   */
  public String getContent(URL url) {

    StringBuffer contentBuf = new StringBuffer();
    URLConnection httpc;
    String line = "";
    try {
      httpc = url.openConnection();
      httpc.setDoInput(true);
      httpc.connect();
      BufferedReader in = new BufferedReader(new InputStreamReader(httpc.getInputStream()));
      while ((line = in.readLine()) != null) {
        contentBuf.append(line);
      }
      in.close();
    } catch (IOException e) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Problem writing to " + url, e);
    }
    return contentBuf.toString();

  }

  /**
   * Return response after a POST request with given input data
   * 
   * @param url
   * @param inputData
   * @return
   */
  public String setContent(URL url, String inputData) {
    StringBuffer contentBuf = new StringBuffer();
    HttpURLConnection connection = null;
    OutputStreamWriter out = null;
    try {
      connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");
      connection.connect();

      // Write POST request data
      out = new OutputStreamWriter(connection.getOutputStream());
      out.write(inputData);
      out.flush();
    } catch (IOException e) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
          "Problem writing to " + url + " content " + inputData, e);
    } finally {
      try {
        out.close();
      } catch (IOException e) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
            "Problem closing stream to " + url + " content " + inputData + " while writeing", e);
      }

    }
    // Get Response
    BufferedReader in = null;
    String strLine = "";
    try {
      if (connection == null) {
        return "";
      }
      in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      while ((strLine = in.readLine()) != null) {
        contentBuf.append(strLine);
      }

      in.close();
    } catch (IOException e) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
          "Problem writing to " + url + " content " + inputData, e);
    }
    return contentBuf.toString();

  }

}
