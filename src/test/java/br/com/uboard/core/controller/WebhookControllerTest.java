package br.com.uboard.core.controller;

import br.com.uboard.core.model.operations.GitlabEventProjectForm;
import br.com.uboard.core.model.operations.MergeRequestObjectAttributesForm;
import br.com.uboard.core.model.operations.ReceiveMergeRequestEventForm;
import br.com.uboard.core.model.operations.ReceiveTagEventForm;
import br.com.uboard.core.service.CreateIssueService;
import br.com.uboard.core.service.CreateTagReleaseNotesService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(WebhookController.class)
@ExtendWith(MockitoExtension.class)
class WebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateIssueService createIssueService;

    @MockBean
    private CreateTagReleaseNotesService createTagReleaseNotesService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        this.objectMapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @DisplayName("Testing if request returns 201 when merge request event is successfully processed")
    void testIfRequestReturns201WhenMergeRequestEventIsSuccessfullyProcessed() throws Exception {
        ReceiveMergeRequestEventForm formAsMock = new ReceiveMergeRequestEventForm(
                new MergeRequestObjectAttributesForm("#260"),
                new GitlabEventProjectForm(1L)
        );
        String formAsJson = this.objectMapper.writeValueAsString(formAsMock);

        BDDMockito.doNothing().when(this.createIssueService).create(formAsMock);

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/webhooks/merge-request")
                .content(formAsJson)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Testing if request returns 400 when merge request event payload is invalid")
    void testIfRequestReturns201WhenMergeRequestEventPayloadIsInvalid() throws Exception {
        ReceiveMergeRequestEventForm formAsMock = BDDMockito.mock(ReceiveMergeRequestEventForm.class);
        String formAsJson = this.objectMapper.writeValueAsString(formAsMock);

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/webhooks/merge-request")
                .content(formAsJson)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Testing if request returns 201 when tag event is successfully processed")
    void testIfRequestReturns201WhenTagEventIsSuccessfullyProcessed() throws Exception {
        ReceiveTagEventForm formAsMock = new ReceiveTagEventForm(
                "tag_push",
                "refs/tags/1.0-ALFA01",
                new GitlabEventProjectForm(1L)
        );
        String formAsJson = this.objectMapper.writeValueAsString(formAsMock);

        BDDMockito.doNothing().when(this.createTagReleaseNotesService).create(formAsMock);

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/webhooks/tag")
                .content(formAsJson)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Testing if request returns 400 when tag event payload is invalid")
    void testIfRequestReturns201WhenTagEventPayloadIsInvalid() throws Exception {
        ReceiveTagEventForm formAsMock = BDDMockito.mock(ReceiveTagEventForm.class);
        String formAsJson = this.objectMapper.writeValueAsString(formAsMock);

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/webhooks/tag")
                .content(formAsJson)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}