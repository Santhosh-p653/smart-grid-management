package com.smartgrid.util;

import com.smartgrid.entity.*;
import com.smartgrid.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private GridNodeRepository gridNodeRepository;

    @Autowired
    private PowerReadingRepository powerReadingRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private MeterReadingRepository meterReadingRepository;

    @Autowired
    private FaultRepository faultRepository;

    @Autowired
    private OutageRepository outageRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Initialize Roles
        if (roleRepository.count() == 0) {
            Role adminRole = Role.builder().name(RoleName.ROLE_ADMIN).build();
            Role operatorRole = Role.builder().name(RoleName.ROLE_OPERATOR).build();
            roleRepository.saveAll(List.of(adminRole, operatorRole));
        }

        // 2. Initialize Users
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not initialized"));
            Role operatorRole = roleRepository.findByName(RoleName.ROLE_OPERATOR)
                    .orElseThrow(() -> new RuntimeException("Operator role not initialized"));

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@smartgrid.com")
                    .fullName("Chief Administrator")
                    .role(adminRole)
                    .build();

            User operator1 = User.builder()
                    .username("operator1")
                    .password(passwordEncoder.encode("operator123"))
                    .email("op1@smartgrid.com")
                    .fullName("John Grid Operator")
                    .role(operatorRole)
                    .build();

            User operator2 = User.builder()
                    .username("operator2")
                    .password(passwordEncoder.encode("operator123"))
                    .email("op2@smartgrid.com")
                    .fullName("Alice Node Manager")
                    .role(operatorRole)
                    .build();

            userRepository.saveAll(List.of(admin, operator1, operator2));
        }

        // 3. Initialize Zones
        if (zoneRepository.count() == 0) {
            Zone northZone = Zone.builder().name("North Zone").region("Metro North Region").description("Residential and light commercial zone").build();
            Zone southZone = Zone.builder().name("South Zone").region("Industrial Park South").description("Heavy industrial load distribution zone").build();
            Zone centralZone = Zone.builder().name("Central Zone").region("Downtown Core").description("High density business district").build();

            zoneRepository.saveAll(List.of(northZone, southZone, centralZone));
        }

        // 4. Initialize Grid Nodes
        if (gridNodeRepository.count() == 0) {
            List<Zone> zones = zoneRepository.findAll();
            Zone north = zones.stream().filter(z -> z.getName().equals("North Zone")).findFirst().get();
            Zone south = zones.stream().filter(z -> z.getName().equals("South Zone")).findFirst().get();

            GridNode transformerN1 = GridNode.builder()
                    .name("North Substation T1")
                    .type("Substation")
                    .capacity(50.0) // MW
                    .status(NodeStatus.ACTIVE)
                    .zone(north)
                    .build();

            GridNode transformerN2 = GridNode.builder()
                    .name("North Transformer A")
                    .type("Transformer")
                    .capacity(10.0) // MW
                    .status(NodeStatus.ACTIVE)
                    .zone(north)
                    .build();

            GridNode substationS1 = GridNode.builder()
                    .name("South Industrial Substation")
                    .type("Substation")
                    .capacity(150.0) // MW
                    .status(NodeStatus.ACTIVE)
                    .zone(south)
                    .build();

            GridNode distributionBoxS2 = GridNode.builder()
                    .name("South Distribution Link")
                    .type("Distribution Box")
                    .capacity(25.0) // MW
                    .status(NodeStatus.UNDER_MAINTENANCE)
                    .zone(south)
                    .build();

            gridNodeRepository.saveAll(List.of(transformerN1, transformerN2, substationS1, distributionBoxS2));
        }

        // 5. Initialize Power Readings
        if (powerReadingRepository.count() == 0) {
            List<GridNode> nodes = gridNodeRepository.findAll();
            for (GridNode node : nodes) {
                // Add simulated normal load readings
                PowerReading reading = PowerReading.builder()
                        .gridNode(node)
                        .voltage(node.getStatus() == NodeStatus.ACTIVE ? 228.4 : 0.0)
                        .current(node.getStatus() == NodeStatus.ACTIVE ? (node.getCapacity() * 4.3) : 0.0)
                        .frequency(node.getStatus() == NodeStatus.ACTIVE ? 50.02 : 0.0)
                        .powerFactor(node.getStatus() == NodeStatus.ACTIVE ? 0.96 : 0.0)
                        .activeLoad(node.getStatus() == NodeStatus.ACTIVE ? (node.getCapacity() * 0.65) : 0.0)
                        .healthStatus(node.getStatus() == NodeStatus.ACTIVE ? "NORMAL" : "CRITICAL")
                        .timestamp(LocalDateTime.now().minusMinutes(5))
                        .build();

                powerReadingRepository.save(reading);
            }
        }

        // 6. Initialize Consumers
        if (consumerRepository.count() == 0) {
            Consumer c1 = Consumer.builder()
                    .name("Global Steel Works")
                    .email("energy@globalsteel.com")
                    .phone("+1-555-0199")
                    .address("Plot 12, Industrial Sector, South District")
                    .contractCapacity(2500.0) // kW
                    .build();

            Consumer c2 = Consumer.builder()
                    .name("Downtown Mall Complex")
                    .email("facilities@downtownmall.com")
                    .phone("+1-555-0145")
                    .address("450 Broadway St, Central District")
                    .contractCapacity(800.0) // kW
                    .build();

            Consumer c3 = Consumer.builder()
                    .name("Robert Johnson (Residential)")
                    .email("robert.j@gmail.com")
                    .phone("+1-555-0122")
                    .address("104 Maple Ave, North District")
                    .contractCapacity(15.0) // kW
                    .build();

            consumerRepository.saveAll(List.of(c1, c2, c3));
        }

        // 7. Initialize Meter Readings
        if (meterReadingRepository.count() == 0) {
            List<Consumer> consumers = consumerRepository.findAll();
            for (Consumer consumer : consumers) {
                MeterReading reading1 = MeterReading.builder()
                        .consumer(consumer)
                        .readingDate(LocalDateTime.now().minusMonths(1))
                        .activePower(consumer.getContractCapacity() * 180) // simulated monthly consumption in kWh
                        .reactivePower(consumer.getContractCapacity() * 40)
                        .billingAmount(consumer.getContractCapacity() * 180 * 0.15)
                        .status("BILLED")
                        .build();

                MeterReading reading2 = MeterReading.builder()
                        .consumer(consumer)
                        .readingDate(LocalDateTime.now().minusMinutes(1))
                        .activePower(consumer.getContractCapacity() * 190)
                        .reactivePower(consumer.getContractCapacity() * 45)
                        .billingAmount(consumer.getContractCapacity() * 190 * 0.15)
                        .status("UNBILLED")
                        .build();

                meterReadingRepository.saveAll(List.of(reading1, reading2));
            }
        }

        // 8. Initialize Faults
        if (faultRepository.count() == 0) {
            List<GridNode> nodes = gridNodeRepository.findAll();
            GridNode nodeForFault = nodes.stream().filter(n -> n.getName().contains("South Distribution")).findFirst().orElse(nodes.get(0));

            Fault fault1 = Fault.builder()
                    .gridNode(nodeForFault)
                    .title("Insulator Flashover")
                    .description("Insulator breakdown on overhead distribution lines due to humidity")
                    .severity(FaultSeverity.HIGH)
                    .status(FaultStatus.ACTIVE)
                    .reportedAt(LocalDateTime.now().minusHours(2))
                    .build();

            GridNode normalNode = nodes.stream().filter(n -> n.getStatus() == NodeStatus.ACTIVE).findFirst().get();
            Fault fault2 = Fault.builder()
                    .gridNode(normalNode)
                    .title("Minor Voltage Flicker")
                    .description("Brief transient voltage flicker resolved by automatic recloser")
                    .severity(FaultSeverity.LOW)
                    .status(FaultStatus.RESOLVED)
                    .reportedAt(LocalDateTime.now().minusDays(3))
                    .resolvedAt(LocalDateTime.now().minusDays(3).plusMinutes(12))
                    .build();

            faultRepository.saveAll(List.of(fault1, fault2));
        }

        // 9. Initialize Outages
        if (outageRepository.count() == 0) {
            List<GridNode> nodes = gridNodeRepository.findAll();
            GridNode nodeForOutage = nodes.stream().filter(n -> n.getStatus() == NodeStatus.UNDER_MAINTENANCE || n.getStatus() == NodeStatus.INACTIVE).findFirst().orElse(nodes.get(0));

            Outage outage1 = Outage.builder()
                    .gridNode(nodeForOutage)
                    .startTime(LocalDateTime.now().minusHours(1))
                    .status("ACTIVE")
                    .description("Precautionary outage for breaker panel repair")
                    .build();

            GridNode activeNode = nodes.stream().filter(n -> n.getStatus() == NodeStatus.ACTIVE).findFirst().get();
            Outage outage2 = Outage.builder()
                    .gridNode(activeNode)
                    .startTime(LocalDateTime.now().minusDays(4))
                    .endTime(LocalDateTime.now().minusDays(4).plusHours(2))
                    .status("RESTORED")
                    .description("Short circuit branch contact outage resolved by grid crew")
                    .build();

            outageRepository.saveAll(List.of(outage1, outage2));
        }

        // 10. Initialize Alerts
        if (alertRepository.count() == 0) {
            Alert alert1 = Alert.builder()
                    .title("Warning: High Humidity Fault")
                    .message("South Distribution Link is reporting high insulation breakdown probability.")
                    .severity("WARNING")
                    .status("UNREAD")
                    .createdAt(LocalDateTime.now().minusHours(2))
                    .build();

            alertRepository.save(alert1);
        }
    }
}
