package com.nnk.springboot;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.RuleNameService;
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

public class RuleNameServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    private RuleNameService ruleNameService;

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
        when(ruleNameRepository.findAll()).thenReturn(List.of(new RuleName()));

        // Act
        String viewName = ruleNameService.homeService(model);

        // Assert
        assertEquals("ruleName/list", viewName);
        verify(model).addAttribute(eq("ruleNames"), any());
    }

    @Test
    void validateService_ShouldReturnAddFormWhenErrorsExist() {
        // Arrange
        when(result.hasErrors()).thenReturn(true);

        // Act
        String viewName = ruleNameService.validateService(new RuleName(), result, model);

        // Assert
        assertEquals("ruleName/add", viewName);
    }

    @Test
    void validateService_ShouldSaveRuleNameAndRedirect() {
        // Arrange
        when(result.hasErrors()).thenReturn(false);

        // Act
        String viewName = ruleNameService.validateService(new RuleName(), result, model);

        // Assert
        assertEquals("redirect:/ruleName/list", viewName);
        verify(ruleNameRepository).save(any(RuleName.class));
    }

    @Test
    void showUpdateFormService_ShouldReturnUpdateViewWhenRuleNameExists() {
        // Arrange
        RuleName ruleName = new RuleName();
        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(ruleName));

        // Act
        String viewName = ruleNameService.showUpdateFormService(1, model);

        // Assert
        assertEquals("ruleName/update", viewName);
        verify(model).addAttribute(eq("ruleName"), any());
    }

    @Test
    void updateRuleNameService_ShouldReturnUpdateFormWhenErrorsExist() {
        // Arrange
        when(result.hasErrors()).thenReturn(true);

        // Act
        String viewName = ruleNameService.updateRuleNameService(1, new RuleName(), result, model);

        // Assert
        assertEquals("ruleName/update", viewName);
    }

    @Test
    void updateRuleNameService_ShouldSaveRuleNameAndRedirect() {
        // Arrange
        when(result.hasErrors()).thenReturn(false);
        RuleName ruleName = new RuleName();
        ruleName.setId(1);

        // Act
        String viewName = ruleNameService.updateRuleNameService(1, ruleName, result, model);

        // Assert
        assertEquals("redirect:/ruleName/list", viewName);
        verify(ruleNameRepository).save(any(RuleName.class));
    }

    @Test
    void deleteRuleNameService_ShouldDeleteAndRedirect() {
        // Act
        String viewName = ruleNameService.deleteRuleNameService(1, model);

        // Assert
        assertEquals("redirect:/ruleName/list", viewName);
        verify(ruleNameRepository).deleteById(anyInt());
    }

    @Test
    void deleteRuleNameService_ShouldReturnErrorWhenException() {
        // Arrange
        doThrow(new RuntimeException("Deletion failed")).when(ruleNameRepository).deleteById(anyInt());

        // Act
        String viewName = ruleNameService.deleteRuleNameService(1, model);

        // Assert
        assertEquals("error", viewName);
        verify(model).addAttribute(eq("error"), any(String.class));
    }
}
