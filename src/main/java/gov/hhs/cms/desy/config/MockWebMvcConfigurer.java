package gov.hhs.cms.desy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import gov.hhs.cms.desy.web.rest.interceptors.mock.MockUserInterceptor;

@Configuration
@Profile("dev")
public class MockWebMvcConfigurer implements WebMvcConfigurer {
	private final Logger log = LoggerFactory.getLogger(MockWebMvcConfigurer.class);

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		log.info("********** MockWebMvcConfigurer in DEV Profile****************** ");
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("language");
		registry.addInterceptor(localeChangeInterceptor);
		registry.addInterceptor(new MockUserInterceptor()).addPathPatterns("/");
	}

}
