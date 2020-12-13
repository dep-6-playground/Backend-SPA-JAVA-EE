package lk.ijse.dep.web.listener;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

/**
 * @author : Deshan Charuka <d.c.0729439631@gmail.com>
 * @since : 2020-12-09
 **/
@WebListener
public class MyServletContextListner implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Context is being initialized...!!");

        BasicDataSource bds = new BasicDataSource();
        bds.setUsername("root");
        bds.setPassword("1234");
        bds.setUrl("jdbc:mysql://localhost:3306/dep6BackUp");
        bds.setConnectionProperties("useSSL=false");
        bds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        bds.setInitialSize(5);
        bds.setMaxTotal(5);

        ServletContext ctx = sce.getServletContext();
        ctx.setAttribute("cp", bds);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Context is being destroyed...!!");

        BasicDataSource cp = (BasicDataSource) sce.getServletContext().getAttribute("cp");
        try {
            cp.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
