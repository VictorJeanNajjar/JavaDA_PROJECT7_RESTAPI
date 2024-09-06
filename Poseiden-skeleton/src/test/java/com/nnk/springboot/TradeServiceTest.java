package com.nnk.springboot;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.TradeService;
import org.junit.jupiter.api.BeforeEach;
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

public class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    @Mock
    private Model model;

    @Mock
    private BindingResult result;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void homeService_ShouldReturnListView() {
        // Arrange
        when(tradeRepository.findAll()).thenReturn(List.of(new Trade()));

        // Act
        String viewName = tradeService.homeService(model);

        // Assert
        assertEquals("trade/list", viewName);
        verify(model).addAttribute(eq("trades"), any());
    }

    @Test
    void validateService_ShouldReturnAddFormWhenErrorsExist() {
        // Arrange
        when(result.hasErrors()).thenReturn(true);

        // Act
        String viewName = tradeService.validateService(new Trade(), result, model);

        // Assert
        assertEquals("trade/add", viewName);
    }

    @Test
    void validateService_ShouldSaveTradeAndRedirect() {
        // Arrange
        when(result.hasErrors()).thenReturn(false);

        // Act
        String viewName = tradeService.validateService(new Trade(), result, model);

        // Assert
        assertEquals("redirect:/trade/list", viewName);
        verify(tradeRepository).save(any(Trade.class));
    }

    @Test
    void showUpdateFormService_ShouldReturnUpdateViewWhenTradeExists() {
        // Arrange
        Trade trade = new Trade();
        when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(trade));

        // Act
        String viewName = tradeService.showUpdateFormService(1, model);

        // Assert
        assertEquals("trade/update", viewName);
        verify(model).addAttribute(eq("trade"), any());
    }
    @Test
    void updateTradeService_ShouldReturnUpdateFormWhenErrorsExist() {
        // Arrange
        when(result.hasErrors()).thenReturn(true);

        // Act
        String viewName = tradeService.updateTradeService(1, new Trade(), result, model);

        // Assert
        assertEquals("trade/update", viewName);
    }

    @Test
    void updateTradeService_ShouldSaveTradeAndRedirect() {
        // Arrange
        when(result.hasErrors()).thenReturn(false);
        Trade trade = new Trade();
        trade.setTradeId(1);

        // Act
        String viewName = tradeService.updateTradeService(1, trade, result, model);

        // Assert
        assertEquals("redirect:/trade/list", viewName);
        verify(tradeRepository).save(any(Trade.class));
    }

    @Test
    void deleteTradeService_ShouldDeleteAndRedirect() {
        // Act
        String viewName = tradeService.deleteTradeService(1, model);

        // Assert
        assertEquals("redirect:/trade/list", viewName);
        verify(tradeRepository).deleteById(anyInt());
    }

    @Test
    void deleteTradeService_ShouldReturnErrorWhenException() {
        // Arrange
        doThrow(new RuntimeException("Deletion failed")).when(tradeRepository).deleteById(anyInt());

        // Act
        String viewName = tradeService.deleteTradeService(1, model);

        // Assert
        assertEquals("error", viewName);
        verify(model).addAttribute(eq("error"), any(String.class));
    }
}
