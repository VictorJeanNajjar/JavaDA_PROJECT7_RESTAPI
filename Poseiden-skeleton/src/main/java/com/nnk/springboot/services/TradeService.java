package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
public class TradeService {
    @Autowired
    private TradeRepository tradeRepository;
    public String homeService(Model model) {
        model.addAttribute("trades", tradeRepository.findAll());
        return "trade/list";
    }
    public String validateService(Trade trade, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/add";
        }
        tradeRepository.save(trade);
        return "redirect:/trade/list";
    }
    public String showUpdateFormService(Integer id, Model model) {
        Trade trade = tradeRepository.findById(id).orElseThrow(() -> new RuntimeException("existing trade not found"));;
        if (trade == null) {
            model.addAttribute("error", "Trade not found with ID: " + id);
            return "error";
        }
        model.addAttribute("trade", trade);
        return "trade/update";
    }
    public String updateTradeService(Integer id, Trade trade,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/update";
        }
        if (!id.equals(trade.getTradeId())) {
            model.addAttribute("error", "ID mismatch. Please try again.");
            return "error";
        }

        tradeRepository.save(trade);
        return "redirect:/trade/list";
    }
    public String deleteTradeService(Integer id, Model model) {
        try {
            tradeRepository.deleteById(id);
        } catch (Exception e) {
            model.addAttribute("error", "Trade not found or could not be deleted.");
            return "error";
        }
        return "redirect:/trade/list";
    }

}
