package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;
@Service
public class CurvePointService {
    @Autowired
    private CurvePointRepository curvePointRepository;

    public String homeService(Model model) {
        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        model.addAttribute("curvePoints", curvePoints);
        return "curvePoint/list";
    }
    public String validateService(CurvePoint curvePoint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "curvePoint/add";
        }
        curvePointRepository.save(curvePoint);
        return "redirect:/curvePoint/list";
    }
    public String showUpdateFormService(Integer id, Model model) {
        CurvePoint curvePoint = curvePointRepository.findById(id).orElseThrow(() -> new RuntimeException("existing curve not found"));
        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/update";
    }
    public String updateBidService(Integer id, CurvePoint curvePoint,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("curvePoint", curvePoint);
            return "curvePoint/update";
        }if (!id.equals(curvePoint.getId())) {
            model.addAttribute("error", "ID mismatch. Please try again.");
            return "error";
        }
        curvePointRepository.save(curvePoint);
        return "redirect:/curvePoint/list";
    }
    public String deleteBidService(Integer id) {
        curvePointRepository.deleteById(id);
        return "redirect:/curvePoint/list";
    }
}
