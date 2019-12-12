package org.rug.wacc2019smartenergy.powerinfrastructureservice.repositories;

import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.Appliance;
import org.rug.wacc2019smartenergy.powerinfrastructureservice.model.ApplianceKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestExecutionListeners(value = {CassandraUnitTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@ActiveProfiles("test")
@EmbeddedCassandra
@CassandraDataSet("createkeyspace.cql")
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ApplianceRepositoryIntegrationTest {

    //@BeforeAll
    //public static void waitForDB() throws InterruptedException { Thread.sleep(20000); }

    @Autowired
    private ApplianceRepository applianceRepository;

    @Test
    public void findByKeyHouseholdId() {
        // Given
        Appliance applianceToAdd = new Appliance(
                new ApplianceKey(
                        UUID.fromString("123e4567-e89b-12d3-a456-426655440000"),
                        UUID.fromString("123e4567-e89b-12d3-a456-426655440001")
                ),
                "Fridge",
                30,
                30,
                true
        );
        applianceRepository.save(applianceToAdd);

        // When
        Flux<Appliance> applianceMonoFetched = applianceRepository.findByKeyHouseholdId(UUID.fromString("123e4567-e89b-12d3-a456-426655440001"));

        // Then
        applianceMonoFetched.subscribe(appliance -> {
            assertEquals(appliance.getKey().getHouseholdId(), applianceToAdd.getKey().getHouseholdId());
        });
    }

    @Test
    public void findByKeyHouseholdIdAndKeyId() {
        // Given
        Appliance applianceToAdd = new Appliance(
                new ApplianceKey(
                        UUID.fromString("123e4567-e89b-12d3-a456-426655440000"),
                        UUID.fromString("123e4567-e89b-12d3-a456-426655440002")
                ),
                "Fridge",
                30,
                30,
                true
        );
        applianceRepository.save(applianceToAdd);

        // When
        Mono<Appliance> applianceMonoFetched = applianceRepository.findByKeyHouseholdIdAndKeyId(
                    UUID.fromString("123e4567-e89b-12d3-a456-426655440000"),
                    UUID.fromString("123e4567-e89b-12d3-a456-426655440002")
                );

        // Then
        applianceMonoFetched.subscribe(appliance -> {
            assertEquals(appliance.getKey().getId(), applianceToAdd.getKey().getId());
        });
    }
}