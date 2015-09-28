package ru.argustelecom.accounter.api;

import java.net.MalformedURLException;
import java.net.URL;

import javax.security.auth.login.AccountNotFoundException;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class AccountServiceClient implements AccountService {
	private static final String LOCALHOST = "localhost";
	private static final String JBOSS14 = "jboss14";
	private static final String DEFAULT_PORT = "8080";
	
	private AccountService accountService;

	public AccountServiceClient() throws MalformedURLException {
		init(LOCALHOST, DEFAULT_PORT);
	}
	
	public AccountServiceClient(String host, String port) throws MalformedURLException {
		init(host, port);
	}

	private void init(String host, String port) throws MalformedURLException{
		URL wsdlLocation = new URL("http://" + host + ":" + port + "/AccountWeb?wsdl");
		QName serviceName = new QName("http://api.accounter.argustelecom.ru/",
				"AccountWebService");
		QName portName = new QName("http://api.accounter.argustelecom.ru/",
				"AccountWebPort");

		Service service = Service.create(wsdlLocation, serviceName);
		accountService = service.getPort(portName, AccountService.class);
	}
	
	@Override
	public long createAccount(Account account) {
		return accountService.createAccount(account);
	}

	@Override
	public Account getAccount(long id) throws AccountNotFoundException {
		return accountService.getAccount(id);
	}

	@Override
	public boolean commit(Account account) throws AccountNotFoundException {
		return accountService.commit(account);
	}
	
	@Override
	public void delete(long id) throws AccountNotFoundException {
		accountService.delete(id);
	}

	public static void main(String[] args) {
		int i = 1;
		try {
			AccountService client = new AccountServiceClient(JBOSS14, DEFAULT_PORT);
			while(i > 0)
				client.delete(i++);
			// Account ac = new Account().setAmount(10);
//			 System.out.println("new account was created with id: " +
//			 client.createAccount(ac));
//			 while(i<2)
//			 System.out.println(client.getAccount(i++));
//			 client.createAccount(new Account().setAmount(56.4f));
//			 client.createAccount(new Account().setAmount(677.12f));
//			
			 //Account account = client.getAccount(1);
//			 System.out.println(account.getId() + " " + account.getAmount());
//			 client.commit(client.getAccount(1).addAmount(1000));
//			 System.out.println(client.getAccount(1));
		} catch (MalformedURLException e) {
			System.out.println("Error with initializing WS");
		} catch (AccountNotFoundException e) {
			System.out.println("Error. Account was not found");
			if(i!=1)
				System.out.println("Total " + i + " accounts were deleted");
		}

	}

	

}
