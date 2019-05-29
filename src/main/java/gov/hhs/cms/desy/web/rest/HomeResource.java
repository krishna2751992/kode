package gov.hhs.cms.desy.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeResource {

	@GetMapping("/")
	public ModelAndView redirectHome() {
		return new ModelAndView("redirect:/ui");
	}
}
