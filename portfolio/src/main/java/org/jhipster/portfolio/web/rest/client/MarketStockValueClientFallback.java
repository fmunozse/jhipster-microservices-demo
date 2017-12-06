package org.jhipster.portfolio.web.rest.client;

import org.jhipster.portfolio.web.rest.StockResource;
import org.jhipster.portfolio.web.rest.vm.StockValueVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MarketStockValueClientFallback implements MarketStockValueClient{

    private final Logger log = LoggerFactory.getLogger(MarketStockValueClientFallback.class);
    
	@Override
	public StockValueVM getStockValueByCode(String authorizationToken, String code) {	
		log.debug("fallback - getStockValueByCode {} ", code);
		
		StockValueVM o = new StockValueVM();
		o.setAmount(0);
		return o;
	}


}
