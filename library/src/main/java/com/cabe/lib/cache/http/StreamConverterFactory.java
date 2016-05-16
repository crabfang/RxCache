package com.cabe.lib.cache.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
 * Created by cabe on 16/5/13.
 */
public class StreamConverterFactory implements Converter {
    public static String ENCODE = "UTF-8";

    public static StreamConverterFactory create() {
        return new StreamConverterFactory();
    }

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        try {
            return body == null ? null : body.in();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public TypedOutput toBody(Object object) {
        return null;
    }

    public static byte[] inputStream2Bytes(InputStream is) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try {
                int nRead;
                byte[] data = new byte[16384];

                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }

                buffer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return buffer.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String inputStream2String(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, ENCODE));
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