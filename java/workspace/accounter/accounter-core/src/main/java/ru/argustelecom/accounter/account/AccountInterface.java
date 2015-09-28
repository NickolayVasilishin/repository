package ru.argustelecom.accounter.account;

import javax.security.auth.login.AccountNotFoundException;

public interface AccountInterface {
	Account findAccountById(long id) throws AccountNotFoundException;
	void createAccount(Account account);
	void addAmount(long id, float amount) throws AccountNotFoundException;
	void setAmount(long id, float amount) throws AccountNotFoundException;
	void withdrawAmount(long id, float amount) throws Exception, AccountNotFoundException;
	void deleteAccount(long id) throws AccountNotFoundException;
	void createAccounts(int number);
}
