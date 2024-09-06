package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
public class RuleNameService {
    @Autowired
    private RuleNameRepository ruleNameRepository;
    public String homeService(Model model) {
        model.addAttribute("ruleNames", ruleNameRepository.findAll());
        return "ruleName/list";
    }
    public String validateService(RuleName ruleName, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/add";
        }
        ruleNameRepository.save(ruleName);
        return "redirect:/ruleName/list";
    }
    public String showUpdateFormService(Integer id, Model model) {
        RuleName ruleName = ruleNameRepository.findById(id).orElseThrow(() -> new RuntimeException("existing rule not found"));
        if (ruleName == null) {
            model.addAttribute("error", "RuleName not found with ID: " + id);
            return "error";
        }
        model.addAttribute("ruleName", ruleName);
        return "ruleName/update";
    }
    public String updateRuleNameService(Integer id, RuleName ruleName,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/update";
        }
        if (!id.equals(ruleName.getId())) {
            model.addAttribute("error", "ID mismatch. Please try again.");
            return "error";
        }
        ruleNameRepository.save(ruleName);
        return "redirect:/ruleName/list";
    }
    public String deleteRuleNameService(Integer id, Model model) {
        try {
            ruleNameRepository.deleteById(id);
        } catch (Exception e) {
            model.addAttribute("error", "RuleName not found or could not be deleted.");
            return "error";
        }
        return "redirect:/ruleName/list";
    }


}
