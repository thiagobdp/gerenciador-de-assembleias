package br.com.gerenciador.assembleias.controller;

import java.net.URI;

import org.json.JSONArray;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.gerenciador.assembleias.model.Pauta;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PautaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Cadastra uma nova Pauta antes de cada teste a ser executado
	 * @throws Exception
	 */
	@Before
	public void cadastraPauta() throws Exception {
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

	/**
	 * Limpa os dados das tabelas após a execução de cada teste
	 */
	@After
	public void limpaBD() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "pauta", "voto");
	}
	
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

	@Test
	public void testDetalhesPauta() throws Exception {
		Long ultimoIDpauta = this.obtemUltimoID();
		MvcResult mvcResultLista = mockMvc.perform(MockMvcRequestBuilders//

				.get("/pauta/{id}", ultimoIDpauta)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isOk())
				.andReturn();

		String jsonReturned = mvcResultLista.getResponse().getContentAsString();
		String jsonExpected = "{\"id\":"+ultimoIDpauta+",\"titulo\":\"Definir a linguagem padrao para o sistema\",\"descricao\":\"Sera votado se a linguagem sera Java.\",\"inicioSessao\":null,\"fimSessao\":null,\"qtdVotosSim\":0,\"qtdVotosNao\":0,\"sessaoFechada\":false}";

		JSONAssert.assertEquals(jsonExpected, jsonReturned, JSONCompareMode.LENIENT);
	}

	@Test
	public void testDetalhesPautaInexistente() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders//
				.get("/pauta/{id}", 9999999)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isNotFound());
	}

	@Test
	public void testAbrirSessaoComSucesso() throws Exception {
		String json = "{\r\n" + "  \"duracaoEmHoras\": 0,\r\n" + "  \"duracaoEmMinutos\": 5\r\n" + "}";

		Long ultimoID = this.obtemUltimoID();

		mockMvc.perform(MockMvcRequestBuilders//
				.put("/pauta/{id}/abrirsessao", ultimoID)// .
				.content(json)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isOk());
	}

	@Test
	public void testAbrirSessaoInexistente() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders//
				.put("/pauta/{id}/abrirsessao", "9999")// .
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isNotFound());
	}

	@Test
	public void testAbrirSessaoJaAberta() throws Exception {
		Long ultimoIDpauta = this.obtemUltimoID();
		// abre a sessao
		String json = "{\r\n" + "  \"duracaoEmHoras\": 0,\r\n" + "  \"duracaoEmMinutos\": 5\r\n" + "}";
		mockMvc.perform(MockMvcRequestBuilders//
				.put("/pauta/{id}/abrirsessao", ultimoIDpauta)// .
				.content(json)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isOk());

		// tenta abrir novamente
		mockMvc.perform(MockMvcRequestBuilders//
				.put("/pauta/{id}/abrirsessao", ultimoIDpauta)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isBadRequest());
	}

	private Long obtemUltimoID() throws Exception {
		URI uri = new URI("/pauta");
		MvcResult mvcResultLista = mockMvc.perform(MockMvcRequestBuilders//
				.get(uri)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isOk())
				.andReturn();

		JSONArray jsonReturned = new JSONArray(mvcResultLista.getResponse().getContentAsString());
		Assert.assertTrue(jsonReturned.length() == 1);

		ObjectMapper objectMapper = new ObjectMapper();
		Pauta pauta = objectMapper.readValue(jsonReturned.getString(0), Pauta.class);
		return pauta.getId();
	}
}
