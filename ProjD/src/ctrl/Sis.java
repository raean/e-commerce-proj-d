package ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;
import model.StudentBean;

@WebServlet("/Sis.do")
public class Sis extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		if (request.getParameter("generate") == null) {
		} else {
			Engine engine = Engine.getInstance();
			String namePrefix = request.getParameter("namePrefix");
			String minGpa = request.getParameter("minGpa");
			String sortOption = request.getParameter("sortBy");
			request.setAttribute("namePrefix", namePrefix);
			request.setAttribute("minGpa", minGpa);
			try {
				List<StudentBean> result = engine.doSis(namePrefix, minGpa, sortOption);
				request.setAttribute("result", result);
			} catch (Exception e) {
				request.setAttribute("error", e.getMessage());
			}
		}
		
		this.getServletContext().getRequestDispatcher("/Sis.jspx").forward(request, response);
	} 

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doGet(request, response);
	}
}
