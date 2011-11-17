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

//Get Web Resource - Class
public class GetWebResources {

	/**
	 * @param httpurl
	 * @return response after GET Request
	 */
	public String getContent(URL url) {

		StringBuffer contentBuf = new StringBuffer();

		try {
			URLConnection httpc;
			httpc = url.openConnection();
			httpc.setDoInput(true);
			httpc.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					httpc.getInputStream()));
			String line = "";
			while ((line = in.readLine()) != null) {
				contentBuf.append(line);
			}
			in.close();
		} catch (IOException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
					"Problem writing to " + url, e);
		}
		return contentBuf.toString();

	}

	// Return response after POST Request
	public String setContent(URL url, String inputdata) {
		StringBuffer contentBuf = new StringBuffer();
		try {
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.connect();

			// Write POST request data
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write(inputdata);
			out.flush();

			// Get Response
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String strLine = "";
			while ((strLine = in.readLine()) != null) {
				contentBuf.append(strLine);
			}

			in.close();
			out.close();
		} catch (IOException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
					"Problem writing to " + url + " content " + inputdata, e);
		}
		return contentBuf.toString();

	}

}
