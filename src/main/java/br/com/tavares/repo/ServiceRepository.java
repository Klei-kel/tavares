package br.com.tavares.repo;

import br.com.tavares.domain.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
  List<ServiceEntity> findByActiveTrueOrderByNameAsc();
}
