package org.jhipster.portfolio.repository;

import java.util.List;

import org.jhipster.portfolio.domain.Stock;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Stock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

	List<Stock> findByUserId (String userId);
	
}
