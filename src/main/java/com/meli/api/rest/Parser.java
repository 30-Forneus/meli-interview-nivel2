package com.meli.api.rest;

import static com.meli.api.rest.Constant.UTF_8;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import lombok.SneakyThrows;

public enum Parser {
	
	INSTANCE;
		
    public Map<String, List<String>> splitQuery(String query) {
        if (query == null || "".equals(query)) {
            return Collections.emptyMap();
        }

        return Pattern.compile("&").splitAsStream(query)
            .map(s -> Arrays.copyOf(s.split("="), 2))
            .collect(groupingBy(s -> decode(s[0]), mapping(s -> decode(s[1]), toList())));

    }

    @SneakyThrows(UnsupportedEncodingException.class)
    String decode(final String encoded) {
    	return encoded == null ? null : URLDecoder.decode(encoded, UTF_8);
    }

}
