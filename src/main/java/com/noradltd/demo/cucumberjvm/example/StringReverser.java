package com.noradltd.demo.cucumberjvm.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringReverser {

	public String reverse(String input) {
		List<String> words = new ArrayList<String>(Arrays.asList(input
				.split("\\s+")));
		Collections.reverse(words);
		StringBuilder sb = new StringBuilder();
		for (String word : words) {
			sb.append(word).append(" ");
		}
		return sb.toString().trim();
	}

}
