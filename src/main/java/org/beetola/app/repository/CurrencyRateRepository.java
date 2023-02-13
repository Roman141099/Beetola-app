package org.beetola.app.repository;

import org.beetola.app.model.domain.CurrencyRate;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyRateRepository extends CrudRepository<CurrencyRate, String> {
}
