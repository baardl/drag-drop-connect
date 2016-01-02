package com.bardlind.dragdrop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan(basePackageClasses = DragDropSpringConfiguration.class)
@ImportResource("classpath:constretto/spring-constretto.xml")
public class DragDropSpringConfiguration {
}
