package com.auth0.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.sun.deploy.uitoolkit.impl.awt.OldPluginAWTUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootApplication
@ComponentScan(basePackages = "com.auth0.example")
@EnableAutoConfiguration
@PropertySources({
		@PropertySource("classpath:application.properties"),
		@PropertySource("classpath:auth0.properties")
})
public class App {

	public static void main(final String[] args) throws Exception {
		SpringApplication.run(App.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	public static class OAuthState {
		public final String state;
		public final Date startTime;

        public OAuthState(String state) {
            this.state = state;
            this.startTime = new Date();
        }

        public String getState() {
            return state;
        }

        public Date getStartTime() {
            return startTime;
        }

        public static OAuthState withTenant(String tenant_id) {
			return new OAuthState(UUID.randomUUID().toString() + "|" + tenant_id);
		}
	}

	public static interface OAuthStateRepo {

	    void initializeOAuthState(OAuthState state);

		Collection<OAuthState> getOrphaned();

		OAuthState getOAuthState(String state);
	}

	@Bean
    public OAuthStateRepo oAuthStateRepo() {
	    return new OAuthStateRepo() {

	        private final Map<String, OAuthState> stateMap = Maps.newConcurrentMap();

            @Override
            public void initializeOAuthState(OAuthState oAuthState) {
                stateMap.put(oAuthState.getState(), oAuthState);
            }

            @Override
            public Collection<OAuthState> getOrphaned() {
                return stateMap.values();
            }

            @Override
            public OAuthState getOAuthState(String state) {
                return stateMap.remove(state);
            }
        };
    }
}
