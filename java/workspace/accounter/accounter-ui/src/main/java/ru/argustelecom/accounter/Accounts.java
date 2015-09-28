package ru.argustelecom.accounter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.argustelecom.accounter.account.AccountLocal;

/**
 * Servlet implementation class Accounts
 */
@WebServlet("/debug")
public class Accounts extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB
	AccountLocal accountEJB;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Accounts() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = null;
		if (request.getParameter("id") != null)
			if (request.getParameter("amount") != null) {
				
				try {
					accountEJB.addAmount(Long.valueOf(request.getParameter("id")),
							Float.valueOf(request.getParameter("amount")));
					message = "Account was edited: amount increased by $" + request.getParameter("amount");
				} catch (AccountNotFoundException e) {
					message = "Invalid account ID. Operation canceled";
					e.printStackTrace();
				} catch (NumberFormatException e) {
					message = "Invalid amount. Operation canceled.";
					e.printStackTrace();
				} finally {
					formResponse(response, message);
				}
				
			} else {
				float amount;
				try {
					amount = accountEJB.findAccountById(Long.valueOf(request.getParameter("id"))).getAmount();
					message = "Account has $" + amount;
				} catch (AccountNotFoundException e) {
					message = "Invalid account ID. Operation canceled";
					e.printStackTrace();
				} catch (NumberFormatException e) {
					message = "Invalid amount. Operation canceled.";
					e.printStackTrace();
				} finally {
					formResponse(response, message);
				}
			} else if(request.getParameter("create") != null) {
				accountEJB.createAccounts(Integer.valueOf(request.getParameter("create")));
				formResponse(response, "Accounts created");
			} else
				formResponse(response, "Invalid operation");
				

	}
	
	private void formResponse(HttpServletResponse response, String message) throws IOException{
		PrintWriter writer = response.getWriter();
        writer.println("<!DOCTYPE HTML>");
        writer.println("<html>");
        writer.println(" <head>");
        writer.println("  <title>Accounter</title>");
        writer.println(" </head>");
        writer.println(" <body>");
        writer.println(" <h1>");
        writer.println(message);
        writer.println(" </h1>");
        writer.println(" </body>");
        writer.println("</html>");
        writer.close();
	}

}
