package com.strandls.userGroup.util;

import com.google.common.net.InternetDomainName;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.HttpHeaders;

public class AppUtil {

	public static String getDomain(HttpServletRequest request) {
		String domain = "";
		String tmpDomain = request.getHeader(HttpHeaders.HOST);
		if (tmpDomain != null && !tmpDomain.isEmpty() && tmpDomain.contains(".")) {
			domain = InternetDomainName.from(tmpDomain).topDomainUnderRegistrySuffix().toString();
		}
		return domain;
	}
}
