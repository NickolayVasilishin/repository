package ru.argustelecom.accounter.controller;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.auth.login.AccountNotFoundException;

import ru.argustelecom.accounter.account.Account;
import ru.argustelecom.accounter.account.AccountLocal;
import ru.argustelecom.accounter.account.AccountRest;

@Stateless
public class AccountEJB implements AccountRest, AccountLocal {
	@PersistenceContext(unitName = "primary")
	private EntityManager em;
	
	@Override
	public void createAccounts(int number){
		while(number-- != 0)
			createAccount(new Account().setAmount(number*10.0f));
	}
	
	@Override
	public Account findAccountById(long id) throws AccountNotFoundException {
		Account account = em.find(Account.class, id);
		if(account == null)
			throw new AccountNotFoundException();
		return em.find(Account.class, id);
	}

	@Override
	public void createAccount(Account account) {
		em.persist(account); 
	}

	@Override
	public void addAmount(long id, float amount) throws AccountNotFoundException {
		Account account = em.find(Account.class, id);
		if(account != null)
			account.addAmount(amount);
		else 
			throw new AccountNotFoundException();
	}

	@Override
	public void setAmount(long id, float amount) throws AccountNotFoundException {
		Account account = em.find(Account.class, id);
		if(account != null)
			account.setAmount(amount);
		else
			throw new AccountNotFoundException();
	}

	@Override
	public void deleteAccount(long id) throws AccountNotFoundException {
		em.remove(findAccountById(id));
	}

	@Override
	public void withdrawAmount(long id, float amount) throws Exception {
		Account account = em.find(Account.class, id);
		if(account != null)
			account.withdrowAmount(amount);
		else
			throw new AccountNotFoundException();
	}
	
}
