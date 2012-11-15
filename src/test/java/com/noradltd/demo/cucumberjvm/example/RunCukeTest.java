package com.noradltd.demo.cucumberjvm.example;

import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@Cucumber.Options(format = { "pretty", "html:target/cucumber" })
public class RunCukeTest {

}
