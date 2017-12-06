package org.jhipster.market.repository;

import org.jhipster.market.domain.StockValue;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the StockValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockValueRepository extends MongoRepository<StockValue, String> {

	StockValue findByCode(String code);

}
