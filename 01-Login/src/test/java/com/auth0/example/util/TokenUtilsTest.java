package com.auth0.example.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TokenUtilsTest {

    private final DecodedJWT jwt = JWT.decode("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjEyMzQ1Njc4OTAsImlhdCI6MTIzNDU2Nzg5MCwibmJmIjoxMjM0NTY3ODkwLCJqdGkiOiJodHRwczovL2p3dC5pby8iLCJhdWQiOiJodHRwczovL2RvbWFpbi5hdXRoMC5jb20iLCJzdWIiOiJsb2dpbiIsImlzcyI6ImF1dGgwIiwiZW1haWwiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZXh0cmFDbGFpbSI6IkpvaG4gRG9lIiwiZXh0cmFDbGFpbU51bGwiOm51bGwsImV4dHJhQ2xhaW1BcnJheSI6WyJibHVlIiwiZ3JlZW4iLCJyZWQiXSwiZXh0cmFDbGFpbU9iamVjdCI6eyJmYXZvcml0ZUNvbG9yIjoiYmx1ZSJ9LCJleHRyYUNsYWltTmVzdGVkIjp7ImZvbyI6WzEsMiwzXSwiYmFyIjp7InByb3AiOiJ2YWwifX19.eL18X9B9_mzqAdiLYSDchz_Pa89hcVWiwUj6-ZOrPYI");;
    private String json;
    private JsonNode jsonNode;

    @Before
    public void setup() throws Exception{
        json = TokenUtils.claimsAsJson(jwt.getClaims());
        ObjectMapper objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(json);
    }

    @Test
    public void claimsArray() {
        JsonNode extraClaimArray = jsonNode.get("extraClaimArray");

        assertThat(extraClaimArray.size(), is(3));
        assertThat(extraClaimArray.isArray(), is(true));
    }

    @Test
    public void claimsObject() {
        JsonNode extraClaimObject = jsonNode.get("extraClaimObject");

        assertThat(extraClaimObject.isObject(), is(true));
        assertThat(extraClaimObject.get("favoriteColor").asText(), is("blue"));
    }

    @Test
    public void claimsNull() {
        JsonNode extraClaimNull = jsonNode.get("extraClaimNull");

        assertThat(extraClaimNull.isNull(), is(true));
    }

    @Test
    public void claimsNested() {
        JsonNode extraClaimNested = jsonNode.get("extraClaimNested");

        JsonNode arrayNode = extraClaimNested.get("foo");
        assertThat(arrayNode.size(), is(3));
        assertThat(arrayNode.isArray(), is(true));

        JsonNode objectNode = extraClaimNested.get("bar");
        assertThat(objectNode.isObject(), is(true));
        assertThat(objectNode.get("prop").asText(), is("val"));
    }

}