package com.egen;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitialize extends AbstractAnnotationConfigDispatcherServletInitializer{
/**
 * implement the following methods
 */

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[]{AppConfig.class,JPAConfig.class};

	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[0];
	}

	@Override
	protected String[] getServletMappings() {
		return new  String[]{"/api/*"};
	}
}
