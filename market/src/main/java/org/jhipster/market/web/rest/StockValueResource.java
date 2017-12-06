package org.jhipster.market.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.jhipster.market.domain.StockValue;

import org.jhipster.market.repository.StockValueRepository;
import org.jhipster.market.security.SecurityUtils;
import org.jhipster.market.web.rest.errors.BadRequestAlertException;
import org.jhipster.market.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing StockValue.
 */
@RestController
@RequestMapping("/api")
public class StockValueResource {

    private final Logger log = LoggerFactory.getLogger(StockValueResource.class);

    private static final String ENTITY_NAME = "stockValue";

    private final StockValueRepository stockValueRepository;

    public StockValueResource(StockValueRepository stockValueRepository) {
        this.stockValueRepository = stockValueRepository;
    }

    /**
     * POST  /stock-values : Create a new stockValue.
     *
     * @param stockValue the stockValue to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockValue, or with status 400 (Bad Request) if the stockValue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-values")
    @Timed
    public ResponseEntity<StockValue> createStockValue(@Valid @RequestBody StockValue stockValue) throws URISyntaxException {
        log.debug("REST request to save StockValue : {}", stockValue);
        if (stockValue.getId() != null) {
            throw new BadRequestAlertException("A new stockValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockValue result = stockValueRepository.save(stockValue);
        return ResponseEntity.created(new URI("/api/stock-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-values : Updates an existing stockValue.
     *
     * @param stockValue the stockValue to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockValue,
     * or with status 400 (Bad Request) if the stockValue is not valid,
     * or with status 500 (Internal Server Error) if the stockValue couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-values")
    @Timed
    public ResponseEntity<StockValue> updateStockValue(@Valid @RequestBody StockValue stockValue) throws URISyntaxException {
        log.debug("REST request to update StockValue : {}", stockValue);
        if (stockValue.getId() == null) {
            return createStockValue(stockValue);
        }
        StockValue result = stockValueRepository.save(stockValue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockValue.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-values : get all the stockValues.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of stockValues in body
     */
    @GetMapping("/stock-values")
    @Timed
    public List<StockValue> getAllStockValues() {
        log.debug("REST request to get all StockValues");
        
        log.info("User:{} - JWT: {} ", SecurityUtils.getCurrentUserLogin(), SecurityUtils.getCurrentUserJWT());
        
        return stockValueRepository.findAll();
        }

    
    /**
     * GET  /stock-values-by-code : get all the stockValues.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of stockValues in body
     */
    @GetMapping("/stock-values-by-code/{code}")
    @Timed
    public ResponseEntity<StockValue> getAllStockValues(@PathVariable String code) {
        log.debug("REST StockValue by code {} ", code);
        
        log.info("User:{} - JWT: {} ", SecurityUtils.getCurrentUserLogin(), SecurityUtils.getCurrentUserJWT());
        
        StockValue stockValue =  stockValueRepository.findByCode (code);
        
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockValue));
    }



    
    /**
     * GET  /stock-values/:id : get the "id" stockValue.
     *
     * @param id the id of the stockValue to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockValue, or with status 404 (Not Found)
     */
    @GetMapping("/stock-values/{id}")
    @Timed
    public ResponseEntity<StockValue> getStockValue(@PathVariable String id) {
        log.debug("REST request to get StockValue : {}", id);
        StockValue stockValue = stockValueRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockValue));
    }

    /**
     * DELETE  /stock-values/:id : delete the "id" stockValue.
     *
     * @param id the id of the stockValue to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-values/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockValue(@PathVariable String id) {
        log.debug("REST request to delete StockValue : {}", id);
        stockValueRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
