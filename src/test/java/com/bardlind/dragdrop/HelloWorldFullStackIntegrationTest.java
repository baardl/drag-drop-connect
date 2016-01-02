package com.bardlind.dragdrop;

import com.bardlind.dragdrop.resources.HelloWorldResource;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import no.cantara.dwsample.api.Planet;
import no.cantara.dwsample.api.Saying;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloWorldFullStackIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<DragDropConfiguration> RULE =
            new DropwizardAppRule<>(Main.class, ResourceHelpers.resourceFilePath("drag-drop-connect-test.yml"));

    @Test
    public void getHelloWorld() {
        Client client = JerseyClientBuilder.createClient();

        Response response = client.target(
                String.format("http://localhost:%d" + HelloWorldResource.PATH, RULE.getLocalPort()))
                .request()
                .get();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void postHelloWorld() {
        Client client = JerseyClientBuilder.createClient();

        Response response = client.target(
                String.format("http://localhost:%d" + HelloWorldResource.PATH, RULE.getLocalPort()))
                .request()
                .post(Entity.json(new Planet("Neptune", "Bad Santa")));

        assertThat(response.getStatus()).isEqualTo(200);
        Saying saying = response.readEntity(Saying.class);
        assertThat(saying.getContent()).isEqualTo("Hello Bad Santa on planet Neptune");
    }

}
