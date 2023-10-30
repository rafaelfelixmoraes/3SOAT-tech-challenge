package br.com.tech.challenge.api.client.factory;

import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

public class RetryTemplateFactory {

    public static RetryTemplate retryTemplate() {
        final long TIME_BETWEEN_REQUESTS_IN_MILLIS = 2000L;
        final int MAX_ATTEMPTS = 3;

        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(TIME_BETWEEN_REQUESTS_IN_MILLIS);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(MAX_ATTEMPTS);
        retryTemplate.setRetryPolicy(simpleRetryPolicy);

        return retryTemplate;
    }

}
