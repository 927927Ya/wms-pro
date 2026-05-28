package com.d0dd.wms.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MultiDateDeserializer extends JsonDeserializer<Date> {

    private static final List<String> patterns = new ArrayList<>();

    static {
        patterns.add("yyyy-MM-dd HH:mm:ss");
        patterns.add("yyyy-MM-dd");
        patterns.add("yyyy/MM/dd HH:mm:ss");
        patterns.add("yyyy/MM/dd");
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String dateStr = p.getText();
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        for (String pattern : patterns) {
            try {
                // SimpleDateFormat is not thread-safe, so we create a new instance for each attempt
                // or use a ThreadLocal if performance becomes a concern.
                // Given the context, creating a new instance is acceptable.
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                sdf.setLenient(false);
                return sdf.parse(dateStr);
            } catch (ParseException e) {
                // Ignore and try next pattern
            }
        }
        
        throw new IOException("Cannot parse date: " + dateStr);
    }
}
