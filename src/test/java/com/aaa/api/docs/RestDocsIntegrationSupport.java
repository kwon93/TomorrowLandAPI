package com.aaa.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.tomorrow.com", uriPort = 443)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public abstract class RestDocsIntegrationSupport {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

}
