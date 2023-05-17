package com.juliuskrah.camelconsumer;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonConstants;
import org.apache.camel.component.rest.RestComponent;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.spi.ComponentCustomizer;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Julius Krah
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CamelConsumerUtilsProperties.class)
@AutoConfigureBefore(name = { //
        "com.juliuskrah.camelconsumer.CamelConsumerAsyncAutoConfiguration", //
        "com.juliuskrah.camelconsumer.CamelConsumerSyncAutoConfiguration" //
})
public class CamelConsumerUtilsAutoconfiguration {
    @Autowired
    private CamelConsumerUtilsProperties properties;

    /**
     * Any customization of the REST Component is done here
     */
    @Bean
    ComponentCustomizer restCustomizer() {
        return (name, restComponent) -> {
            if (restComponent instanceof RestComponent) {
                var component = (RestComponent) restComponent;
                component.setHost(properties.getRest().getHostPort());
            }
        };
    }

    /**
     * Any additions to make on the camel context is done here
     */
    @Bean
    CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext context) {
                context.getGlobalOptions().put(JacksonConstants.ENABLE_TYPE_CONVERTER, "true");
                // allow Jackson json to convert to pojo types also (by default jackson only
                // converts to String and other simple types)
                context.getGlobalOptions().put(JacksonConstants.TYPE_CONVERTER_TO_POJO, "true");
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                // ignore
            }
        };
    }

    @Bean
    RouteBuilder sendByRestRoute() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                restConfiguration()

                        .bindingMode(RestBindingMode.json);
                from("direct:sendrequest") //
                        .routeId("sendByRest") //
                        .description("utility for invoking an external merchant API via REST") //
                        .log("send to merchant ${body}") //
                        .toF("rest:post:%s?bridgeEndpoint=true", properties.getRest().getUriTemplate())//
                        .end();
            }
        };
    }
}
