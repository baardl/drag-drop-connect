package com.bardlind.dragdrop.domain.counter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
public class CounterService {
    private final static Logger log = LoggerFactory.getLogger(CounterService.class);

    private final AtomicLong counter = new AtomicLong(0);

    public long next() {
        log.trace("{}", kv("counter-before", counter.get()));
        long value = counter.incrementAndGet();
        log.trace("{}", kv("counter-after", counter.get()));
        return value;
    }
}
