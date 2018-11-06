package ctrl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;

@WebServlet("/Admin")
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int droneCount = Integer.parseInt(request.getServletContext().getAttribute("droneCount").toString());
			int rideCount = Integer.parseInt(request.getServletContext().getAttribute("rideCount").toString());
			int usageCount = Integer.parseInt(request.getServletContext().getAttribute("usageCount").toString());
			int droneUsage = Integer.parseInt(request.getServletContext().getAttribute("droneUsage").toString());
			request.setAttribute("droneUsage", droneUsage);
		} catch (Exception e) {
			
		}
		this.getServletContext().getRequestDispatcher("/Admin.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
