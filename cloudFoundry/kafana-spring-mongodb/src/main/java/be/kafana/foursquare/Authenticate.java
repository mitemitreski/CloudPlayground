//package be.kafana.foursquare;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.URL;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import be.kafana.foursquare.core.FourSquareAuthData;
//import be.kafana.foursquare.core.GetWebResources;
//
//import com.google.gson.Gson;
//
//@WebServlet(name = "auth", urlPatterns = {"/auth"})
//public class Authenticate extends HttpServlet {
//
//  /**
//   * 
//   */
//  private static final long serialVersionUID = 1L;
//
//  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//      throws ServletException, IOException {
//    response.setContentType("text/html;charset=UTF-8");
//    System.out.println(request.getQueryString());
//    PrintWriter out = response.getWriter();
//    String code = request.getParameter("code");
//    System.out.println(request.getRequestURL().toString());
//
//
//    // Instatiate GetWebRes Object
//    GetWebResources webrs = new GetWebResources();
//
//    // Auth data
//    FourSquareAuthData data = new FourSquareAuthData();
//    HttpSession session = request.getSession();
//
//    // If code is not null
//    if (!"".equals(code)) {
//
//      String url = "https://foursquare.com/oauth2/access_token?client_id=" + data.getClientId()
//          + "&client_secret=" + data.getClientSecret()
//          + "&grant_type=authorization_code&redirect_uri=" + data.getRedirectUri() + "&code="
//          + code;
//
//      String content = webrs.getContent(new URL(url));
//
//      // JSON Parsing using GSON Library
//      OauthToken oauthtoken = new Gson().fromJson(content, OauthToken.class);
//
//      // Store Oauth token in session
//      session.setAttribute("access_token", oauthtoken.getAccess_Token());
//
//      out.println("Authenticated Successfully!");
//
//      // Redirect to welcome page
//      response.sendRedirect(request.getContextPath() + "/getvenues");
//
//    } else {
//      out.println("Error: Could not authenticate");
//    }
//
//
//    try {
//      // out.println(myCode);
//    } finally {
//      out.close();
//    }
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
//
//// OauthToken object for JSON Mapping
//class OauthToken {
//
//  private String access_token;
//
//  public String getAccess_Token() {
//    return access_token;
//  }
//
//  public void setAccess_Token(String accesstoken) {
//    this.access_token = accesstoken;
//  }
//}
