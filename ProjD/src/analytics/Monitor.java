package analytics;

import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class Monitor implements ServletContextListener, ServletRequestAttributeListener, ServletRequestListener, HttpSessionAttributeListener, HttpSessionListener, ServletContextAttributeListener {

	private final static int PRECENTAGE_MULTIPLIER = 100;

    public void contextInitialized(ServletContextEvent sce)  { 
    	int droneCount = 0;
    	int rideCount = 0;
    	int usageCount = 0;    	
    	int droneUsage = 0;
    	int rideAndDroneUsage = 0;
    	int sessionCount = 0;
    	int likelihood = 0;
    	sce.getServletContext().setAttribute("droneCount",droneCount);
    	sce.getServletContext().setAttribute("rideCount",rideCount);
    	sce.getServletContext().setAttribute("usageCount",usageCount);  
    	sce.getServletContext().setAttribute("droneUsage",droneUsage);
    	sce.getServletContext().setAttribute("rideAndDroneUsage",rideAndDroneUsage);
    	sce.getServletContext().setAttribute("sessionCount",sessionCount);
    	sce.getServletContext().setAttribute("likelihood",likelihood);
    }
    
    public void sessionCreated(HttpSessionEvent se)  { 
    	boolean droneDone = false;
    	boolean rideDone = false;
    	boolean added = false;
    	se.getSession().setAttribute("droneDone", droneDone);
    	se.getSession().setAttribute("rideDone", rideDone);
    	se.getSession().setAttribute("added", added);
    	se.getSession().getServletContext().setAttribute("sessionCount",(Integer.parseInt(se.getSession().getServletContext().getAttribute("sessionCount").toString())) + 1);
    }

    public void requestInitialized(ServletRequestEvent sre) { 
    	if (sre.getServletRequest().getParameter("calculateTime") != null) {
    		sre.getServletContext().setAttribute("droneCount",(Integer.parseInt(sre.getServletContext().getAttribute("droneCount").toString())) + 1);
    		((HttpServletRequest) sre.getServletRequest()).getSession().setAttribute("droneDone", true);
    		if (((HttpServletRequest) sre.getServletRequest()).getSession().getAttribute("droneDone").equals(true)
    				&& ((HttpServletRequest) sre.getServletRequest()).getSession().getAttribute("rideDone").equals(true)
    				&& ((HttpServletRequest) sre.getServletRequest()).getSession().getAttribute("added").equals(false)) {
    			sre.getServletContext().setAttribute("rideAndDroneUsage",(Integer.parseInt(sre.getServletContext().getAttribute("rideAndDroneUsage").toString())) + 1);
    			((HttpServletRequest) sre.getServletRequest()).getSession().setAttribute("added", true);
    		}
    	} else if (sre.getServletRequest().getParameter("calculateCost") != null) {
    		sre.getServletContext().setAttribute("rideCount",(Integer.parseInt(sre.getServletContext().getAttribute("rideCount").toString())) + 1);
    		((HttpServletRequest) sre.getServletRequest()).getSession().setAttribute("rideDone", true);
    		if (((HttpServletRequest) sre.getServletRequest()).getSession().getAttribute("droneDone").equals(true)
    				&& ((HttpServletRequest) sre.getServletRequest()).getSession().getAttribute("rideDone").equals(true)
    				&& ((HttpServletRequest) sre.getServletRequest()).getSession().getAttribute("added").equals(false)) {
    			sre.getServletContext().setAttribute("rideAndDroneUsage",(Integer.parseInt(sre.getServletContext().getAttribute("rideAndDroneUsage").toString())) + 1);
    			((HttpServletRequest) sre.getServletRequest()).getSession().setAttribute("added", true);
    		} 
    	}
    	
    	if (sre.getServletRequest().getParameter("compute") != null
    			|| sre.getServletRequest().getParameter("doit") != null
    			|| sre.getServletRequest().getParameter("next") != null
    			|| sre.getServletRequest().getParameter("generate") != null
    			|| sre.getServletRequest().getParameter("calculateTime") != null
    			|| sre.getServletRequest().getParameter("calculateCost") != null) {
    		sre.getServletContext().setAttribute("usageCount",(Integer.parseInt(sre.getServletContext().getAttribute("usageCount").toString())) + 1);
    	}
    	
    	if ((Integer.parseInt(sre.getServletContext().getAttribute("usageCount").toString())) != 0) {
    		sre.getServletContext().setAttribute("droneUsage",PRECENTAGE_MULTIPLIER*
    				(Integer.parseInt(sre.getServletContext().getAttribute("droneCount").toString()))/(Integer.parseInt(sre.getServletContext().getAttribute("usageCount").toString())));
    	}
    	
    	if ((Integer.parseInt(sre.getServletContext().getAttribute("rideAndDroneUsage").toString())) != 0) {
    		sre.getServletContext().setAttribute("likelihood",PRECENTAGE_MULTIPLIER*
    				(Integer.parseInt(sre.getServletContext().getAttribute("rideAndDroneUsage").toString()))/(Integer.parseInt(sre.getServletContext().getAttribute("sessionCount").toString())));
    	}
    }
	
}
