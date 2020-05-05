package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aonaware.services.webservices.WordDefinition;

import client.IPLocator;
import client.WordDefinator;


/**
 * Servlet implementation class MyConvertor_Servlet
 */
@WebServlet("/Serv1")
public class Serv1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Serv1() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		WordDefinator client = new WordDefinator();
		IPLocator client2 = new IPLocator();
		
		String value = request.getParameter("value").toString();
		String type = request.getParameter("type").toString();
		PrintWriter out = response.getWriter();
		if(value.equals("")){
			out.write("error: Please, provide a value!");  
		}else{
			String result = "";
			if(type.equals("text")) {
				result = client.defineWord(value);
			}else{
				result = client2.getLocation(value);
			}
			out.write(result); 			
		}
					 
		out.flush();
	    out.close();


	}

}
