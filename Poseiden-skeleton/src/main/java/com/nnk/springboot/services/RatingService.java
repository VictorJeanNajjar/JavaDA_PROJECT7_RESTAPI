package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    public String homeService(Model model) {
        model.addAttribute("ratings", ratingRepository.findAll());
        return "rating/list";
    }
    public String validateService(Rating rating, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/add";
        }
        ratingRepository.save(rating);
        return "redirect:/rating/list";
    }
    public String showUpdateFormService(Integer id, Model model) {
        Rating rating = ratingRepository.findById(id).orElseThrow(() -> new RuntimeException("existing rating not found"));
        if (rating == null) {
            model.addAttribute("error", "Rating not found with ID: " + id);
            return "error";
        }
        model.addAttribute("rating", rating);
        return "rating/update";
    }
    public String updateRatingService(Integer id, Rating rating,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/update";
        }
        if (!id.equals(rating.getId())) {
            model.addAttribute("error", "ID mismatch. Please try again.");
            return "error";
        }
        ratingRepository.save(rating);
        return "redirect:/rating/list";
    }
    public String deleteRatingService(Integer id, Model model) {
        try {
            ratingRepository.deleteById(id);
        } catch (Exception e) {
            model.addAttribute("error", "Rating not found or could not be deleted.");
            return "error";
        }
        return "redirect:/rating/list";
    }

}
