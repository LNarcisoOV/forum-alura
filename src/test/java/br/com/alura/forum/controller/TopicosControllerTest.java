package br.com.alura.forum.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = TopicosController.class)
public class TopicosControllerTest {

	private static final int TAMANHO_LISTA_DEFAULT = 3;
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	private static final String DUVIDA_2_TITULO = "DUVIDA2";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TopicoRepository topicoRepository;

	@MockBean
	private CursoRepository cursoRepository;

	private List<Topico> listaDefault;

	private Topico topico2;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		topico2 = new Topico("DUVIDA2", "TESTE", new Curso());
		topico2.setId(2L);
		topico2.setAutor(new Usuario());
	}

	@Test
	public void deveRetornarStatusOkAoBuscarLista() throws Exception {
		mockMvc.perform(get("/topicos")).andExpect(status().isOk());
	}

	@Test
	public void deveRetornarListaEVerificarTamanhoDefaultDeRetorno() throws Exception {
		listaDefault = new ArrayList<Topico>();
		listaDefault.add(new Topico());
		listaDefault.add(new Topico());
		listaDefault.add(new Topico());

		when(topicoRepository.findAll()).thenReturn(listaDefault);

		mockMvc.perform(get("/topicos")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(TAMANHO_LISTA_DEFAULT));
	}

	@Test
	public void deveRetornarListaEVerificarValorCampoTituloDoPrimeiroItem() throws Exception {
		listaDefault = new ArrayList<Topico>();
		listaDefault.add(new Topico("DUVIDA", "TESTE", new Curso()));
		listaDefault.add(new Topico("DUVIDA2", "TESTE", new Curso()));
		listaDefault.add(new Topico("DUVIDA3", "TESTE", new Curso()));

		when(topicoRepository.findAll()).thenReturn(listaDefault);

		String response = mockMvc.perform(get("/topicos")).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertTrue(response.contains(DUVIDA_2_TITULO));
	}

	@Test
	public void deveRetornarTopicoPorId() throws Exception {
		Optional<Topico> optTopico2 = Optional.of(topico2);

		when(topicoRepository.findById(2L)).thenReturn(optTopico2);

		String response = mockMvc.perform(get("/topicos/2")).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertTrue(response.contains(DUVIDA_2_TITULO));
	}

	@Test
	public void deveAtualizarTopicoPorId() throws Exception {
		AtualizacaoTopicoForm form = new AtualizacaoTopicoForm();
		form.setMensagem("MENSAGEM PARA ATUALIZACAO");
		form.setTitulo("TITULO PARA ATUALIZACAO");

		Optional<Topico> optTopico2 = Optional.of(topico2);

		when(topicoRepository.findById(2L)).thenReturn(optTopico2);
		when(topicoRepository.getOne(2L)).thenReturn(topico2);

		String response = mockMvc
				.perform(put("/topicos/2").contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(new ObjectMapper().writeValueAsString(form)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		assertFalse(response.contains(DUVIDA_2_TITULO));
		assertTrue(response.contains("TITULO PARA ATUALIZACAO"));
	}

//	@Test
//	public void deveDeletarTopicoPorId() throws Exception {
//		listaDefault = new ArrayList<Topico>();
//		listaDefault.add(new Topico("DUVIDA", "TESTE", new Curso()));
//		listaDefault.add(topico2);
//		listaDefault.add(new Topico("DUVIDA3", "TESTE", new Curso()));
//
//		Optional<Topico> optTopico2 = Optional.of(topico2);
//
//		when(topicoRepository.findById(2L)).thenReturn(optTopico2);
//		
//		mockMvc.perform(delete("/topicos/2")).andExpect(status().isOk());
//
//		when(topicoRepository.findAll()).thenReturn(listaDefault);
//
//		String response = mockMvc.perform(get("/topicos")).andExpect(status().isOk()).andReturn().getResponse()
//				.getContentAsString();
//
//		assertFalse(listaDefault.contains(DUVIDA_2_TITULO));
//	}

}
