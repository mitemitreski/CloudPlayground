//package be.kafana.foursquare;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import be.kafana.foursquare.core.FourSquareAuthData;
//
//
//@WebServlet(name = "login", urlPatterns = {"/login"})
//public class Login extends HttpServlet {
//
//  private static final long serialVersionUID = 1L;
//
//  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//      throws ServletException, IOException {
//    response.setContentType("text/html;charset=UTF-8");
//    PrintWriter out = response.getWriter();
//
//    // API Credentials
//    FourSquareAuthData data=new FourSquareAuthData();
//    
//   
//
//    try {
//      // Redirect User to foursquare login page
//      String url = "https://foursquare.com/oauth2/authenticate?client_id=" + data.getClientId()
//          + "&response_type=code&redirect_uri=" + data.getRedirectUri();
//      response.sendRedirect(url);
//
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
