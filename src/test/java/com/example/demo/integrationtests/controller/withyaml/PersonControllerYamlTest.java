 package com.example.demo.integrationtests.controller.withyaml;

 import com.example.demo.configs.TestConfigs;
 import com.example.demo.integrationtests.controller.withyaml.mapper.YMLMapper;
 import com.example.demo.integrationtests.testcontainers.AbstractIntegrationTest;
 import com.example.demo.integrationtests.vo.AccountCredentialsVO;
 import com.example.demo.integrationtests.vo.PersonVO;
 import com.example.demo.integrationtests.vo.TokenVO;
 import com.fasterxml.jackson.core.JsonProcessingException;
 import com.fasterxml.jackson.core.type.TypeReference;
 import com.fasterxml.jackson.databind.DeserializationFeature;
 import com.fasterxml.jackson.dataformat.xml.XmlMapper;
 import io.restassured.builder.RequestSpecBuilder;
 import io.restassured.config.EncoderConfig;
 import io.restassured.config.RestAssuredConfig;
 import io.restassured.filter.log.LogDetail;
 import io.restassured.filter.log.RequestLoggingFilter;
 import io.restassured.filter.log.ResponseLoggingFilter;
 import io.restassured.http.ContentType;
 import io.restassured.specification.RequestSpecification;
 import org.junit.jupiter.api.*;
 import org.springframework.boot.test.context.SpringBootTest;

 import java.util.Arrays;
 import java.util.List;

 import static io.restassured.RestAssured.given;
 import static org.junit.jupiter.api.Assertions.*;

 @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
 @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
 public class PersonControllerYamlTest extends AbstractIntegrationTest {

     private static RequestSpecification requestSpecification;
     private static YMLMapper objectMapper;
     private static PersonVO person;

     @BeforeAll
     public static void setUp() {
         objectMapper = new YMLMapper();

         person = new PersonVO();
     }

     @Test
     @Order(0)
     public void authorization() {
         AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

         var accessToken = given()
                 .config(RestAssuredConfig
                         .config()
                         .encoderConfig(EncoderConfig
                                 .encoderConfig()
                                 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                 .basePath("/auth/signin")
                 .port(TestConfigs.SERVER_PORT)
                 .contentType(TestConfigs.CONTENT_TYPE_YML)
                 .accept(TestConfigs.CONTENT_TYPE_YML)
                 .body(user, objectMapper)
                 .when()
                 .post()
                 .then()
                 .statusCode(200)
                 .extract()
                 .body()
                 .as(TokenVO.class, objectMapper).getAccessToken();

         requestSpecification = new RequestSpecBuilder()
                 .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                 .setBasePath("/api/person/v1")
                 .setPort(TestConfigs.SERVER_PORT)
                 .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                 .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                 .build();
     }

     @Test
     @Order(1)
     public void testCreate() {
         mockPerson();

         var createdPerson = given()
                 .spec(requestSpecification)
                 .config(RestAssuredConfig
                         .config()
                         .encoderConfig(EncoderConfig
                                 .encoderConfig()
                                 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                 .contentType(TestConfigs.CONTENT_TYPE_YML)
                 .accept(TestConfigs.CONTENT_TYPE_YML)
                 .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                 .body(person, objectMapper)
                 .when()
                 .post()
                 .then()
                 .statusCode(200)
                 .extract()
                 .body()
                 .as(PersonVO.class, objectMapper);

         person = createdPerson;

         assertNotNull(createdPerson);
         assertNotNull(createdPerson.getId());
         assertNotNull(createdPerson.getFirstName());
         assertNotNull(createdPerson.getLastName());
         assertNotNull(createdPerson.getAddress());
         assertNotNull(createdPerson.getGender());
         assertTrue(createdPerson.getEnabled());

         assertTrue(createdPerson.getId() > 0);

         assertEquals("Richard", createdPerson.getFirstName());
         assertEquals("Stallman", createdPerson.getLastName());
         assertEquals("New York City, US", createdPerson.getAddress());
         assertEquals("Male", createdPerson.getGender());
     }

     @Test
     @Order(2)
     public void testDisableById() {
         var persistedPerson = given()
                 .spec(requestSpecification)
                 .config(RestAssuredConfig
                         .config()
                         .encoderConfig(EncoderConfig
                                 .encoderConfig()
                                 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                 .contentType(TestConfigs.CONTENT_TYPE_YML)
                 .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                 .pathParam("id", person.getId())
                 .when()
                 .patch("{id}")
                 .then()
                 .statusCode(200)
                 .extract()
                 .body()
                 .as(PersonVO.class, objectMapper);

         person = persistedPerson;

         assertNotNull(persistedPerson);
         assertNotNull(persistedPerson.getId());
         assertNotNull(persistedPerson.getFirstName());
         assertNotNull(persistedPerson.getLastName());
         assertNotNull(persistedPerson.getAddress());
         assertNotNull(persistedPerson.getGender());
         assertFalse(persistedPerson.getEnabled());

         assertTrue(persistedPerson.getId() > 0);

         assertEquals("Richard", persistedPerson.getFirstName());
         assertEquals("Stallman", persistedPerson.getLastName());
         assertEquals("New York City, US", persistedPerson.getAddress());
         assertEquals("Male", persistedPerson.getGender());
     }

     @Test
     @Order(3)
     public void testFindById() {
         mockPerson();

         var persistedPerson = given()
                 .spec(requestSpecification)
                 .config(RestAssuredConfig
                         .config()
                         .encoderConfig(EncoderConfig
                                 .encoderConfig()
                                 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                 .contentType(TestConfigs.CONTENT_TYPE_YML)
                 .accept(TestConfigs.CONTENT_TYPE_YML)
                 .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                 .pathParam("id", person.getId())
                 .when()
                 .get("{id}")
                 .then()
                 .statusCode(200)
                 .extract()
                 .body()
                 .as(PersonVO.class, objectMapper);

         person = persistedPerson;

         assertNotNull(persistedPerson);
         assertNotNull(persistedPerson.getId());
         assertNotNull(persistedPerson.getFirstName());
         assertNotNull(persistedPerson.getLastName());
         assertNotNull(persistedPerson.getAddress());
         assertNotNull(persistedPerson.getGender());
         assertFalse(persistedPerson.getEnabled());

         assertTrue(persistedPerson.getId() > 0);

         assertEquals("Richard", persistedPerson.getFirstName());
         assertEquals("Stallman", persistedPerson.getLastName());
         assertEquals("New York City, US", persistedPerson.getAddress());
         assertEquals("Male", persistedPerson.getGender());
     }

     @Test
     @Order(4)
     public void testUpdate() {
         person.setLastName("Piquet");

         var createdPerson = given()
                 .spec(requestSpecification)
                 .config(RestAssuredConfig
                         .config()
                         .encoderConfig(EncoderConfig
                                 .encoderConfig()
                                 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                 .contentType(TestConfigs.CONTENT_TYPE_YML)
                 .accept(TestConfigs.CONTENT_TYPE_YML)
                 .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                 .body(person, objectMapper)
                 .when()
                 .put()
                 .then()
                 .statusCode(200)
                 .extract()
                 .body()
                 .as(PersonVO.class, objectMapper);

         person = createdPerson;

         assertNotNull(createdPerson);
         assertNotNull(createdPerson.getId());
         assertNotNull(createdPerson.getFirstName());
         assertNotNull(createdPerson.getLastName());
         assertNotNull(createdPerson.getAddress());
         assertNotNull(createdPerson.getGender());
         assertFalse(createdPerson.getEnabled());

         assertEquals(person.getId(), createdPerson.getId());

         assertEquals("Richard", createdPerson.getFirstName());
         assertEquals("Piquet", createdPerson.getLastName());
         assertEquals("New York City, US", createdPerson.getAddress());
         assertEquals("Male", createdPerson.getGender());
     }

     @Test
     @Order(5)
     public void testDelete() {
         var content = given()
                 .spec(requestSpecification)
                 .config(RestAssuredConfig
                         .config()
                         .encoderConfig(EncoderConfig
                                 .encoderConfig()
                                 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                 .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                 .pathParam("id", person.getId())
                 .when()
                 .delete("{id}")
                 .then()
                 .statusCode(204);
     }

     @Test
     @Order(6)
     public void testFindAll() {
         var content = given()
                 .spec(requestSpecification)
                 .config(RestAssuredConfig
                         .config()
                         .encoderConfig(EncoderConfig
                                 .encoderConfig()
                                 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                 .contentType(TestConfigs.CONTENT_TYPE_YML)
                 .accept(TestConfigs.CONTENT_TYPE_YML)
                 .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                 .body(person, objectMapper)
                 .when()
                 .get()
                 .then()
                 .statusCode(200)
                 .extract()
                 .body()
                 .as(PersonVO[].class, objectMapper);

         List<PersonVO> people = Arrays.asList(content);

         PersonVO foundPersonOne = people.get(0);

         assertNotNull(foundPersonOne);
         assertNotNull(foundPersonOne.getId());
         assertNotNull(foundPersonOne.getFirstName());
         assertNotNull(foundPersonOne.getLastName());
         assertNotNull(foundPersonOne.getAddress());
         assertNotNull(foundPersonOne.getGender());

         assertTrue(foundPersonOne.getEnabled());

         assertEquals(1,foundPersonOne.getId());
         assertEquals("Guimarães", foundPersonOne.getFirstName());
         assertEquals("David", foundPersonOne.getLastName());
         assertEquals("Male", foundPersonOne.getAddress());
         assertEquals("Morais", foundPersonOne.getGender());

         PersonVO foundPersonTwo = people.get(1);

         assertNotNull(foundPersonTwo);
         assertNotNull(foundPersonTwo.getId());
         assertNotNull(foundPersonTwo.getFirstName());
         assertNotNull(foundPersonTwo.getLastName());
         assertNotNull(foundPersonTwo.getAddress());
         assertNotNull(foundPersonTwo.getGender());

         assertTrue(foundPersonTwo.getEnabled());

         assertEquals(2,foundPersonTwo.getId());
         assertEquals("Guimarães - Portugal", foundPersonTwo.getFirstName());
         assertEquals("Dinis", foundPersonTwo.getLastName());
         assertEquals("Male", foundPersonTwo.getAddress());
         assertEquals("Morais", foundPersonTwo.getGender());
     }

     @Test
     @Order(7)
     public void testFindAllWithoutToken() {
         RequestSpecification requestSpecificationWithoutToken = new RequestSpecBuilder()
                 .setBasePath("/api/person/v1")
                 .setPort(TestConfigs.SERVER_PORT)
                 .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                 .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                 .build();

         given()
                 .spec(requestSpecificationWithoutToken)
                 .config(RestAssuredConfig
                         .config()
                         .encoderConfig(EncoderConfig
                                 .encoderConfig()
                                 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                 .contentType(TestConfigs.CONTENT_TYPE_YML)
                 .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                 .body(person, objectMapper)
                 .when()
                 .get()
                 .then()
                 .statusCode(403);
     }

     private void mockPerson() {
         person.setFirstName("Richard");
         person.setLastName("Stallman");
         person.setAddress("New York City, US");
         person.setGender("Male");
         person.setEnabled(true);
     }
 }
