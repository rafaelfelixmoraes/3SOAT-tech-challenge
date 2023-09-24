package br.com.tech.challenge.redis;

import br.com.tech.challenge.domain.dto.TransactionDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRedisRepository extends CrudRepository<TransactionDto, String> {

}
