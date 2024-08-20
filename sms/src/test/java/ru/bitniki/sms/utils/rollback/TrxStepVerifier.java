package ru.bitniki.sms.utils.rollback;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.test.StepVerifier;

@Component
public class TrxStepVerifier {

    private final ReactiveTransactionManager reactiveTransactionManager;

    @Autowired
    public TrxStepVerifier(ReactiveTransactionManager transactionManager) {
        this.reactiveTransactionManager = transactionManager;
    }

    public <T> StepVerifier.FirstStep<T> create(Publisher<? extends T> publisher) {
        return StepVerifier.create(
                TransactionalOperator.create(reactiveTransactionManager)
                        .execute(trx -> {
                            trx.setRollbackOnly();
                            return publisher;
                        })
        );
    }
}
