package org.jhipster.portfolio.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.jhipster.portfolio.security.SecurityUtils;
import org.jhipster.portfolio.service.StockService;
import org.jhipster.portfolio.web.rest.client.MarketStockValueClient;
import org.jhipster.portfolio.web.rest.errors.BadRequestAlertException;
import org.jhipster.portfolio.web.rest.util.HeaderUtil;
import org.jhipster.portfolio.web.rest.vm.StockValueVM;
import org.jhipster.portfolio.service.dto.StockDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Stock.
 */
@RestController
@RequestMapping("/api")
public class StockResource {

    private final Logger log = LoggerFactory.getLogger(StockResource.class);

    private static final String ENTITY_NAME = "stock";

    private final StockService stockService;

    private MarketStockValueClient  marketStockValueClient;

    public StockResource(StockService stockService, MarketStockValueClient  marketStockValueClient ) {
        this.stockService = stockService;
        this.marketStockValueClient = marketStockValueClient;
    }

    /**
     * POST  /stocks : Create a new stock.
     *
     * @param stockDTO the stockDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockDTO, or with status 400 (Bad Request) if the stock has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stocks")
    @Timed
    public ResponseEntity<StockDTO> createStock(@Valid @RequestBody StockDTO stockDTO) throws URISyntaxException {
        log.debug("REST request to save Stock : {}", stockDTO);
        if (stockDTO.getId() != null) {
            throw new BadRequestAlertException("A new stock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        stockDTO.setUserId(SecurityUtils.getCurrentUserLogin());
        
        StockDTO result = stockService.save(stockDTO);
        return ResponseEntity.created(new URI("/api/stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stocks : Updates an existing stock.
     *
     * @param stockDTO the stockDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockDTO,
     * or with status 400 (Bad Request) if the stockDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stocks")
    @Timed
    public ResponseEntity<StockDTO> updateStock(@Valid @RequestBody StockDTO stockDTO) throws URISyntaxException {
        log.debug("REST request to update Stock : {}", stockDTO);
        if (stockDTO.getId() == null) {
            return createStock(stockDTO);
        }
        
        stockDTO.setUserId(SecurityUtils.getCurrentUserLogin());
        StockDTO result = stockService.save(stockDTO);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stocks : get all the stocks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of stocks in body
     */
    @GetMapping("/stocks")
    @Timed
    public List<StockDTO> getAllStocks() {
        log.debug("REST request to get all Stocks");        
        log.info("User:{} - JWT:{} ", SecurityUtils.getCurrentUserLogin(), SecurityUtils.getCurrentUserJWT());
                        
        return stockService.findByUserId(SecurityUtils.getCurrentUserLogin());
        }

    /**
     * GET  /stocks/:id : get the "id" stock.
     *
     * @param id the id of the stockDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stocks/{id}")
    @Timed
    public ResponseEntity<StockDTO> getStock(@PathVariable Long id) {
        log.debug("REST request to get Stock : {}", id);
        
        StockDTO stockDTO = stockService.findOne(id);
        
        StockValueVM stockValueVM = marketStockValueClient.getStockValueByCode("Bearer " + SecurityUtils.getCurrentUserJWT(), stockDTO.getCode());        
        if (stockValueVM != null) {
        	stockDTO.setAmount(stockValueVM.getAmount());
        }
        
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockDTO));
    }

    /**
     * DELETE  /stocks/:id : delete the "id" stock.
     *
     * @param id the id of the stockDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stocks/{id}")
    @Timed
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        log.debug("REST request to delete Stock : {}", id);
        stockService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
