package br.com.tech.challenge.servicos;

import br.com.tech.challenge.redis.TransactionRedisRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private TransactionRedisRepository transactionRedisRepository;

    public TransactionService(TransactionRedisRepository transactionRedisRepository) {
        this.transactionRedisRepository = transactionRedisRepository;
    }

    /// minuto 18 - aula 3


}
