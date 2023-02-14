package org.beetola.app.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRate {

    @Id
    private String id;
    @Column(nullable = false, precision = 20, scale = 4)
    private BigDecimal coefficient;
}
