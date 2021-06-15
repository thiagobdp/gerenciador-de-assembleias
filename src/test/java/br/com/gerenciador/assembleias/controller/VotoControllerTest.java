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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.gerenciador.assembleias.controller.dto.VotoDto;
import br.com.gerenciador.assembleias.model.Pauta;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class VotoControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
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
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "voto", "pauta");
	}

	@Test
	public void testVotarComSucesso() throws Exception {
		this.abreSessao();
		
		URI uri = new URI("/voto/votar");

		Long ultimaPautaID = this.obtemUltimaPautaID();		
		String json = "{\r\n"
				+ "  \"idPauta\":"+ultimaPautaID+",\r\n"
				+ "  \"cpf\": \"05517584900\",\r\n"
				+ "  \"voto\": \"SIM\"\r\n"
				+ "}";

		MvcResult mvcResultCadastro = mockMvc.perform(MockMvcRequestBuilders//
				.post(uri)//
				.content(json)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isCreated())
				.andReturn();

		String jsonReturnedCadastro = mvcResultCadastro.getResponse().getContentAsString();
		
		Long ultimoVotoID = this.obtemUltimoVotoID();
		String jsonExpectedCadastro = "{\"id\":"+ultimoVotoID+",\"cpf\":\"05517584900\",\"voto\":\"SIM\",\"pautaId\":"+ultimaPautaID+"}";

		JSONAssert.assertEquals(jsonExpectedCadastro, jsonReturnedCadastro, JSONCompareMode.LENIENT);
	}
	
	@Test
	public void testVotarComSucessoCPFiniciaComZero() throws Exception {
		this.abreSessao();
		
		URI uri = new URI("/voto/votar");

		Long ultimaPautaID = this.obtemUltimaPautaID();		
		String json = "{\r\n"
				+ "  \"idPauta\":"+ultimaPautaID+",\r\n"
				+ "  \"cpf\": \"05517584900\",\r\n"
				+ "  \"voto\": \"SIM\"\r\n"
				+ "}";

		MvcResult mvcResultCadastro = mockMvc.perform(MockMvcRequestBuilders//
				.post(uri)//
				.content(json)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isCreated())
				.andReturn();

		String jsonReturnedCadastro = mvcResultCadastro.getResponse().getContentAsString();
		
		Long ultimoVotoID = this.obtemUltimoVotoID();
		String jsonExpectedCadastro = "{\"id\":"+ultimoVotoID+",\"cpf\":\"05517584900\",\"voto\":\"SIM\",\"pautaId\":"+ultimaPautaID+"}";

		JSONAssert.assertEquals(jsonExpectedCadastro, jsonReturnedCadastro, JSONCompareMode.LENIENT);
	}
	
	@Test
	public void testVotarRepetido() throws Exception {
		this.abreSessao();
		
		Long ultimaPautaID = this.obtemUltimaPautaID();
		
		URI uri = new URI("/voto/votar");
		
		String json = "{\r\n"
				+ "  \"idPauta\":"+ultimaPautaID+",\r\n"
				+ "  \"cpf\": \"05517584900\",\r\n"
				+ "  \"voto\": \"SIM\"\r\n"
				+ "}";

		//realiza primeiro voto
		mockMvc.perform(MockMvcRequestBuilders//
				.post(uri)//
				.content(json)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isCreated())
				.andReturn();
		
		//tenta realizar voto repetido		

		mockMvc.perform(MockMvcRequestBuilders//
				.post(uri)//
				.content(json)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isBadRequest())
				.andReturn();
	}
	
	@Test
	public void testVotarPautaInexistente() throws Exception {
		URI uri = new URI("/voto/votar");

		String json = "{\r\n"
				+ "  \"idPauta\":99999,\r\n"
				+ "  \"cpf\": \"05517584900\",\r\n"
				+ "  \"voto\": \"SIM\"\r\n"
				+ "}";

		mockMvc.perform(MockMvcRequestBuilders//
				.post(uri)//
				.content(json)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isNotFound())
				.andReturn();
	}
	
	@Test
	public void testVotarCpfInvalido() throws Exception {		
		URI uri = new URI("/voto/votar");

		String json = "{\r\n"
				+ "  \"idPauta\":1,\r\n"
				+ "  \"cpf\": \"05117584900\",\r\n"
				+ "  \"voto\": \"SIM\"\r\n"
				+ "}";

		mockMvc.perform(MockMvcRequestBuilders//
				.post(uri)//
				.content(json)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isNotFound())
				.andReturn();
	}
	
	@Test
	public void testVotarSessaoAindaNaoAberta() throws Exception {		
		URI uri = new URI("/voto/votar");

		String json = "{\r\n"
				+ "  \"idPauta\":"+this.obtemUltimaPautaID()+",\r\n"
				+ "  \"cpf\": \"05517584900\",\r\n"
				+ "  \"voto\": \"SIM\"\r\n"
				+ "}";

		mockMvc.perform(MockMvcRequestBuilders//
				.post(uri)//
				.content(json)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isBadRequest())
				.andReturn();
	}
	
	@Test
	public void testListar() throws Exception {
		this.vota();
		URI uri = new URI("/voto");

		MvcResult mvcResultCadastro = mockMvc.perform(MockMvcRequestBuilders//
				.get(uri)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isOk())//
				.andReturn();

		JSONArray jsonReturnedCadastro = new JSONArray(mvcResultCadastro.getResponse().getContentAsString());
		JSONArray jsonExpectedCadastro = new JSONArray("[{\"id\":"+this.obtemUltimoVotoID()+",\"cpf\":\"05517584900\",\"voto\":\"SIM\",\"pautaId\":"+this.obtemUltimaPautaID()+"}]");

		Assert.assertTrue(jsonReturnedCadastro.length()>0);
		JSONAssert.assertEquals(jsonExpectedCadastro.get(0).toString(), jsonReturnedCadastro.get(0).toString(), JSONCompareMode.LENIENT);
	}

	
	private void abreSessao() throws Exception {
		String json = "{\r\n" + "  \"duracaoEmHoras\": 0,\r\n" + "  \"duracaoEmMinutos\": 5\r\n" + "}";

		mockMvc.perform(MockMvcRequestBuilders//
				.put("/pauta/{id}/abrirsessao", this.obtemUltimaPautaID())// .
				.content(json)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isOk());
	}

	private Long obtemUltimaPautaID() throws Exception {
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
	
	private Long obtemUltimoVotoID() throws Exception {
		URI uri = new URI("/voto");
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
		objectMapper.registerModule(new JavaTimeModule());
		VotoDto pauta = objectMapper.readValue(jsonReturned.getString(0), VotoDto.class);
		return pauta.getId();
	}
	
	private void vota() throws Exception {
		this.abreSessao();
		
		URI uri = new URI("/voto/votar");

		Long ultimaPautaID = this.obtemUltimaPautaID();		
		String json = "{\r\n"
				+ "  \"idPauta\":"+ultimaPautaID+",\r\n"
				+ "  \"cpf\": \"05517584900\",\r\n"
				+ "  \"voto\": \"SIM\"\r\n"
				+ "}";

		MvcResult mvcResultCadastro = mockMvc.perform(MockMvcRequestBuilders//
				.post(uri)//
				.content(json)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(MockMvcResultMatchers//
						.status()//
						.isCreated())
				.andReturn();

		String jsonReturnedCadastro = mvcResultCadastro.getResponse().getContentAsString();
		
		Long ultimoVotoID = this.obtemUltimoVotoID();
		String jsonExpectedCadastro = "{\"id\":"+ultimoVotoID+",\"cpf\":\"05517584900\",\"voto\":\"SIM\",\"pautaId\":"+ultimaPautaID+"}";

		JSONAssert.assertEquals(jsonExpectedCadastro, jsonReturnedCadastro, JSONCompareMode.LENIENT);
	}

}
