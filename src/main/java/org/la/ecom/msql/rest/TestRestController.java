package org.la.ecom.msql.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestRestController {

	@GetMapping("/hello")
	public String get() {
		return "hello world";
	}
}
