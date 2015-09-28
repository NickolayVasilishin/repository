package ru.argustelecom.accounter.account;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "ACCOUNT")
@XmlRootElement(name = "Account")
@XmlAccessorType(XmlAccessType.FIELD)
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;
	@NotNull @Min(0)
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
		if(this.amount >= amount)
			this.amount -= amount;
		else
			throw new Exception();
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
