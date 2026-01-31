package br.com.tavares.repo;

import br.com.tavares.domain.BlockedTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface BlockedTimeRepository extends JpaRepository<BlockedTime, Long> {
  @Query("select b from BlockedTime b where b.startAt < :to and b.endAt > :from")
  List<BlockedTime> findOverlapping(ZonedDateTime from, ZonedDateTime to);
}
