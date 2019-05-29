package gov.hhs.cms.desy.web.rest.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidInputStringImpl implements ConstraintValidator<ValidInputString, String> {

	private static final String SPECIAL_CHARACTERS_XSS = "[=<>]";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value != null) {
			Pattern regex = Pattern.compile(SPECIAL_CHARACTERS_XSS);
			Matcher matcher = regex.matcher(value);
			return !matcher.find();
		} else {
			return true;
		}
	}
}
