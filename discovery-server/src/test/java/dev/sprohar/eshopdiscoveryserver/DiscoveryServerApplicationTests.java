package dev.sprohar.eshopdiscoveryserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DiscoveryServerApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	@WithAnonymousUser
	void shouldReturnStatusCodeUnauthorizedWhenNoCredentialsAreGiven() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(status().isUnauthorized());
	}

	/**
	 * The <code>WithUserDetails</code> annotation uses "password" and "USER"
	 * as the default values for <code>password</code> and <code>role</code>,
	 * respectively.
	 * <br /><br />
	 * The value for <code>value</code> is the username that you defined in
	 * <code>application.settings</code>
	 */
	@Test
//	@WithMockUser(username = "eureka")
	@WithUserDetails(value = "eureka")
	void shouldReturnStatusCodeOkWhenValidCredentialsAreGiven() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(status().isOk());
	}
}
