package ru.argustelecom.accounter.api;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Account")
@XmlAccessorType(XmlAccessType.FIELD)
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private long id;
	@XmlAttribute(required = true)
	private float amount;

	public Account() {
		amount = 0;
	}

	public Account addAmount(float sum){
		amount += sum;
		return this;
	}
	
	public float getAmount(){
		return amount;
	}
	
	public Account withdrowAmount(float amount) throws Exception{
		this.amount -= amount;
		return this;
	}
	
	public Account setAmount(float amount){
		this.amount = amount;
		return this;
	}
	
	public long getId(){
		return id;
	}
	
	@Override
	public String toString(){
		return getId() + ": $" + getAmount();
	}
}
