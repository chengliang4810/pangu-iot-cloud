package com.pangu.common.web.converter;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NDJSONMessageConverter extends AbstractGenericHttpMessageConverter<Object> {

    public NDJSONMessageConverter() {
        super(MediaType.APPLICATION_NDJSON);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return super.canRead(type, contextClass, mediaType) && type instanceof Class && !CharSequence.class.isAssignableFrom((Class<?>) type);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        List<Object> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputMessage.getBody(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(JSONUtil.toBean(line, type.getClass()));
            }
        }
        return list;
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        List<Object> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputMessage.getBody(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(JSONUtil.toBean(line, clazz));
            }
        }
        return list;
    }


    @Override
    protected void writeInternal(Object o, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        ObjectMapper objectMapper = new ObjectMapper();
        for (Object item : (List<?>) o) {
            String json = objectMapper.writeValueAsString(item);
            outputMessage.getBody().write(json.getBytes(StandardCharsets.UTF_8));
            outputMessage.getBody().write('\n');
        }
    }

}
