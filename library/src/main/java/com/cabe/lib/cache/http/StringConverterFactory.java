package com.cabe.lib.cache.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 *
 * Created by cabe on 16/4/12.
 */
public final class StringConverterFactory implements Converter {
    public StringConverterFactory() {
    }

    public static StringConverterFactory create() {
        return new StringConverterFactory();
    }

    public Object fromBody(TypedInput typedInput, Type type) throws ConversionException {
        String text = null;

        try {
            text = fromStream(typedInput.in());
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return text;
    }

    public TypedOutput toBody(Object o) {
        return null;
    }

    public static String fromStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        String line;
        while((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }

        return out.toString();
    }
}