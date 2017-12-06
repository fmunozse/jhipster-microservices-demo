package org.jhipster.market.web.rest;

import org.jhipster.market.MarketApp;

import org.jhipster.market.domain.StockValue;
import org.jhipster.market.repository.StockValueRepository;
import org.jhipster.market.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StockValueResource REST controller.
 *
 * @see StockValueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MarketApp.class)
public class StockValueResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    @Autowired
    private StockValueRepository stockValueRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restStockValueMockMvc;

    private StockValue stockValue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockValueResource stockValueResource = new StockValueResource(stockValueRepository);
        this.restStockValueMockMvc = MockMvcBuilders.standaloneSetup(stockValueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockValue createEntity() {
        StockValue stockValue = new StockValue()
            .code(DEFAULT_CODE)
            .amount(DEFAULT_AMOUNT);
        return stockValue;
    }

    @Before
    public void initTest() {
        stockValueRepository.deleteAll();
        stockValue = createEntity();
    }

    @Test
    public void createStockValue() throws Exception {
        int databaseSizeBeforeCreate = stockValueRepository.findAll().size();

        // Create the StockValue
        restStockValueMockMvc.perform(post("/api/stock-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockValue)))
            .andExpect(status().isCreated());

        // Validate the StockValue in the database
        List<StockValue> stockValueList = stockValueRepository.findAll();
        assertThat(stockValueList).hasSize(databaseSizeBeforeCreate + 1);
        StockValue testStockValue = stockValueList.get(stockValueList.size() - 1);
        assertThat(testStockValue.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testStockValue.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    public void createStockValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockValueRepository.findAll().size();

        // Create the StockValue with an existing ID
        stockValue.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockValueMockMvc.perform(post("/api/stock-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockValue)))
            .andExpect(status().isBadRequest());

        // Validate the StockValue in the database
        List<StockValue> stockValueList = stockValueRepository.findAll();
        assertThat(stockValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockValueRepository.findAll().size();
        // set the field null
        stockValue.setCode(null);

        // Create the StockValue, which fails.

        restStockValueMockMvc.perform(post("/api/stock-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockValue)))
            .andExpect(status().isBadRequest());

        List<StockValue> stockValueList = stockValueRepository.findAll();
        assertThat(stockValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockValueRepository.findAll().size();
        // set the field null
        stockValue.setAmount(null);

        // Create the StockValue, which fails.

        restStockValueMockMvc.perform(post("/api/stock-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockValue)))
            .andExpect(status().isBadRequest());

        List<StockValue> stockValueList = stockValueRepository.findAll();
        assertThat(stockValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllStockValues() throws Exception {
        // Initialize the database
        stockValueRepository.save(stockValue);

        // Get all the stockValueList
        restStockValueMockMvc.perform(get("/api/stock-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockValue.getId())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)));
    }

    @Test
    public void getStockValue() throws Exception {
        // Initialize the database
        stockValueRepository.save(stockValue);

        // Get the stockValue
        restStockValueMockMvc.perform(get("/api/stock-values/{id}", stockValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockValue.getId()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT));
    }

    @Test
    public void getNonExistingStockValue() throws Exception {
        // Get the stockValue
        restStockValueMockMvc.perform(get("/api/stock-values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateStockValue() throws Exception {
        // Initialize the database
        stockValueRepository.save(stockValue);
        int databaseSizeBeforeUpdate = stockValueRepository.findAll().size();

        // Update the stockValue
        StockValue updatedStockValue = stockValueRepository.findOne(stockValue.getId());
        updatedStockValue
            .code(UPDATED_CODE)
            .amount(UPDATED_AMOUNT);

        restStockValueMockMvc.perform(put("/api/stock-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStockValue)))
            .andExpect(status().isOk());

        // Validate the StockValue in the database
        List<StockValue> stockValueList = stockValueRepository.findAll();
        assertThat(stockValueList).hasSize(databaseSizeBeforeUpdate);
        StockValue testStockValue = stockValueList.get(stockValueList.size() - 1);
        assertThat(testStockValue.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStockValue.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    public void updateNonExistingStockValue() throws Exception {
        int databaseSizeBeforeUpdate = stockValueRepository.findAll().size();

        // Create the StockValue

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockValueMockMvc.perform(put("/api/stock-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockValue)))
            .andExpect(status().isCreated());

        // Validate the StockValue in the database
        List<StockValue> stockValueList = stockValueRepository.findAll();
        assertThat(stockValueList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteStockValue() throws Exception {
        // Initialize the database
        stockValueRepository.save(stockValue);
        int databaseSizeBeforeDelete = stockValueRepository.findAll().size();

        // Get the stockValue
        restStockValueMockMvc.perform(delete("/api/stock-values/{id}", stockValue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockValue> stockValueList = stockValueRepository.findAll();
        assertThat(stockValueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockValue.class);
        StockValue stockValue1 = new StockValue();
        stockValue1.setId("id1");
        StockValue stockValue2 = new StockValue();
        stockValue2.setId(stockValue1.getId());
        assertThat(stockValue1).isEqualTo(stockValue2);
        stockValue2.setId("id2");
        assertThat(stockValue1).isNotEqualTo(stockValue2);
        stockValue1.setId(null);
        assertThat(stockValue1).isNotEqualTo(stockValue2);
    }
}
