package com.noradltd.demo.cucumberjvm.example;

import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@Cucumber.Options(format = { "pretty", "html:target/cucumber-html-report", "json-pretty:target/cucumber-report.json" }, monochrome = true) //<< monochrome so it's pretty in Eclipse
public class RunCukeTest {

}
