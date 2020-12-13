package lk.ijse.dep.web;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.dep.web.model.Customer;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Deshan Charuka <d.c.0729439631@gmail.com>
 * @since : 2020-12-08
 **/
@WebServlet(name = "CustomerServlet", urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setContentType("application/json");

        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
        try( Connection connection = cp.getConnection();) {
            Jsonb jsonb = JsonbBuilder.create();
            Customer customer = jsonb.fromJson(request.getReader(), Customer.class);

            /*Validation Logic*/
            if (customer.getId() == null | customer.getName() == null | customer.getAddress() == null){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            if (!customer.getId().matches("C\\d{3}")|| customer.getName().trim().isEmpty() || customer.getAddress().trim().isEmpty()){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Customer VALUES (?,?,?,6969.69)");
            pstm.setString(1, customer.getId());
            pstm.setString(2, customer.getName());
            pstm.setString(3, customer.getAddress());
            if (pstm.executeUpdate() > 0) {
              response.setStatus(HttpServletResponse.SC_CREATED);
            } else {
               response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (SQLIntegrityConstraintViolationException exp) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }catch (SQLException exp){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        ToDo: write the update method
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {

        String id = request.getParameter("id");

        resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.setContentType("application/json");
        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
        try (Connection connection = cp.getConnection()) {
            PrintWriter out = resp.getWriter();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Customer" + ((id != null) ? " WHERE id=?" : ""));
            if (id != null) {
                pstm.setObject(1, id);
            }

            ResultSet rst = pstm.executeQuery();

            List<Customer> cusList = new ArrayList();

            while (rst.next()) {
                id = rst.getString(1);
                String name = rst.getString(2);
                String address = rst.getString(3);
                cusList.add(new Customer(id, name, address));
            }

            if (id != null && cusList.isEmpty()) {
//                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else {
                Jsonb jsonb = JsonbBuilder.create();
                out.println(jsonb.toJson(cusList));
                connection.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            //resp.getWriter(e.toString());
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        ToDo:Complete delete method please
        String id = req.getParameter("id");

        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
    }
}
