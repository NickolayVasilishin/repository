package ru.argustelecom.accounter;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpSession;

import ru.argustelecom.accounter.account.Account;
import ru.argustelecom.accounter.account.AccountLocal;

@Named
@ManagedBean
@SessionScoped
public class AccountController implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String REDIRECT = "?faces-redirect=true";
	
	@EJB
	private AccountLocal accountEJB;
	//Entity values
	private Account account;
	//Session values
	private long id;
	private float amount;
	
	public String doFindAccountById(){
		try {
			account = accountEJB.findAccountById(id);
		} catch (AccountNotFoundException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error with id", e.getMessage()));
			return null;
		}
		return "account.xhtml" + REDIRECT;
	}
	
	public String doCreateAccount(){
		account = new Account();
		accountEJB.createAccount(account);
		return "account.xhtml" + REDIRECT;
	}
	
	public void doWithdrawMoney(){
		try {
			accountEJB.withdrawAmount(account.getId(), amount);
			account = accountEJB.findAccountById(account.getId());
		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Not enough money at account", e.getMessage()));
		} 
	}
	
	public void doAddMoney(){
		try {
			accountEJB.addAmount(account.getId(), amount);
			account = accountEJB.findAccountById(account.getId());
		} catch (AccountNotFoundException e) {
		} 
	}
	
	public String doLogOut(){
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
		session.invalidate();
		return "index.xhtml" + REDIRECT;
	}
	
	
	public Account getAccount(){
		return account;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public long getId(){
		return id;
	}
	
	
	public void setAmount(float amount){
		this.amount = amount;
	}
	
	public float getAmount(){
		return amount;
	}
	
}
