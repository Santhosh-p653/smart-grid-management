package com.smartgrid.service;

import com.smartgrid.dto.ConsumerDto;
import com.smartgrid.dto.MeterReadingDto;
import com.smartgrid.entity.Consumer;
import com.smartgrid.entity.MeterReading;
import com.smartgrid.exception.BadRequestException;
import com.smartgrid.exception.ResourceNotFoundException;
import com.smartgrid.repository.ConsumerRepository;
import com.smartgrid.repository.MeterReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsumptionService {

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private MeterReadingRepository meterReadingRepository;

    private static final double RATE_PER_KWH = 0.15; // standard tariff rate

    // --- Consumer CRUD ---

    @Transactional
    public ConsumerDto createConsumer(ConsumerDto dto) {
        if (consumerRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Consumer email already registered");
        }

        Consumer consumer = Consumer.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .contractCapacity(dto.getContractCapacity())
                .build();

        consumer = consumerRepository.save(consumer);
        return mapToConsumerDto(consumer);
    }

    @Transactional
    public ConsumerDto updateConsumer(Long id, ConsumerDto dto) {
        Consumer consumer = consumerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consumer not found"));

        if (!consumer.getEmail().equalsIgnoreCase(dto.getEmail()) && consumerRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }

        consumer.setName(dto.getName());
        consumer.setEmail(dto.getEmail());
        consumer.setPhone(dto.getPhone());
        consumer.setAddress(dto.getAddress());
        consumer.setContractCapacity(dto.getContractCapacity());

        consumer = consumerRepository.save(consumer);
        return mapToConsumerDto(consumer);
    }

    public Page<ConsumerDto> getConsumers(String search, Pageable pageable) {
        Page<Consumer> consumers;
        if (search != null && !search.trim().isEmpty()) {
            consumers = consumerRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
        } else {
            consumers = consumerRepository.findAll(pageable);
        }
        return consumers.map(this::mapToConsumerDto);
    }

    public ConsumerDto getConsumerById(Long id) {
        Consumer consumer = consumerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consumer not found"));
        return mapToConsumerDto(consumer);
    }

    @Transactional
    public void deleteConsumer(Long id) {
        Consumer consumer = consumerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consumer not found"));
        consumerRepository.delete(consumer);
    }

    // --- Meter Readings ---

    @Transactional
    public MeterReadingDto addMeterReading(MeterReadingDto dto) {
        Consumer consumer = consumerRepository.findById(dto.getConsumerId())
                .orElseThrow(() -> new ResourceNotFoundException("Consumer not found"));

        // Auto-calculate bill: consumption * tariff rate
        double calculatedBill = dto.getActivePower() * RATE_PER_KWH;

        MeterReading reading = MeterReading.builder()
                .consumer(consumer)
                .readingDate(LocalDateTime.now())
                .activePower(dto.getActivePower())
                .reactivePower(dto.getReactivePower())
                .billingAmount(calculatedBill)
                .status("UNBILLED")
                .build();

        reading = meterReadingRepository.save(reading);
        return mapToReadingDto(reading);
    }

    public List<MeterReadingDto> getConsumerReadings(Long consumerId) {
        return meterReadingRepository.findByConsumerIdOrderByReadingDateDesc(consumerId).stream()
                .map(this::mapToReadingDto)
                .collect(Collectors.toList());
    }

    public List<MeterReadingDto> getAllReadings() {
        return meterReadingRepository.findAll().stream()
                .map(this::mapToReadingDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void payBill(Long readingId) {
        MeterReading reading = meterReadingRepository.findById(readingId)
                .orElseThrow(() -> new ResourceNotFoundException("Meter reading record not found"));
        reading.setStatus("BILLED");
        meterReadingRepository.save(reading);
    }

    // --- Mappings ---

    private ConsumerDto mapToConsumerDto(Consumer c) {
        return ConsumerDto.builder()
                .id(c.getId())
                .name(c.getName())
                .email(c.getEmail())
                .phone(c.getPhone())
                .address(c.getAddress())
                .contractCapacity(c.getContractCapacity())
                .build();
    }

    private MeterReadingDto mapToReadingDto(MeterReading mr) {
        return MeterReadingDto.builder()
                .id(mr.getId())
                .consumerId(mr.getConsumer().getId())
                .consumerName(mr.getConsumer().getName())
                .readingDate(mr.getReadingDate())
                .activePower(mr.getActivePower())
                .reactivePower(mr.getReactivePower())
                .billingAmount(mr.getBillingAmount())
                .status(mr.getStatus())
                .build();
    }
}
