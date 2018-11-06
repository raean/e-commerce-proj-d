package adhoc;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class October
 */
@WebFilter(urlPatterns = {"/Sis.do"})
public class October implements Filter {

    /**
     * Default constructor. 
     */
    public October() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// place your code here
		HttpServletRequest hreq = (HttpServletRequest) request;
		if (Pattern.matches(".*/Ride.do", hreq.getRequestURI())) {
			response.getWriter().flush();
			response.getWriter().write("<a href=\"Dash.do\">Dashboard</a><p> This page is out of order...</p>");
		} else {
			if (!(hreq.getParameter("sortBy")==null)) {
				System.out.println(hreq.getParameter("sortBy") + ", post");
				if (!hreq.getParameter("sortBy").equals("NONE")) {
					response.getWriter().flush();
					response.getWriter().write("<a href=\"Dash.do\">Dashboard</a><p> The sorting feature is out of order...</p>");
				} else {
					chain.doFilter(hreq, response);
				}
			} else {
				chain.doFilter(hreq, response);
			}
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
