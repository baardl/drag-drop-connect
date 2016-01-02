package com.bardlind.dragdrop;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import java.util.HashMap;
import java.util.Map;

public class DragDropConfiguration extends Configuration {
    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    private Map<String, Map<String, String>> viewRendererConfiguration = new HashMap<>();

    public Map<String, Map<String, String>> getViewRendererConfiguration() {
        Map<String, String> ftlMap = new HashMap<>();
//        ftlMap.put("swdemo/domain-owner-cashboard.ftl", "domain-owner-cashboard.ftl");
//        ftlMap.put("domain-owner-cashboard.ftl","swdemo/domain-owner-cashboard.ftl");
//        ftlMap.put("planet","swdemo/domain-owner-cashboard.ftl");
//        viewRendererConfiguration.put(".ftl", ftlMap);
        return viewRendererConfiguration;
    }

    public void setViewRendererConfiguration(Map<String, Map<String, String>> viewRendererConfiguration) {
        this.viewRendererConfiguration = viewRendererConfiguration;
    }
}
