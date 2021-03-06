package com.talanlabs.mybatis.test.it.config;

import com.talanlabs.component.IComponent;
import com.talanlabs.mybatis.component.session.handler.INlsColumnHandler;

import java.util.HashMap;
import java.util.Map;

public class DefaultNlsColumnHandler implements INlsColumnHandler {

    private String languageCode;

    public DefaultNlsColumnHandler() {
        super();

        this.languageCode = "fra";
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public Object getContext() {
        return languageCode;
    }

    @Override
    public Map<String, Object> getAdditionalParameter(Class<? extends IComponent> componentClass, String propertyName) {
        Map<String, Object> map = new HashMap<>();
        map.put("languageCode", languageCode);
        return map;
    }

    @Override
    public String getSelectNlsColumnId(Class<? extends IComponent> componentClass, String propertyName) {
        return "com.talanlabs.mybatis.test.it.mapper.NlsMapper.selectNlsColumn";
    }

    @Override
    public boolean isUpdateDefaultNlsColumn(Class<? extends IComponent> componentClass, String propertyName) {
        return "eng".equals(languageCode);
    }

    @Override
    public String getMergeNlsColumnId(Class<? extends IComponent> componentClass, String propertyName) {
        return "com.talanlabs.mybatis.test.it.mapper.NlsMapper.mergeNlsColumn";
    }

    @Override
    public String getDeleteNlsColumnsId(Class<? extends IComponent> componentClass) {
        return "com.talanlabs.mybatis.test.it.mapper.NlsMapper.deleteNlsColumns";
    }

}
