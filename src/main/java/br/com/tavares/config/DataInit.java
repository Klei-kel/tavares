package br.com.tavares.config;

import br.com.tavares.domain.ServiceEntity;
import br.com.tavares.repo.ServiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInit {

  @Bean
  CommandLineRunner seed(ServiceRepository repo) {
    return args -> {
      if (repo.count() > 0) return;

      repo.save(new ServiceEntity("Carro pequeno (lavagem)", new BigDecimal("35.00")));
      repo.save(new ServiceEntity("Carro pequeno com cera", new BigDecimal("40.00")));
      repo.save(new ServiceEntity("Carro grande (lavagem)", new BigDecimal("50.00")));
      repo.save(new ServiceEntity("Carro grande com cera", new BigDecimal("60.00")));
      repo.save(new ServiceEntity("Moto (lavagem)", new BigDecimal("30.00")));
      repo.save(new ServiceEntity("Lavagem completa (motor e por baixo) - pequeno", new BigDecimal("130.00")));
      repo.save(new ServiceEntity("Lavagem completa (motor e por baixo) - grande", new BigDecimal("150.00")));
      repo.save(new ServiceEntity("Mensal carro pequeno (4 lavagens/mês)", new BigDecimal("140.00")));
      repo.save(new ServiceEntity("Mensal carro grande (4 lavagens/mês)", new BigDecimal("180.00")));
    };
  }
}
