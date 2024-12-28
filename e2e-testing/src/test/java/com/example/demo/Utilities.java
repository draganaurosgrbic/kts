package com.example.demo;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Utilities {
	
	public static DesiredCapabilities SSLIgnore() {
		DesiredCapabilities handlSSLErr = DesiredCapabilities.chrome(); 
		handlSSLErr.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		handlSSLErr.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
		return handlSSLErr;
	}

}
