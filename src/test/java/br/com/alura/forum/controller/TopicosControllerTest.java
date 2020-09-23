package br.com.alura.forum.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TopicosControllerTest {

	
	
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

}
