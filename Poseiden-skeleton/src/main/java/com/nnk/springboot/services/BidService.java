package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

@Service
public class BidService {
    @Autowired
    private BidListRepository bidListRepository;
    public String checkAllBids(Model model){
        List<BidList> bidList = bidListRepository.findAll();
        model.addAttribute("bidlists", bidList);
        return "bidList/list";
    }
    public String validateService( BidList bid, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/add";
        }
        bidListRepository.save(bid);
        return "redirect:/bidList/list";
    }

    public String showUpdateFormService(Integer id, Model model) {
        Optional<BidList> bid = bidListRepository.findById(id);
        model.addAttribute("bid", bid);
        return "bidList/update";
    }

    public String updateBidService(Integer id, BidList bidList,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("bidList", bidList);
            return "bidList/update";
        }if (!id.equals(bidList.getBidListId())) {
            model.addAttribute("error", "ID mismatch. Please try again.");
            return "error";
        }
        bidListRepository.save(bidList);

        return "redirect:/bidList/list";
    }


    public String deleteBidService(Integer id, Model model) {
        bidListRepository.deleteById(id);
        return "redirect:/bidList/list";
    }
}
