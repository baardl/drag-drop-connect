package com.bardlind.dragdrop;

import com.bardlind.dragdrop.spring.SpringContextLoaderListener;
import com.codahale.metrics.health.HealthCheck;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.servlets.tasks.Task;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.slf4j.Logger;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

public class Main extends Application<DragDropConfiguration> {
    private static final Logger log = getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            args = new String[] {"server", "drag-drop-connect.yml"};
        }
        new Main().run(args);
    }

    @Override
    public String getName() {
        return "drag-drop-connect";
    }

    @Override
    public void initialize(Bootstrap<DragDropConfiguration> bootstrap) {
        bootstrap.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        bootstrap.addBundle(new AssetsBundle());

//        bootstrap.addBundle(new AssetsBundle("/assets/index.html","/","index.html"));
//        bootstrap.addBundle(new AssetsBundle("/assets/android/bpmservice/www/","/android/", "index.html","android"));
        bootstrap.addBundle(new AssetsBundle("/assets/index.html","/","index.html"));
//        bootstrap.addBundle(new AssetsBundle("/assets/android","/android","index.html"));
//        bootstrap.addBundle(new AssetsBundle("/assets/android/ui","/android/ui",null,"android/ui"));
//        bootstrap.addBundle(new AssetsBundle("/assets/android/libs","/android/libs",null,"android/libs"));
//        bootstrap.addBundle(new AssetsBundle("/assets/android/sprite","/android/sprite",null,"android/sprite"));
//        bootstrap.addBundle(new AssetsBundle("/assets/android","/android/fonts",null,"fonts"));
//        bootstrap.addBundle(new AssetsBundle("/assets/android","/android/ui",null,"ui"));
//        bootstrap.addBundle(new AssetsBundle("/assets/android","/android/lib",null,"lib"));
//        bootstrap.addBundle(new AssetsBundle("/assets/favicon.ico","/favicon.ico"));
        bootstrap.addBundle(new AssetsBundle("/assets/css", "/css", null, "css"));
        bootstrap.addBundle(new AssetsBundle("/assets/js", "/js", null, "js"));
        bootstrap.addBundle(new AssetsBundle("/assets/fonts", "/fonts", null, "fonts"));
        bootstrap.addBundle(new AssetsBundle("/assets/images", "/images", null, "images"));
        bootstrap.addBundle(new ViewBundle<DragDropConfiguration>(){
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(DragDropConfiguration config) {
                return config.getViewRendererConfiguration();
            }
        });
        bootstrap.addBundle(new Java8Bundle());
        bootstrap.addBundle(new SwaggerBundle<DragDropConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(DragDropConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
        /*
        bootstrap.addBundle(new Java8Bundle());
        bootstrap.addBundle(new SwaggerBundle<DragDropConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(DragDropConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
        */
    }

    @Override
    public void run(DragDropConfiguration configuration, Environment environment) {
        AnnotationConfigWebApplicationContext parent = createSpringParentContext(configuration);
        AnnotationConfigWebApplicationContext ctx = createSpringContext(parent);
        logBeans(ctx);
        environment.servlets().addServletListeners(new SpringContextLoaderListener(ctx));
        //Websocket
//        BroadcastServlet broadcastServlet = ctx.getBean(BroadcastServlet.class);
//        environment.getApplicationContext().getServletHandler().addServletWithMapping(
//                new ServletHolder(broadcastServlet), "/producerws/*");
        addManaged(environment, ctx);
        addTasks(environment, ctx);
        addHealthChecks(environment, ctx);
        addResources(environment, ctx);
        addProviders(environment, ctx);
    }

    private void logBeans(AnnotationConfigWebApplicationContext ctx) {
        Map<String, Object> beans = ctx.getBeansWithAnnotation(Path.class);
        Set<String> beanNames = beans.keySet();
        for (String beanName : beanNames) {
            log.info("Bean found {}",beans.get(beanName).getClass());
        }
    }

    private AnnotationConfigWebApplicationContext createSpringParentContext(DragDropConfiguration configuration) {
        AnnotationConfigWebApplicationContext parent = new AnnotationConfigWebApplicationContext();
        parent.refresh();
        parent.getBeanFactory().registerSingleton("configuration", configuration);
        parent.registerShutdownHook();
        parent.start();
        return parent;
    }

    private AnnotationConfigWebApplicationContext createSpringContext(AnnotationConfigWebApplicationContext parent) {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.setParent(parent);
        ctx.register(DragDropSpringConfiguration.class);
        ctx.refresh();
        ctx.registerShutdownHook();
        ctx.start();
        return ctx;
    }
    private void addManaged(Environment environment, AnnotationConfigWebApplicationContext ctx) {
        Map<String, Managed> managed = ctx.getBeansOfType(Managed.class);
        for(Map.Entry<String,Managed> entry : managed.entrySet()) {
            environment.lifecycle().manage(entry.getValue());
        }
    }

    private void addHealthChecks(Environment environment, AnnotationConfigWebApplicationContext ctx) {
        Map<String, HealthCheck> healthChecks = ctx.getBeansOfType(HealthCheck.class);
        for(Map.Entry<String,HealthCheck> entry : healthChecks.entrySet()) {
            environment.healthChecks().register(entry.getKey(), entry.getValue());
        }
    }

    private void addResources(Environment environment, AnnotationConfigWebApplicationContext ctx) {
        Map<String, Object> resources = ctx.getBeansWithAnnotation(Path.class);
        for(Map.Entry<String,Object> entry : resources.entrySet()) {
            environment.jersey().register(entry.getValue());
        }
    }

    private void addTasks(Environment environment, AnnotationConfigWebApplicationContext ctx) {
        Map<String, Task> tasks = ctx.getBeansOfType(Task.class);
        for(Map.Entry<String,Task> entry : tasks.entrySet()) {
            environment.admin().addTask(entry.getValue());
        }
    }

    private void addProviders(Environment environment, AnnotationConfigWebApplicationContext ctx) {
        Map<String, Object> providers = ctx.getBeansWithAnnotation(Provider.class);
        for(Map.Entry<String,Object> entry : providers.entrySet()) {
            environment.jersey().register(entry.getValue());
        }
    }
}
