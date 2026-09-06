package com.natixis.scheduling.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.natixis.scheduling.dto.TransferRequestDTO;
import com.natixis.scheduling.dto.TransferResponseDTO;
import com.natixis.scheduling.exception.ResourceNotFoundException;
import com.natixis.scheduling.model.Transfer;
import com.natixis.scheduling.repository.TransferRepository;
import com.natixis.scheduling.strategy.FeeContext;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @Mock
    private TransferRepository repository;

    @Mock
    private FeeContext feeContext;

    private TransferServiceImpl service;

    private Clock fixedClock;

    @BeforeEach
    void setUp() {

        fixedClock = Clock.fixed(
                Instant.parse("2026-09-06T10:00:00Z"),
                ZoneOffset.UTC
        );

        service = new TransferServiceImpl(
                repository,
                feeContext,
                fixedClock
        );
    }

    @Test
    void shouldCreateTransfer() {

        TransferRequestDTO request = new TransferRequestDTO(
                "123",
                "456",
                new BigDecimal("500.00"),
                LocalDate.of(2026, 9, 6)
        );

        when(feeContext.calculateFee(any(Transfer.class)))
                .thenReturn(new BigDecimal("18.00"));

        when(repository.save(any(Transfer.class)))
                .thenAnswer(invocation -> {

                    Transfer transfer = invocation.getArgument(0);

                    transfer.setId(1L);

                    return transfer;
                });

        TransferResponseDTO response =
                service.createTransfer(request);

        assertEquals(1L, response.id());
        assertEquals("123", response.sourceAccount());
        assertEquals("456", response.destinationAccount());
        assertEquals(
                new BigDecimal("500.00"),
                response.amount()
        );
        assertEquals(
                new BigDecimal("18.00"),
                response.fee()
        );
        assertEquals(
                LocalDate.of(2026, 9, 6),
                response.scheduledDate()
        );
        assertEquals(
                LocalDate.of(2026, 9, 6),
                response.transferDate()
        );

        verify(feeContext)
                .calculateFee(any(Transfer.class));

        verify(repository)
                .save(any(Transfer.class));
    }

    @Test
    void shouldFindTransferById() {

        Transfer transfer = createTransfer();

        when(repository.findById(1L))
                .thenReturn(Optional.of(transfer));

        TransferResponseDTO response =
                service.findById(1L);

        assertEquals(1L, response.id());
        assertEquals("123", response.sourceAccount());
        assertEquals("456", response.destinationAccount());
        assertEquals(
                new BigDecimal("500.00"),
                response.amount()
        );

        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTransferDoesNotExist() {

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(999L)
        );

        verify(repository).findById(999L);
    }

    @Test
    void shouldReturnAllTransfers() {

        Transfer firstTransfer = createTransfer();

        Transfer secondTransfer = new Transfer(
                2L,
                "777",
                "888",
                new BigDecimal("1500.00"),
                new BigDecimal("135.00"),
                LocalDate.of(2026, 9, 11),
                LocalDate.of(2026, 9, 6)
        );

        when(repository.findAll())
                .thenReturn(
                        List.of(
                                firstTransfer,
                                secondTransfer
                        )
                );

        List<TransferResponseDTO> response =
                service.findAll();

        assertEquals(2, response.size());

        assertEquals(
                1L,
                response.get(0).id()
        );

        assertEquals(
                2L,
                response.get(1).id()
        );

        verify(repository).findAll();
    }

    @Test
    void shouldUpdateTransfer() {

        Transfer existingTransfer = createTransfer();

        TransferRequestDTO request = new TransferRequestDTO(
                "123",
                "999",
                new BigDecimal("800.00"),
                LocalDate.of(2026, 9, 6)
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(existingTransfer));

        when(feeContext.calculateFee(existingTransfer))
                .thenReturn(new BigDecimal("27.00"));

        when(repository.save(existingTransfer))
                .thenReturn(existingTransfer);

        TransferResponseDTO response =
                service.updateTransfer(
                        1L,
                        request
                );

        assertEquals(
                "999",
                response.destinationAccount()
        );

        assertEquals(
                new BigDecimal("800.00"),
                response.amount()
        );

        assertEquals(
                new BigDecimal("27.00"),
                response.fee()
        );

        verify(repository).findById(1L);

        verify(feeContext)
                .calculateFee(existingTransfer);

        verify(repository)
                .save(existingTransfer);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingTransfer() {

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        TransferRequestDTO request = new TransferRequestDTO(
                "123",
                "456",
                new BigDecimal("500.00"),
                LocalDate.of(2026, 9, 6)
        );

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateTransfer(
                        999L,
                        request
                )
        );

        verify(repository, never())
                .save(any(Transfer.class));
    }

    @Test
    void shouldDeleteExistingTransfer() {

        when(repository.existsById(1L))
                .thenReturn(true);

        service.deleteTransfer(1L);

        verify(repository)
                .deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingTransfer() {

        when(repository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteTransfer(999L)
        );

        verify(repository, never())
                .deleteById(999L);
    }

    @Test
    void shouldFindTransfersByAccount() {

        Transfer transfer = createTransfer();

        when(
                repository
                        .findBySourceAccountOrDestinationAccount(
                                "123",
                                "123"
                        )
        ).thenReturn(List.of(transfer));

        List<TransferResponseDTO> response =
                service.findByAccount("123");

        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).id());

        verify(repository)
                .findBySourceAccountOrDestinationAccount(
                        "123",
                        "123"
                );
    }

    @Test
    void shouldFindTransfersByDateRange() {

        LocalDate startDate =
                LocalDate.of(2026, 9, 1);

        LocalDate endDate =
                LocalDate.of(2026, 9, 30);

        when(
                repository.findByTransferDateBetween(
                        startDate,
                        endDate
                )
        ).thenReturn(List.of(createTransfer()));

        List<TransferResponseDTO> response =
                service.findByDateRange(
                        startDate,
                        endDate
                );

        assertEquals(1, response.size());

        verify(repository)
                .findByTransferDateBetween(
                        startDate,
                        endDate
                );
    }

    @Test
    void shouldRejectInvalidDateRange() {

        LocalDate startDate =
                LocalDate.of(2026, 9, 30);

        LocalDate endDate =
                LocalDate.of(2026, 9, 1);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.findByDateRange(
                        startDate,
                        endDate
                )
        );

        verify(repository, never())
                .findByTransferDateBetween(
                        any(LocalDate.class),
                        any(LocalDate.class)
                );
    }

    private Transfer createTransfer() {

        return new Transfer(
                1L,
                "123",
                "456",
                new BigDecimal("500.00"),
                new BigDecimal("18.00"),
                LocalDate.of(2026, 9, 6),
                LocalDate.of(2026, 9, 6)
        );
    }
}