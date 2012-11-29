package ru.rostpro.netUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;



public class MUrlCreator {	
	
	private static final String DEFAULT_HOST  = "rost-pro.nichost.ru";
	private static final String PROTOCOL = "http";
	private static final String SEPARATOR = "/";
	private static final String PREFIX = "ru";
		
	private List<NameValuePair> arguments;
	private String host;	
	private String[] prefix;
	private String protocol;
	private String operation;
	
	public MUrlCreator() {
		this(DEFAULT_HOST,PROTOCOL, new String[]{PREFIX});
	}
	public MUrlCreator(String host, String protocol, String prefix[]){
		arguments = new ArrayList<NameValuePair>();
		this.host = host;
		this.prefix = prefix;
		this.protocol = protocol;
		
	}
	public MUrlCreator(String host){
		this(host,  PROTOCOL, new String[]{PREFIX});
	}
	public MUrlCreator(String[] prefix){
		this(DEFAULT_HOST, PROTOCOL, prefix);
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setPrefix(){
		this.prefix = new String[]{PREFIX};
	}
	public String[] getPrefix() {
		return prefix;
	}
	public void setPrefix(String[] prefix) {
		this.prefix = prefix;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public boolean addArgument(String key, String value){
		NameValuePair arg = new BasicNameValuePair(key, value);
		if(arguments.contains(arg))
			arguments.remove(arg);
		return arguments.add(arg);
	}	
	public void clear(){
		arguments.clear();
	}
	public List<NameValuePair> getArgs(){
		if(arguments.size() == 0)
			return null;
		return arguments;
	}
	public String getHostUrl(){
		StringBuilder urlBuilder = new StringBuilder(protocol + ":" + SEPARATOR + SEPARATOR);
		urlBuilder.append(host);
		urlBuilder.append(SEPARATOR + getPrefixString());		
		return urlBuilder.toString();
	}
	public String getUrl(){
		StringBuilder urlBuilder = new StringBuilder(protocol + ":" + SEPARATOR + SEPARATOR);
		urlBuilder.append(host);
		urlBuilder.append(SEPARATOR + getPrefixString());
		urlBuilder.append(operation);
		urlBuilder.append("?");
		urlBuilder.append(URLEncodedUtils.format(arguments, "utf-8"));
		return urlBuilder.toString();
		
	}
	public String getPrefixString(){
		StringBuffer buffer = new StringBuffer();
		for(String pref: prefix){
			buffer.append(pref + SEPARATOR);
		}
		return buffer.toString();
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
}
