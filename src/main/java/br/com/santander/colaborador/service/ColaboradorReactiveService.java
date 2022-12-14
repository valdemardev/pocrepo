package br.com.santander.colaborador.service;

import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import io.smallrye.mutiny.Multi;

@ApplicationScoped
public class ColaboradorReactiveService {

    public Multi<String> greetings(int count, String name) {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .onItem().transform(n -> String.format("Olá %s - %d", name, n))
                .select().first(count);

    }
}
