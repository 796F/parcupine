package com.parq.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParqIncomingUrlServlet extends HttpServlet {

	// unique generated ID
	private static final long serialVersionUID = 554178319671031129L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		parseRequst(req, resp);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		parseRequst(req, resp);
	}

	/**
	 * Handle the HTTP request
	 * 
	 * @param req incoming request
	 * @param resp outging response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void parseRequst(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		
		String incomingUrl = req.getRequestURL().toString();
		
		// use string parsing to split apart the incoming url
		String[] stringParts = incomingUrl.split("/");
		
		// check to make sure the incoming url is well formed
		if (! stringParts[stringParts.length - 3].equalsIgnoreCase("p")) {
			throw new IllegalStateException("Incoming URL is malformed");
		}
		
		String clientId = stringParts[stringParts.length - 2];
		String parkingSpaceId = stringParts[stringParts.length - 1];
		
		
	    PrintWriter out = resp.getWriter();
	    out.println("Incoming URL: " + incomingUrl );
	    out.println("");
	    out.println("Client: " + clientId);
	    out.println("Parking Space: " + parkingSpaceId);
	}

}
