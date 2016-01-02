package com.bardlind.dragdrop.health;

import com.codahale.metrics.health.HealthCheck;
import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Service(TemplateHealthCheck.NAME)
public class TemplateHealthCheck extends HealthCheck {
    public final static String NAME = "template";

    private final static Logger log = LoggerFactory.getLogger(TemplateHealthCheck.class);

    private final String template;

    @Configure
    public TemplateHealthCheck(@Configuration("template") String template) {
        this.template = template;
    }

    @Override
    protected Result check() throws Exception {
        log.trace("Check triggered: {}", kv("healthcheck", NAME));
        final String saying = String.format(template, "TEST");
        if (!saying.contains("TEST")) {
            log.trace("{}", kv("healthy", false));
            return Result.unhealthy("template doesn't include a name");
        }
        log.trace("{}", kv("healthy", true));
        return Result.healthy();
    }
}