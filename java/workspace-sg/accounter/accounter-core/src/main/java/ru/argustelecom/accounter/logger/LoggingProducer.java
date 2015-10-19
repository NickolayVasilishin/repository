package ru.argustelecom.accounter.logger;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class LoggingProducer {
	@Produces @Log
	private Logger createLogger(InjectionPoint ip){
		return Logger.getLogger(ip.getMember().getDeclaringClass().getName());
	}
}
