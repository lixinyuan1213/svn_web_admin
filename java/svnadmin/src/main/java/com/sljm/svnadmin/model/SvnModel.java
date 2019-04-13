package com.sljm.svnadmin.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class SvnModel {
	@Value("${mysvn.localhost}")
	private String localHost;
	@Value("${mysvn.host}")
	private String host;
	@Value("${mysvn.path}")
	private String svnPath;
	public String getLocalHost() {
		return localHost;
	}
	public String getHost() {
		return host;
	}
	public String getSvnPath() {
		return svnPath;
	}
	public void setLocalHost(String localHost) {
		this.localHost = localHost;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public void setSvnPath(String svnPath) {
		this.svnPath = svnPath;
	}
}
