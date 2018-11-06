package ctrl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;

@WebServlet("/Gps.do")
public class Gps extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		if (request.getParameter("compute") == null) {
		} else {
			Engine engine = Engine.getInstance();
			String toLat, toLong, fromLat, fromLong;
			toLat = request.getParameter("toLat");
			toLong = request.getParameter("toLong");
			fromLat = request.getParameter("fromLat");
			fromLong = request.getParameter("fromLong");
			request.setAttribute("toLat", toLat);
			request.setAttribute("toLong", toLong);
			request.setAttribute("fromLat", fromLat);
			request.setAttribute("fromLong", fromLong);
			try {
				int result = engine.doGps(fromLat, fromLong, toLat, toLong);
				request.setAttribute("result", result + " km");
			} catch (IllegalArgumentException e) {
				request.setAttribute("error", e.getMessage());
			}
		}
		
		this.getServletContext().getRequestDispatcher("/Gps.jspx").forward(request, response);
	} 

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doGet(request, response);
	}
}
