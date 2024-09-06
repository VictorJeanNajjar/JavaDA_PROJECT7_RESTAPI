package com.nnk.springboot;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.CurvePointService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class CurvePointServiceTest {

    @Mock
    private CurvePointRepository curvePointRepository;

    @InjectMocks
    private CurvePointService curvePointService;

    @Mock
    private Model model;

    @Mock
    private BindingResult result;

    public CurvePointServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void homeService_ShouldReturnListView() {
        // Arrange
        when(curvePointRepository.findAll()).thenReturn(List.of(new CurvePoint()));

        // Act
        String viewName = curvePointService.homeService(model);

        // Assert
        assertEquals("curvePoint/list", viewName);
        verify(model).addAttribute(eq("curvePoints"), any());
    }

    @Test
    void validateService_ShouldReturnAddFormWhenErrorsExist() {
        // Arrange
        when(result.hasErrors()).thenReturn(true);

        // Act
        String viewName = curvePointService.validateService(new CurvePoint(), result, model);

        // Assert
        assertEquals("curvePoint/add", viewName);
    }

    @Test
    void validateService_ShouldSaveCurvePointAndRedirect() {
        // Arrange
        when(result.hasErrors()).thenReturn(false);

        // Act
        String viewName = curvePointService.validateService(new CurvePoint(), result, model);

        // Assert
        assertEquals("redirect:/curvePoint/list", viewName);
        verify(curvePointRepository).save(any(CurvePoint.class));
    }

    @Test
    void showUpdateFormService_ShouldReturnUpdateView() {
        // Arrange
        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(new CurvePoint()));

        // Act
        String viewName = curvePointService.showUpdateFormService(1, model);

        // Assert
        assertEquals("curvePoint/update", viewName);
        verify(model).addAttribute(eq("curvePoint"), any());
    }

    @Test
    void updateCurvePointService_ShouldReturnUpdateFormWhenErrorsExist() {
        // Arrange
        when(result.hasErrors()).thenReturn(true);

        // Act
        String viewName = curvePointService.updateBidService(1, new CurvePoint(), result, model);

        // Assert
        assertEquals("curvePoint/update", viewName);
        verify(model).addAttribute(eq("curvePoint"), any());
    }

    @Test
    void updateCurvePointService_ShouldSaveCurvePointAndRedirect() {
        // Arrange
        when(result.hasErrors()).thenReturn(false);
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setId(1);

        // Act
        String viewName = curvePointService.updateBidService(1, curvePoint, result, model);

        // Assert
        assertEquals("redirect:/curvePoint/list", viewName);
        verify(curvePointRepository).save(any(CurvePoint.class));
    }

    @Test
    void deleteCurvePointService_ShouldDeleteAndRedirect() {
        // Act
        String viewName = curvePointService.deleteBidService(1);

        // Assert
        assertEquals("redirect:/curvePoint/list", viewName);
        verify(curvePointRepository).deleteById(anyInt());
    }
}
