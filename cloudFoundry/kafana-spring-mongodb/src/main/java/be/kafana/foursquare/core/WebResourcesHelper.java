package be.kafana.foursquare.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.ContentEncodingHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

/**
 * Resource helper class for simple post to URL / return String
 * 
 * @author mite
 * 
 */

public class WebResourcesHelper {

  /**
   * @param uri
   * @return response after GET Request
   */

  private static final HttpClient httpclient = initHttpClient();

  private static final HttpClient initHttpClient() {
    HttpParams clientParams = new BasicHttpParams();
    clientParams.setParameter(CoreProtocolPNames.STRICT_TRANSFER_ENCODING, false);
    clientParams.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf");

    return new ContentEncodingHttpClient(clientParams);
  }

  public String getContent(URI uri) {
    HttpGet get = new HttpGet(uri);
    HttpResponse response = null;
    try {
      response = httpclient.execute(get);
    } catch (ClientProtocolException e1) {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE,
          " problems with client protocol in getting content for " + uri, e1);
    } catch (IOException e1) {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, " problem executing " + uri, e1);
    } finally {
      if (response == null) {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, " got no response");
        return "";
      }

    }

    HttpEntity entity = response.getEntity();
    InputStream in = null;
    try {
      in = entity.getContent();
    } catch (IOException e) {
      Logger.getLogger(getClass().getName())
          .log(Level.SEVERE, " getting entity from" + response, e);
    }
    String result = "";
    try {
      result = IOUtils.toString((in));
    } catch (IOException e) {
      Logger.getLogger(getClass().getName())
          .log(Level.SEVERE, " problem reading from " + entity, e);
    } finally {
      IOUtils.closeQuietly(in);
    }

    return result;

  }

  /**
   * Return response after a POST request with given input data
   * 
   * @param url
   * @param inputData
   * @return
   */
  public String setContent(URI uri, String inputData) {

    if (inputData == null) {
      throw new IllegalArgumentException("input data should be non-null value");
    }

    try {
      HttpPost post = new HttpPost(uri);
      HttpEntity entity = new StringEntity(inputData, HTTP.UTF_8);

      post.setEntity(entity);
      HttpResponse response = httpclient.execute(post);
      return IOUtils.toString(response.getEntity().getContent());

    } catch (IOException e) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
          "Problem writing to " + uri + " content " + inputData, e);
    }

    return "";

  }
}
