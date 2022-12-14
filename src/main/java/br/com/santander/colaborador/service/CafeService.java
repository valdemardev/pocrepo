package br.com.santander.colaborador.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.jboss.logging.Logger;
import br.com.santander.colaborador.entity.Cafe;

@ApplicationScoped
public class CafeService {

	private static final Logger LOGGER = Logger.getLogger(CafeService.class);

    private Map<Integer, Cafe> coffeeList = new TreeMap<>();

    private Map<Integer, Integer> availability = new TreeMap<>();

    private AtomicLong counter = new AtomicLong(0);

    public CafeService() {
        coffeeList.put(1, new Cafe(1, "Catarino", "Centro", 23));
        coffeeList.put(2, new Cafe(2, "Cafes Pagliaroni ", "Centro", 18));
        coffeeList.put(3, new Cafe(3, "Jardim Cafeteria", "Cidade Jardim", 20));

        availability.put(1, 20);
        availability.put(2, 7);
        availability.put(3, 40);
    }

    public List<Cafe> getAllCoffees() {
        return new ArrayList<>(coffeeList.values());
    }

    public Cafe getCoffeeById(Integer id) {
        return coffeeList.get(id);
    }

    public List<Cafe> getRecommendations(Integer id) {
        if (id == null) {
            return Collections.emptyList();
        }
        return coffeeList.values().stream()
                .filter(coffee -> !id.equals(coffee.id))
                .limit(2)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Integer getAvailability(Cafe coffee) {
        maybeFail();
        return availability.get(coffee.id);
    }

    private void maybeFail() {
        final Long invocationNumber = counter.getAndIncrement();
        if (invocationNumber % 4 > 1) {
            LOGGER.errorf("Invocation #%d failing", invocationNumber);
            throw new RuntimeException("Service failed.");
        }
        LOGGER.infof("Invocation #%d OK", invocationNumber);
    }
}
