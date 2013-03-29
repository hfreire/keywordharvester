package sh.exec.keywordharvester.web.controller;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller 
@RequestMapping("/")
public class KeywordHarvesterController {

	@RequestMapping(method = RequestMethod.GET)
	public String getKeywordHarvesterApp() throws HttpException, IOException {
		return "app";
	}
}
