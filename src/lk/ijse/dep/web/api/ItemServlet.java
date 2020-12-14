package lk.ijse.dep.web.api;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author : Deshan Charuka <d.c.0729439631@gmail.com>
 * @since : 2020-12-10
 **/
@WebServlet(name = "ItemServlet",urlPatterns = "/items")
public class ItemServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        resp.setContentType("application/xml");
        try (PrintWriter out = resp.getWriter()) {
            try {
                BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
                Connection connection = cp.getConnection();
                Statement stm = connection.createStatement();
                ResultSet rst = stm.executeQuery("SELECT * FROM Item");
                out.println("<items>");

                while (rst.next()) {
                    String code = rst.getString(1);
                    String des = rst.getString(2);
                    int qty = rst.getInt(3);
//                    String qty = rst.getBigDecimal()

                }
                connection.close();
                out.println("</items>");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
