package ctrl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;

@WebServlet("/Prime.do")
public class Prime extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		if (request.getParameter("doit") == null && request.getParameter("next") == null) {
		} else if (request.getParameter("doit") != null && request.getParameter("next") == null) {
			try {
				Engine engine = Engine.getInstance();
				String lowVal, highVal;
				lowVal = request.getParameter("lowVal");
				highVal = request.getParameter("highVal");
				int nextPrime = engine.doPrime(lowVal, highVal);
				request.setAttribute("lowVal", lowVal);
				request.setAttribute("highVal", highVal);
				request.setAttribute("result",  nextPrime);
			} catch (NumberFormatException e) {
				request.setAttribute("lowVal", request.getParameter("lowVal"));
				request.setAttribute("highVal", request.getParameter("highVal"));
				request.setAttribute("result",  "");
				request.setAttribute("error",  "Invalid entries!");
			} catch (ArithmeticException e) {
				request.setAttribute("lowVal", request.getParameter("lowVal"));
				request.setAttribute("highVal", request.getParameter("highVal"));
				request.setAttribute("result",  "");
				request.setAttribute("error",  e.getMessage());
			} catch (Exception e) {
				request.setAttribute("lowVal", request.getParameter("lowVal"));
				request.setAttribute("highVal", request.getParameter("highVal"));
				request.setAttribute("result",  "");
				request.setAttribute("error",  e.getMessage());		
			}
		} else if (request.getParameter("doit") == null && request.getParameter("next") != null) {
			try {
				Engine engine = Engine.getInstance();
				String lowVal, highVal;
				lowVal = request.getParameter("lowVal");
				highVal = request.getParameter("highVal");
				int newLowVal = engine.doPrime(lowVal, highVal);
				request.setAttribute("lowVal", newLowVal);
				int nextPrime = engine.doPrime(newLowVal+"", highVal);
				request.setAttribute("highVal", highVal);
				request.setAttribute("result",  nextPrime);
			} catch (NumberFormatException e) {
				request.setAttribute("result",  "");
				request.setAttribute("error",  "Invalid entries!");
			} catch (ArithmeticException e) {
				request.setAttribute("lowVal", request.getParameter("lowVal"));
				request.setAttribute("highVal", request.getParameter("highVal"));
				request.setAttribute("result",  "");
				request.setAttribute("error",  e.getMessage());
			} catch (Exception e) {
				request.setAttribute("lowVal", request.getAttribute("lowVal"));
				request.setAttribute("highVal", request.getParameter("highVal"));
				request.setAttribute("result",  "");
				request.setAttribute("error",  e.getMessage());			
			} 
		} 
		
		this.getServletContext().getRequestDispatcher("/Prime.jspx").forward(request, response);
	} 

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doGet(request, response);
	}
}
