package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidService;
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

public class BidServiceTest {

    @Mock
    private BidListRepository bidListRepository;

    @InjectMocks
    private BidService bidService;

    @Mock
    private Model model;

    @Mock
    private BindingResult result;

    public BidServiceTest() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void checkAllBids_ShouldReturnListView() {
        // Arrange
        when(bidListRepository.findAll()).thenReturn(List.of(new BidList()));

        // Act
        String viewName = bidService.checkAllBids(model);

        // Assert
        assertEquals("bidList/list", viewName);
        verify(model).addAttribute(eq("bidlists"), any());
    }

    @Test
    void validateService_ShouldReturnAddFormWhenErrorsExist() {
        // Arrange
        when(result.hasErrors()).thenReturn(true);

        // Act
        String viewName = bidService.validateService(new BidList(), result, model);

        // Assert
        assertEquals("bidList/add", viewName);
    }

    @Test
    void validateService_ShouldSaveBidAndRedirect() {
        // Arrange
        when(result.hasErrors()).thenReturn(false);

        // Act
        String viewName = bidService.validateService(new BidList(), result, model);

        // Assert
        assertEquals("redirect:/bidList/list", viewName);
        verify(bidListRepository).save(any(BidList.class));
    }

    @Test
    void showUpdateFormService_ShouldReturnUpdateView() {
        // Arrange
        when(bidListRepository.findById(anyInt())).thenReturn(Optional.of(new BidList()));

        // Act
        String viewName = bidService.showUpdateFormService(1, model);

        // Assert
        assertEquals("bidList/update", viewName);
        verify(model).addAttribute(eq("bid"), any());
    }

    @Test
    void updateBidService_ShouldReturnUpdateFormWhenErrorsExist() {
        // Arrange
        when(result.hasErrors()).thenReturn(true);

        // Act
        String viewName = bidService.updateBidService(1, new BidList(), result, model);

        // Assert
        assertEquals("bidList/update", viewName);
        verify(model).addAttribute(eq("bidList"), any());
    }

    @Test
    void updateBidService_ShouldSaveBidAndRedirect() {
        // Arrange
        when(result.hasErrors()).thenReturn(false);
        BidList bidList = new BidList();
        bidList.setBidListId(1);

        // Act
        String viewName = bidService.updateBidService(1, bidList, result, model);

        // Assert
        assertEquals("redirect:/bidList/list", viewName);
        verify(bidListRepository).save(any(BidList.class));
    }

    @Test
    void deleteBidService_ShouldDeleteAndRedirect() {
        // Act
        String viewName = bidService.deleteBidService(1, model);

        // Assert
        assertEquals("redirect:/bidList/list", viewName);
        verify(bidListRepository).deleteById(anyInt());
    }
}

