/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import ejb.NewsEntity;
import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Finalgalaxy
 */
@WebServlet(name = "PostMessage", urlPatterns = {"/PostMessage"})
public class PostMessage extends HttpServlet {
    @Resource(mappedName="jms/NewMessageFactory") private ConnectionFactory connectionFactory;
    @Resource(mappedName="jms/NewMessage") private Queue queue;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // Add the following code to send the JMS message
        String title=request.getParameter("title");
        String body=request.getParameter("body");

        if ((title!=null) && (body!=null)) {
            System.out.println("BODY="+body);
            try {
                Connection connection = connectionFactory.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer messageProducer = session.createProducer(queue);

                ObjectMessage message = session.createObjectMessage();
                // here we create NewsEntity, that will be sent in JMS message
                NewsEntity e = new NewsEntity();
                e.setTitle(title);
                e.setBody(body);
                
                System.out.println(messageProducer.toString());

                System.out.println("NewsEntity="+e.getBody()+","+e.getTitle()+","+e.getId()+","+e.toString());
                
                message.setObject(e);       
                System.out.println("message="+message.getObject().toString());  
                messageProducer.send(message);
                connection.close();
                response.sendRedirect("ListNews");

            } catch (JMSException ex) {
                System.out.println("Exception JMSException thrown while handling the message");
                ex.printStackTrace();
            } catch (Exception e){
                
                System.out.println("Exception ??? thrown while handling the message");
                e.printStackTrace();
            }
        }

        PrintWriter out = response.getWriter();
        out.println("Servlet PostMessage at " + request.getContextPath() + "</h1>");

        // The following code adds the form to the web page
        out.println("<form>");
        out.println("Title: <input type='text' name='title'><br/>");
        out.println("Message: <textarea name='body'></textarea><br/>");
        out.println("<input type='submit'><br/>");
        out.println("</form>");

        out.println("</body>");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
