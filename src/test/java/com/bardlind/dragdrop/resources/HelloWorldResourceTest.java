package com.bardlind.dragdrop.resources;

import com.bardlind.dragdrop.domain.counter.CounterService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.java8.jersey.OptionalMessageBodyWriter;
import io.dropwizard.java8.jersey.OptionalParamFeature;
import io.dropwizard.testing.junit.ResourceTestRule;
import no.cantara.dwsample.api.Saying;
import org.hibernate.validator.HibernateValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class HelloWorldResourceTest {

    private static final CounterService counterService = mock(CounterService.class);

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .registerModules(new Jdk8Module())
            .registerModules(new JavaTimeModule());

    private static final Validator VALIDATOR = Validation.byProvider(HibernateValidator.class).configure().addValidatedValueHandler(
            new io.dropwizard.java8.validation.valuehandling.OptionalValidatedValueUnwrapper())
            .addValidatedValueHandler(new io.dropwizard.java8.validation.valuehandling.OptionalValidatedValueUnwrapper()).buildValidatorFactory()
            .getValidator();

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .setMapper(MAPPER)
            .setValidator(VALIDATOR)
            .addProvider(OptionalMessageBodyWriter.class)
            .addProvider(OptionalParamFeature.class)
            .addResource(new HelloWorldResource("Hello, %s!", "Mr. Smith", counterService))
            .build();

    @Before
    public void before() {
        when(counterService.next()).thenReturn(3L);
    }

    @After
    public void after() {
        reset(counterService);
    }

    @Test
    public void testGetPerson() {
        assertThat(resources.client().target(com.bardlind.dragdrop.resources.HelloWorldResource.PATH).request().get(Saying.class))
                .isEqualTo(new Saying(3, "Hello, Mr. Smith!"));
        verify(counterService).next();
    }
}