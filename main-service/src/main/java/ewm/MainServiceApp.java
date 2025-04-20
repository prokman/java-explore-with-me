package ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import stat.StatsClient;
import statdto.StatDtoRequest;
import statdto.StatDtoResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class MainServiceApp {
    public static void main(String[] args) {
        //SpringApplication.run(MainServiceApp.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(MainServiceApp.class, args);
        StatsClient statsClient = context.getBean(StatsClient.class);
        StatDtoRequest request = new StatDtoRequest("ewm-main-service","/events","121.0.0.1", "2025-04-20 06:43:22");
        statsClient.addHit(request);
        List<StatDtoResponse> dtoResponse = statsClient.getStats(
                "2020-05-05 00:00:00",
                "2035-05-05 00:00:00",
                Optional.of(new ArrayList(Arrays.asList("/events"))),
                Optional.empty());
    }
}
