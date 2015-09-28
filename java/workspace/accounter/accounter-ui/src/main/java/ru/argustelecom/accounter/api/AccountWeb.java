package ru.argustelecom.accounter.api;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.Oneway;
import javax.jws.WebService;
import javax.security.auth.login.AccountNotFoundException;

import ru.argustelecom.accounter.account.Account;
import ru.argustelecom.accounter.account.AccountLocal;

@Stateless
@WebService(endpointInterface = "ru.argustelecom.accounter.api.AccountService")
public class AccountWeb implements AccountService {
	@EJB
	AccountLocal accountEJB;
	
	@Oneway
	@Override
	public long createAccount(Account account) {
		accountEJB.createAccount(account);
		return account.getId();
	}

	@Override
	public Account getAccount(long id) throws AccountNotFoundException {
		return accountEJB.findAccountById(id);
	}
	
	@Oneway
	@Override
	public void commit(Account account) throws AccountNotFoundException {
			accountEJB.findAccountById(account.getId());
			accountEJB.setAmount(account.getId(), account.getAmount());
	}

	@Override
	public void delete(long id) throws AccountNotFoundException {
		accountEJB.deleteAccount(id);
	}
}
