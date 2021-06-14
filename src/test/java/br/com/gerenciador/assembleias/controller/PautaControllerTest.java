package br.com.gerenciador.assembleias.controller;

import java.net.URI;

import org.json.JSONArray;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
class PautaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Order(1)
	@Test
	public void testCadastrar() throws Exception {
		URI uri = new URI("/pauta");

		String json = "{\r\n" + "  \"descricao\": \"Sera votado se a linguagem sera Java.\",\r\n"
				+ "  \"titulo\": \"Definir a linguagem padrao para o sistema\"\r\n" + "}";

		MvcResult mvcResultCadastro = mockMvc.perform(MockMvcRequestBuilders//
				.post(uri)//
				.content(json)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isCreated())
				.andReturn();

		String jsonReturnedCadastro = mvcResultCadastro.getResponse().getContentAsString();
		String jsonExpectedCadastro = "{\"titulo\":\"Definir a linguagem padrao para o sistema\",\"descricao\":\"Sera votado se a linguagem sera Java.\"}";

		JSONAssert.assertEquals(jsonExpectedCadastro, jsonReturnedCadastro, JSONCompareMode.LENIENT);
	}

	@Order(2)
	@Test
	public void testListar() throws Exception {
		URI uri = new URI("/pauta");
		MvcResult mvcResultLista = mockMvc.perform(MockMvcRequestBuilders//
				.get(uri)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isOk())
				.andReturn();

		JSONArray jsonReturned = new JSONArray(mvcResultLista.getResponse().getContentAsString());
		JSONArray jsonExpected = new JSONArray(
				"[{\"titulo\":\"Definir a linguagem padrao para o sistema\",\"descricao\":\"Sera votado se a linguagem sera Java.\"}]");

		JSONAssert.assertEquals(jsonExpected, jsonReturned, JSONCompareMode.LENIENT);

	}

	@Order(2)
	@Test
	public void testDetalhes() throws Exception {
		MvcResult mvcResultLista = mockMvc.perform(MockMvcRequestBuilders//
				.get("/pauta/{id}", 1)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isOk())
				.andReturn();

		String jsonReturned = mvcResultLista.getResponse().getContentAsString();
		String jsonExpected = "{\"id\":1,\"titulo\":\"Definir a linguagem padrao para o sistema\",\"descricao\":\"Sera votado se a linguagem sera Java.\",\"inicioSessao\":null,\"fimSessao\":null,\"qtdVotosSim\":0,\"qtdVotosNao\":0,\"sessaoFechada\":false}";

		JSONAssert.assertEquals(jsonExpected, jsonReturned, JSONCompareMode.LENIENT);
	}

	@Order(2)
	@Test
	public void testDetalhesPautaInexistente() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders//
				.get("/pauta/{id}", 2)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isNotFound());
	}

	@Order(3)
	@Test
	public void testAbrirSessao() throws Exception {
		String json = "{\r\n" + "  \"duracaoEmHoras\": 0,\r\n" + "  \"duracaoEmMinutos\": 5\r\n" + "}";

		mockMvc.perform(MockMvcRequestBuilders//
				.put("/pauta/{id}/abrirsessao", "1")// .
				.content(json)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isOk());
	}

	@Order(3)
	@Test
	public void testAbrirSessaoInexistente() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders//
				.put("/pauta/{id}/abrirsessao", "2")// .
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isNotFound());
	}

	@Order(4)
	@Test
	public void testAbrirSessaoJaAberta() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders//
				.put("/pauta/{id}/abrirsessao", "1")//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isBadRequest());
	}
}
