package com.bs.lang;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class BsJarFileLoader extends URLClassLoader {

	public BsJarFileLoader(URL[] urls) {
		super(urls);
	}

	public void addFile(String path) throws MalformedURLException {
		String urlPath = "jar:file://" + path + "!/";
		addURL(new URL(urlPath));
	}

}
