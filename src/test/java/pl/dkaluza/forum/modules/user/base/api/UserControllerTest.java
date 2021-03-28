package pl.dkaluza.forum.modules.user.base.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.dkaluza.forum.modules.user.base.api.UserResultMatchers.expectUser;
import static pl.dkaluza.forum.modules.user.base.api.UserResultMatchers.expectUsersPage;
import static pl.dkaluza.forum.modules.user.base.api.UserSnippets.*;
import static pl.dkaluza.forum.utils.mockmvc.ErrorResultMatchers.expectError;
import static pl.dkaluza.forum.utils.mockmvc.ErrorResultMatchers.expectFieldError;
import static pl.dkaluza.forum.utils.mockmvc.PagedResultMatchers.expectEmptyPage;
import static pl.dkaluza.forum.utils.restdocs.PaginationUtils.pageParamDescriptor;
import static pl.dkaluza.forum.utils.restdocs.PaginationUtils.sizeParamDescriptor;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith({SpringExtension.class})
public class UserControllerTest {
    @RegisterExtension
    final RestDocumentationExtension restDoc = new RestDocumentationExtension("target/generated-snippets/user/base");

    private final ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @Autowired
    public UserControllerTest(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    public void setUp(WebApplicationContext webAppContext, RestDocumentationContextProvider restDoc) {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webAppContext)
            .apply(
                documentationConfiguration(restDoc)
                    .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint())
            )
            .build();
    }

    @Test
    @Sql("valid-user-data.sql")
    public void register_emailAlreadyExists_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("name", "adam.kram");
        body.put("email", "mark@gmail.com");
        body.put("plainPassword", "adamPswrd");

        //When
        ResultActions result = mockMvc.perform(
            post("/register")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.CONFLICT);

        //Document
        result.andDo(document("register/emailAlreadyExists"));
    }

    @Test
    @Sql("valid-user-data.sql")
    public void register_invalidEmail_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("name", "adam.kram");
        body.put("email", "mark@local");
        body.put("plainPassword", "adamPswrd");

        //When
        ResultActions result = mockMvc.perform(
            post("/register")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "email");

        //Document
        result.andDo(document("register/invalidEmail"));
    }

    @Test
    @Sql("valid-user-data.sql")
    public void register_tooLongEmail_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("name", "adam.kram");
        body.put("email", "mamamamamamamamamamamamamamamamamamamamamamamamamamamamamamamamamamamamamamamammamamamamamammamamamamamamamammamamamamamammamama@gmail.com");
        body.put("plainPassword", "adamPswrd");

        //When
        ResultActions result = mockMvc.perform(
            post("/register")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "email");
    }

    @Test
    @Sql("valid-user-data.sql")
    public void register_nameAlreadyExists_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("name", "mark.kram");
        body.put("email", "adam@gmail.com");
        body.put("plainPassword", "adamPswrd");

        //When
        ResultActions result = mockMvc.perform(
            post("/register")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.CONFLICT);

        //Document
        result.andDo(document("register/nameAlreadyExists"));
    }

    @Test
    @Sql("valid-user-data.sql")
    public void register_invalidName_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("name", "Adam Kram");
        body.put("email", "adam@gmail.com");
        body.put("plainPassword", "adamPswrd");

        //When
        ResultActions result = mockMvc.perform(
            post("/register")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "name");

        //Document
        result.andDo(document("register/invalidName"));
    }

    @Test
    @Sql("valid-user-data.sql")
    public void register_tooShortName_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("name", "ad");
        body.put("email", "adam@gmail.com");
        body.put("plainPassword", "adamPswrd");

        //When
        ResultActions result = mockMvc.perform(
            post("/register")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "name");
    }

    @Test
    @Sql("valid-user-data.sql")
    public void register_tooLongName_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("name", "adamadamadamadamadamadamadamadamadam");
        body.put("email", "adam@gmail.com");
        body.put("plainPassword", "adamPswrd");

        //When
        ResultActions result = mockMvc.perform(
            post("/register")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "name");
    }

    @Test
    @Sql("valid-user-data.sql")
    public void register_invalidPassword_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("name", "adam.kram");
        body.put("email", "adam@gmail.com");
        body.put("plainPassword", "super strong password");

        //When
        ResultActions result = mockMvc.perform(
            post("/register")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "plainPassword");

        //Document
        result.andDo(document("register/invalidPassword"));
    }

    @Test
    @Sql("valid-user-data.sql")
    public void register_tooShortPassword_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("name", "adam.kram");
        body.put("email", "adam@gmail.com");
        body.put("plainPassword", "123");

        //When
        ResultActions result = mockMvc.perform(
            post("/register")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "plainPassword");
    }

    @Test
    @Sql("valid-user-data.sql")
    public void register_tooLongPassword_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("name", "adam.kram");
        body.put("email", "adam@gmail.com");
        body.put("plainPassword", "adamadamadamadamadamadamadamadamadam");

        //When
        ResultActions result = mockMvc.perform(
            post("/register")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "plainPassword");
    }

    @Test
    @Sql("valid-user-data.sql")
    public void register_validData_responseWithCreatedUser() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("name", "adam.kram");
        body.put("email", "adam@gmail.com");
        body.put("plainPassword", "adamPswrd");

        //When
        ResultActions result = mockMvc.perform(
            post("/register")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result.andExpect(status().isAccepted());
        expectUser(result, 2);
        result
            .andExpect(jsonPath("$.enabled").value(false))
            .andExpect(jsonPath("$._links.df:confirmRegistration").exists())
            .andExpect(jsonPath("$._links.df:resendRegistrationToken").exists());

        //Document
        result.andDo(document(
            "register/success",
            requestFields(
                fieldWithPath("name").description(
                    "Name that will identify the user on public posts, comments, etc. " +
                        "Must be unique and contain only non-whitespace, from 3 to 32 chars."
                ),
                fieldWithPath("email").description(
                    "E-mail address that will be used to contact with account's owner in case of confirm registration, reset password, etc. " +
                        "Must be unique and contain from 3 to 128 chars."
                ),
                fieldWithPath("plainPassword").description(
                    "Password used to confirm authentication of the account. " +
                        "Must contain from 5 to 32 chars."
                )
            ),
            registeredUserResponseFields(),
            registeredUserLinks()
        ));
    }

    //TODO
    // move pagination tests to some different class
    // that's not responsibility of UserController to test whether the pagination works correctly
    // maybe at the moment it should be enough to write a description about paginating request results on the docs index page?
    // maybe spring utilities shouldn't be tested here? Hard to say at the moment.
    @Test
    public void findAll_noUsers_responseWithEmptyList() throws Exception {
        //Given, When
        ResultActions result = mockMvc.perform(
            get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result.andExpect(status().isOk());
        expectEmptyPage(result);
    }

    @Test
    @Sql({"valid-user-data.sql", "valid-10-male-users-data.sql"})
    public void findAll_elevenUsers_responseWithNextPageOnly() throws Exception {
        //Given, When
        ResultActions result = mockMvc.perform(
            get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.df:user").isArray())
            .andExpect(jsonPath("$._embedded.df:user.length()").value(10))
            .andExpect(jsonPath("$._embedded.df:user[1].id").value(2))
            .andExpect(jsonPath("$._embedded.df:user[1].name").value("kieran.kram"))
            .andExpect(jsonPath("$._embedded.df:user[1].email").value("kieran@gmail.com"))
            .andExpect(jsonPath("$._embedded.df:user[1].enabled").value(false))
            .andExpect(jsonPath("$._embedded.df:user[1]._links").exists())
            .andExpect(jsonPath("$._embedded.df:user[1]._links.self").exists())
            .andExpect(jsonPath("$._links.first").exists())
            .andExpect(jsonPath("$._links.prev").doesNotExist())
            .andExpect(jsonPath("$._links.self").exists())
            .andExpect(jsonPath("$._links.next").exists())
            .andExpect(jsonPath("$._links.last").exists())
            .andExpect(jsonPath("$._links.curies").exists())
            .andExpect(jsonPath("$.page.size").value(10))
            .andExpect(jsonPath("$.page.totalElements").value(11))
            .andExpect(jsonPath("$.page.totalPages").value(2))
            .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    @Sql({"valid-user-data.sql", "valid-10-male-users-data.sql"})
    public void findAll_elevenUsersAndSecondPage_responseWithPreviousPageOnly() throws Exception {
        //Given, When
        ResultActions result = mockMvc.perform(
            get("/user?page=1")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.df:user").isArray())
            .andExpect(jsonPath("$._embedded.df:user.length()").value(1))
            .andExpect(jsonPath("$._embedded.df:user[0].id").value(11))
            .andExpect(jsonPath("$._embedded.df:user[0].name").value("michael.kram"))
            .andExpect(jsonPath("$._embedded.df:user[0].email").value("michael@gmail.com"))
            .andExpect(jsonPath("$._embedded.df:user[0].enabled").value(false))
            .andExpect(jsonPath("$._embedded.df:user[0]._links").exists())
            .andExpect(jsonPath("$._embedded.df:user[0]._links.self").exists())
            .andExpect(jsonPath("$._links.first").exists())
            .andExpect(jsonPath("$._links.prev").exists())
            .andExpect(jsonPath("$._links.self").exists())
            .andExpect(jsonPath("$._links.next").doesNotExist())
            .andExpect(jsonPath("$._links.last").exists())
            .andExpect(jsonPath("$._links.curies").exists())
            .andExpect(jsonPath("$.page.size").value(10))
            .andExpect(jsonPath("$.page.totalElements").value(11))
            .andExpect(jsonPath("$.page.totalPages").value(2))
            .andExpect(jsonPath("$.page.number").value(1));
    }

    @Test
    @Sql({"valid-10-male-users-data.sql"})
    public void findAll_tenUsers_responseWithNoPreviousAndNextPage() throws Exception {
        //Given, When
        ResultActions result = mockMvc.perform(
            get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.df:user").isArray())
            .andExpect(jsonPath("$._embedded.df:user.length()").value(10))
            .andExpect(jsonPath("$._embedded.df:user[0].id").value(1))
            .andExpect(jsonPath("$._embedded.df:user[0].name").value("kieran.kram"))
            .andExpect(jsonPath("$._embedded.df:user[0].email").value("kieran@gmail.com"))
            .andExpect(jsonPath("$._embedded.df:user[0].enabled").value(false))
            .andExpect(jsonPath("$._embedded.df:user[*]._links").exists())
            .andExpect(jsonPath("$._embedded.df:user[*]._links.self").exists())
            .andExpect(jsonPath("$._links.first").doesNotExist())
            .andExpect(jsonPath("$._links.prev").doesNotExist())
            .andExpect(jsonPath("$._links.self").exists())
            .andExpect(jsonPath("$._links.next").doesNotExist())
            .andExpect(jsonPath("$._links.last").doesNotExist())
            .andExpect(jsonPath("$._links.curies").exists())
            .andExpect(jsonPath("$.page.size").value(10))
            .andExpect(jsonPath("$.page.totalElements").value(10))
            .andExpect(jsonPath("$.page.totalPages").value(1))
            .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    @Sql({"valid-10-female-users-data.sql"})
    public void findAll_tenUsersWithThreePerPage_responseWithPages() throws Exception {
        //Given, When
        ResultActions result = mockMvc.perform(
            get("/user?size=3")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.df:user").isArray())
            .andExpect(jsonPath("$._embedded.df:user.length()").value(3))
            .andExpect(jsonPath("$._embedded.df:user[0].id").value(1))
            .andExpect(jsonPath("$._embedded.df:user[0].name").value("marie.kramma"))
            .andExpect(jsonPath("$._embedded.df:user[0].email").value("marie@gmail.com"))
            .andExpect(jsonPath("$._embedded.df:user[0].enabled").value(true))
            .andExpect(jsonPath("$._embedded.df:user[*]._links").exists())
            .andExpect(jsonPath("$._embedded.df:user[*]._links.self").exists())
            .andExpect(jsonPath("$._links.first").exists())
            .andExpect(jsonPath("$._links.prev").doesNotExist())
            .andExpect(jsonPath("$._links.self").exists())
            .andExpect(jsonPath("$._links.next").exists())
            .andExpect(jsonPath("$._links.last").exists())
            .andExpect(jsonPath("$._links.curies").exists())
            .andExpect(jsonPath("$.page.size").value(3))
            .andExpect(jsonPath("$.page.totalElements").value(10))
            .andExpect(jsonPath("$.page.totalPages").value(4))
            .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    @Sql({"valid-10-female-users-data.sql"})
    public void findAll_tenUsersAndSecondPage_noList() throws Exception {
        //Given, When
        ResultActions result = mockMvc.perform(
            get("/user?page=1")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.df:user").doesNotExist())
            .andExpect(jsonPath("$._links.first").exists())
            .andExpect(jsonPath("$._links.prev").exists())
            .andExpect(jsonPath("$._links.self").exists())
            .andExpect(jsonPath("$._links.next").doesNotExist())
            .andExpect(jsonPath("$._links.last").exists())
            .andExpect(jsonPath("$.page.size").value(10))
            .andExpect(jsonPath("$.page.totalElements").value(10))
            .andExpect(jsonPath("$.page.totalPages").value(1))
            .andExpect(jsonPath("$.page.number").value(1));
    }

    @Test
    @Sql({"valid-10-male-users-data.sql", "valid-10-female-users-data.sql"})
    public void findAll_twelveUsers_responseWithPages() throws Exception {
        //Given, When
        ResultActions result = mockMvc.perform(
            get("/user?size=5&page=1")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result.andExpect(status().isOk());
        expectUsersPage(result, 6, 5);

        //Document
        result.andDo(document(
            "getAllUsers/success",
            requestParameters(
                pageParamDescriptor(),
                sizeParamDescriptor()
            ),
            usersPageResponseFields(),
            usersPageLinks()
        ));
    }

    @Test
    @Sql("valid-user-data.sql")
    public void get_notExistingId_responseWithProperError() throws Exception {
        //Given, When
        ResultActions result = mockMvc.perform(
            get("/user/{id}", 34)
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.NOT_FOUND);

        //Document
        result.andDo(document("getUser/userNotFound"));
    }

    @Test
    @Sql("valid-user-data.sql")
    public void get_existingId_responseWithUser() throws Exception {
        //Given, When
        ResultActions result = mockMvc.perform(
            get("/user/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result.andExpect(status().isOk());
        expectUser(result, 1);

        //Document
        result.andDo(document(
            "getUser/success",
            pathParameters(
                parameterWithName("id").description("Id of the requested user")
            ),
            userResponseFields(),
            userLinks()
        ));
    }
}
