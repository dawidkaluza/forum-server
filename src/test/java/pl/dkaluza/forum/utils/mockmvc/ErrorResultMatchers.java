package pl.dkaluza.forum.utils.mockmvc;

import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class ErrorResultMatchers {
    public static void expectError(ResultActions result, HttpStatus status) throws Exception {
        result
            .andExpect(status().is(status.value()))
            .andExpect(jsonPath("$.status").value(status.value()))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").exists());
    }

    public static void expectFieldError(ResultActions result, String field) throws Exception {
        result
            .andExpect(jsonPath("$.fieldErrors[*].field").value(hasItem(field)))
            .andExpect(jsonPath("$.fieldErrors[*].message").exists());
    }
}
