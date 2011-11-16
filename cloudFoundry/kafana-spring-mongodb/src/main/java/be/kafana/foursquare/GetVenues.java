//package be.kafana.foursquare;
//
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.URL;
//import java.net.URLEncoder;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.JSONValue;
//
//import be.kafana.foursquare.core.GetWebResources;
//
//
//@WebServlet(name = "getvenues", urlPatterns = {"/getvenues"})
//public class GetVenues extends HttpServlet {
//
//  /**
//   * 
//   */
//  private static final long serialVersionUID = 1L;
//  
//  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//      throws ServletException, IOException {
//    response.setContentType("text/html;charset=UTF-8");
//    PrintWriter out = response.getWriter();
//
//    // instantiate GetWebResource object
//    GetWebResources webrs = new GetWebResources();
//
//    // Get Session Variables
//    HttpSession session = request.getSession(true);
//    String access_token = (String) session.getAttribute("access_token");
//
//    // If AccessToken is not created
//    if (access_token == null) {
//      // Redirect to login page
//      response.sendRedirect(request.getContextPath() + "/login");
//    }
//
//    // Get lat,lng parameters from request object
//    String ll = request.getParameter("ll");
//    if (ll != null) {
//      ll = URLEncoder.encode(ll, "UTF-8");
//    } else {
//      // use default value
//      ll = URLEncoder.encode("29.976944,-90.114183", "UTF-8");
//    }
//
//    try {
//      // Build url and get contents
//      String url = "https://api.foursquare.com/v2/venues/explore?ll=" + ll
//          + "&section=food&oauth_token=" + access_token;
//      String content = webrs.getContent(new URL(url));
//      // out.println(content);
//
//      String htmltable = "<table width='100%' border=1><thead><th>Name</th><th>Address</th><th>State</th><th>Lat</th><th>Lng</th><th>Actions</th></thead><tbody>";
//      // JSON Parsing (JSON-Simple)
//      Object obj = JSONValue.parse(content);
//      JSONObject main_obj = (JSONObject) obj;
//      JSONObject response_obj = (JSONObject) main_obj.get("response");
//      JSONArray groups_arr = (JSONArray) response_obj.get("groups");
//      for (int i = 0; i < groups_arr.size(); i++) {
//        JSONObject group_obj = (JSONObject) groups_arr.get(i);
//        if (group_obj.get("type").equals("nearby")) {
//          JSONArray items_arr = (JSONArray) group_obj.get("items");
//          for (int j = 0; j < items_arr.size(); j++) {
//            JSONObject item_obj = (JSONObject) items_arr.get(j);
//
//            htmltable += "<tr>";
//            htmltable += "<td>" + item_obj.get("name") + "</td>";
//            JSONObject location_obj = (JSONObject) item_obj.get("location");
//            htmltable += "<td>" + location_obj.get("address") + "</td>";
//            htmltable += "<td>" + location_obj.get("city") + "</td>";
//            htmltable += "<td>" + location_obj.get("lat") + "</td>";
//            htmltable += "<td>" + location_obj.get("lng") + "</td>";
//            htmltable += "<td><a href='" + request.getContextPath()
//                + "/checkin?broadcast=public&venueid=" + item_obj.get("id")
//                + "'>Checkin (Public)</a> / ";
//            htmltable += "<a href='" + request.getContextPath()
//                + "/checkin?broadcast=private&venueid=" + item_obj.get("id")
//                + "'>Checkin (Private)</a> </td>";
//            htmltable += "</tr>";
//            // out.println(items_arr.get(0));
//          }
//        }
//      }
//      htmltable += "</tbody></table>";
//      out.println(htmltable);
//
//    } catch (Exception e) {
//      out.println("Error: Couldnt connect with Foursquare webservice");
//    }
//
//    out.close();
//  }
//
//  @Override
//  protected void doGet(HttpServletRequest request, HttpServletResponse response)
//      throws ServletException, IOException {
//    processRequest(request, response);
//  }
//
//  @Override
//  protected void doPost(HttpServletRequest request, HttpServletResponse response)
//      throws ServletException, IOException {
//    processRequest(request, response);
//  }
//
//
//}
