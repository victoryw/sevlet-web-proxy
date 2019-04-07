package com.thoughtworks.picc.reverse.proxy;

import javafx.util.Pair;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.stream.Collectors;

public class HttpSessionHeadConverter {
    static final String SESSION_ATTRIBUTION_SPLITTER = ":";
    static final String SESSION_DELIMITER = ";";

    static String convertSessionAttributeToString(HttpSession session) {
        final Enumeration<String> attributeNames = session.getAttributeNames();
        final ArrayList<String>  list = Collections.list(attributeNames);
        return list.stream().map(name -> {
            try {

                final Object value = session.getAttribute(name);
                String base64Value = ObjectByteConverter.toString(value);
                String base64Name = ObjectByteConverter.toString(name);
                return base64Name + SESSION_ATTRIBUTION_SPLITTER + base64Value;
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.joining(SESSION_DELIMITER));
    }

    static void convertStringToSession(HttpSession session, String attributes) {
        Arrays.stream(attributes.split(SESSION_DELIMITER)).map(str -> {
            final String[] split = str.split(SESSION_ATTRIBUTION_SPLITTER);
            final String name = (String)ObjectByteConverter.fromString(split[0]);
            final Object value = ObjectByteConverter.fromString(split[1]);
            return new Pair<>(name, value);
        }).forEach(pair -> session.setAttribute(pair.getKey(), pair.getValue()));
    }
}
