package org.jhipster.portfolio.web.rest.client;


import org.jhipster.portfolio.web.rest.vm.StockValueVM;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "market", fallback = MarketStockValueClientFallback.class)
public interface MarketStockValueClient {

	
    @GetMapping("/api/stock-values-by-code/{code}")
    StockValueVM getStockValueByCode(
    		@RequestHeader("Authorization") String authorizationToken,
    		@PathVariable("code") String code);

}
