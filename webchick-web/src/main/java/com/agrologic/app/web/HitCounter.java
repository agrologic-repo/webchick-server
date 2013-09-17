package com.agrologic.app.web;

import com.agrologic.app.model.UserRole;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class HitCounter extends HttpServlet {

    private UserRole role = UserRole.ADMIN;

    public HitCounter() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String r = request.getParameter("role");

        if (r != null) {
            role = UserRole.get(Integer.parseInt(r));
        }

        updateHitCounter();
        getHitCounterImage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String r = request.getParameter("role");

        if (r != null) {
            role = UserRole.get(Integer.parseInt(r));
        }

        updateHitCounter();
        getHitCounterImage(request, response);
    }

    private void updateHitCounter() {
        Connection connection = getConnection();

        try {

            //
            // Update the hits counter table by incrementing the
            // counter every time a user hits our page.
            //
            String sql = "UPDATE hits SET counter = counter + 1";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    private void getHitCounterImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = getConnection();
        String hits = "";

        try {

            //
            // Get the current hits counter from database.
            //
            String sql = "SELECT counter FROM hits";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                hits = rs.getString("counter");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }

//      //
//      // Create an image of our counter to be sent to the browser.
//      //
//      int width  = hits.length() * 10;
//      int height = 20;
//      BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//      Graphics2D g = buffer.createGraphics();
//
//      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//      g.setFont(new Font("Monospaced", Font.PLAIN, 10));
//      g.setColor(Color.WHITE);
//      g.fillRect(0, 0, width, height);
//      g.setColor(Color.BLACK);
//      g.drawString(hits, 0, height);
//      if(role ==  UserRole.ADMIN) {
//          response.setContentType("image/png");
//          OutputStream os = response.getOutputStream();
//          ImageIO.write(buffer, "png", os);
//          os.close();
//      }
        PrintWriter out = response.getWriter();
        out.println("<div class=\"hitcount\">");
        out.println("<p>");
        out.println(hits);
        out.println("</p>");
        out.println("</div>");

//      out.close();
    }

    private Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/agrodb", "root", "agrologic");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    private void closeConnection(Connection connection) {
        try {
            if ((connection != null) && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



