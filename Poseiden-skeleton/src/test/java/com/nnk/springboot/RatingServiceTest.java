package com.nnk.springboot;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingService;
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

public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

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
        when(ratingRepository.findAll()).thenReturn(List.of(new Rating()));

        // Act
        String viewName = ratingService.homeService(model);

        // Assert
        assertEquals("rating/list", viewName);
        verify(model).addAttribute(eq("ratings"), any());
    }

    @Test
    void validateService_ShouldReturnAddFormWhenErrorsExist() {
        // Arrange
        when(result.hasErrors()).thenReturn(true);

        // Act
        String viewName = ratingService.validateService(new Rating(), result, model);

        // Assert
        assertEquals("rating/add", viewName);
    }

    @Test
    void validateService_ShouldSaveRatingAndRedirect() {
        // Arrange
        when(result.hasErrors()).thenReturn(false);

        // Act
        String viewName = ratingService.validateService(new Rating(), result, model);

        // Assert
        assertEquals("redirect:/rating/list", viewName);
        verify(ratingRepository).save(any(Rating.class));
    }

    @Test
    void showUpdateFormService_ShouldReturnUpdateViewWhenRatingExists() {
        // Arrange
        Rating rating = new Rating();
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(rating));

        // Act
        String viewName = ratingService.showUpdateFormService(1, model);

        // Assert
        assertEquals("rating/update", viewName);
        verify(model).addAttribute(eq("rating"), any());
    }
    @Test
    void updateRatingService_ShouldReturnUpdateFormWhenErrorsExist() {
        // Arrange
        when(result.hasErrors()).thenReturn(true);

        // Act
        String viewName = ratingService.updateRatingService(1, new Rating(), result, model);

        // Assert
        assertEquals("rating/update", viewName);
    }

    @Test
    void updateRatingService_ShouldSaveRatingAndRedirect() {
        // Arrange
        when(result.hasErrors()).thenReturn(false);
        Rating rating = new Rating();
        rating.setId(1);

        // Act
        String viewName = ratingService.updateRatingService(1, rating, result, model);

        // Assert
        assertEquals("redirect:/rating/list", viewName);
        verify(ratingRepository).save(any(Rating.class));
    }

    @Test
    void deleteRatingService_ShouldDeleteAndRedirect() {
        // Act
        String viewName = ratingService.deleteRatingService(1, model);

        // Assert
        assertEquals("redirect:/rating/list", viewName);
        verify(ratingRepository).deleteById(anyInt());
    }

    @Test
    void deleteRatingService_ShouldReturnErrorWhenException() {
        // Arrange
        doThrow(new RuntimeException("Deletion failed")).when(ratingRepository).deleteById(anyInt());

        // Act
        String viewName = ratingService.deleteRatingService(1, model);

        // Assert
        assertEquals("error", viewName);
        verify(model).addAttribute(eq("error"), any(String.class));
    }
}

