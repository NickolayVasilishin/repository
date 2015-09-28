package ru.argustelecom.accounter.api;

import javax.jws.WebService;
import javax.security.auth.login.AccountNotFoundException;

import ru.argustelecom.accounter.account.Account;

@WebService
public interface AccountService {
	long createAccount(Account account);
	Account getAccount(long id) throws AccountNotFoundException;
	void commit(Account account) throws AccountNotFoundException;
	void delete(long id) throws AccountNotFoundException;
}
