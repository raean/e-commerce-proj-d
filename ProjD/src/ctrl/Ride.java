package ctrl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;

@WebServlet("/Ride.do")
public class Ride extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		if (request.getParameter("calculateCost") == null) {
		} else {
			Engine engine = Engine.getInstance();
			String startAddress = request.getParameter("startAddress");
			String endAddress = request.getParameter("endAddress");
			request.setAttribute("startAddress", startAddress);
			request.setAttribute("endAddress", endAddress);
			try {
				double cost = engine.doCost(startAddress, endAddress);
				request.setAttribute("result", "$"+cost);
			} catch (Exception e) {
				request.setAttribute("error", e.getMessage());
			}
		}
		
		this.getServletContext().getRequestDispatcher("/Ride.jspx").forward(request, response);
	} 

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doGet(request, response);
	}
}
