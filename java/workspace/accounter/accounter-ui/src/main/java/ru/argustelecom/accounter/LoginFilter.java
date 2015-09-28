package ru.argustelecom.accounter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.argustelecom.accounter.logger.Log;

public class LoginFilter implements Filter {

	@Inject
	AccountController controller;
//	@Inject
//	@Log
//	Logger log;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
//		log.info("Session id: " + req.getSession().getId());

		if (req.getRequestURL().toString().contains("account")) {

			if (controller != null && controller.getAccount() == null) {
				((HttpServletResponse) response).sendRedirect(req
						.getContextPath() + "/error.xhtml");
			}
		} else if (req.getRequestURL().toString().contains("index"))

			if (req.getRequestURL().toString().contains("debug"))
				return;

		if (!response.isCommitted()) {
			chain.doFilter(request, response);
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
