package com.t6.lockhood.controller;

import com.t6.lockhood.dto.UnitDTO;
import com.t6.lockhood.model.Department;
import com.t6.lockhood.model.Unit;
import com.t6.lockhood.repository.DepartmentRepository;
import com.t6.lockhood.repository.UnitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitControllerTest {

    @Mock
    UnitRepository unitRepository;

    @Mock
    DepartmentRepository departmentRepository;

    @InjectMocks
    UnitController unitController;

    private final Department department = Department.builder().id(3).name("Assembly").build();

    @Test
    void unitIdIsNumericSoItMatchesTheRepositoryKeyAndIdentityGeneration() throws NoSuchFieldException {
        assertEquals(int.class, Unit.class.getDeclaredField("id").getType());
    }

    @Test
    void createSavesNewUnitWithNameAndDepartment() {
        when(departmentRepository.findById(3)).thenReturn(Optional.of(department));
        when(unitRepository.save(any(Unit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        unitController.saveUnit(UnitDTO.builder().name("Line A").departmentId(3).build());

        ArgumentCaptor<Unit> saved = ArgumentCaptor.forClass(Unit.class);
        verify(unitRepository).save(saved.capture());
        assertEquals(0, saved.getValue().getId());
        assertEquals("Line A", saved.getValue().getName());
        assertSame(department, saved.getValue().getDepartment());
    }

    @Test
    void createIgnoresIdInBodySoTheDatabaseGeneratesIt() {
        when(departmentRepository.findById(3)).thenReturn(Optional.of(department));
        when(unitRepository.save(any(Unit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        unitController.saveUnit(UnitDTO.builder().id(9).name("Line A").departmentId(3).build());

        ArgumentCaptor<Unit> saved = ArgumentCaptor.forClass(Unit.class);
        verify(unitRepository).save(saved.capture());
        assertEquals(0, saved.getValue().getId());
    }

    @Test
    void createWithUnknownDepartmentIsBadRequestAndSavesNothing() {
        when(departmentRepository.findById(42)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> unitController.saveUnit(UnitDTO.builder().name("Line A").departmentId(42).build()));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        verify(unitRepository, never()).save(any());
    }

    @Test
    void updateUsesPathIdAndNewValues() {
        when(unitRepository.findById(5)).thenReturn(Optional.of(Unit.builder().id(5).name("Old").department(department).build()));
        when(departmentRepository.findById(3)).thenReturn(Optional.of(department));
        when(unitRepository.save(any(Unit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Unit updated = unitController.editUnit(5, UnitDTO.builder().id(99).name("Renamed").departmentId(3).build());

        assertEquals(5, updated.getId());
        assertEquals("Renamed", updated.getName());
        assertSame(department, updated.getDepartment());
    }

    @Test
    void updateOfMissingUnitIsNotFoundAndSavesNothing() {
        when(unitRepository.findById(5)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> unitController.editUnit(5, UnitDTO.builder().name("Renamed").departmentId(3).build()));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        verify(unitRepository, never()).save(any());
    }

    @Test
    void deleteRemovesExistingUnit() {
        when(unitRepository.findById(5)).thenReturn(Optional.of(Unit.builder().id(5).build()));

        unitController.deleteUnit(5);

        verify(unitRepository).deleteById(5);
    }

    @Test
    void deleteOfMissingUnitIsNotFound() {
        when(unitRepository.findById(5)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> unitController.deleteUnit(5));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        verify(unitRepository, never()).deleteById(anyInt());
    }

    @Test
    void getByIdReturnsTheUnit() {
        Unit unit = Unit.builder().id(5).name("Line A").department(department).build();
        when(unitRepository.findById(5)).thenReturn(Optional.of(unit));

        assertSame(unit, unitController.getUnit(5));
    }
}
