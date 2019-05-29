package gov.hhs.cms.desy.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
/**
 * Test class for the RequestStatusResource REST controller.
 *
 * @see RequestStatusResource
 */
public class RequestResourceTest {

    private MockMvc restMockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

		RequestResource requestStatusResource = new RequestResource();
        restMockMvc = MockMvcBuilders
            .standaloneSetup(requestStatusResource)
            .build();
    }

	// FIXME
    /**
    * Test getRequestStatus
    */
    @Test
    public void testGetRequestStatus() throws Exception {
		restMockMvc.perform(get("/api/get-request"))
				.andExpect(status().is4xxClientError());
    }

}
