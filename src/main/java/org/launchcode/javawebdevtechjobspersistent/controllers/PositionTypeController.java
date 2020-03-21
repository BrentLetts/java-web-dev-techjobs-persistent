package org.launchcode.javawebdevtechjobspersistent.controllers;

import org.launchcode.javawebdevtechjobspersistent.models.PositionType;
import org.launchcode.javawebdevtechjobspersistent.models.data.PositionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("positionType")
public class PositionTypeController {

    @Autowired
    public PositionTypeRepository positionTypeRepository;

    @GetMapping("add")
    public String displayAddPositionForm(Model model){
        model.addAttribute(new PositionType());
        return "positionType/add";
    }

    @PostMapping("add")
    public String processAddPositionForm(@ModelAttribute @Valid PositionType newPositionType,
                                         Errors errors, Model model){
        if(errors.hasErrors()){
            model.addAttribute(newPositionType);
            return "positionType/add";
        }

        // Check to see if the name already exists
        Iterable<PositionType> positionTypes = positionTypeRepository.findAll();

        for(PositionType positionType : positionTypes){
            if(positionType.getName().equals(newPositionType.getName())){
                errors.rejectValue("name", "name.alreadyexists", "This position name already exists");
                model.addAttribute("title", "Add Position");
                return "positionType/add";
            }
        }

        positionTypeRepository.save(newPositionType);

        return "redirect:/add";
    }

    @GetMapping("view/{positionTypeId}")
    public String displayViewPosition(Model model, @PathVariable int positionTypeId){
        Optional optPosition = positionTypeRepository.findById(positionTypeId);
        if(optPosition.isPresent()){
            PositionType positionType = (PositionType)optPosition.get();
            model.addAttribute("position", positionType);
            return "positionType/view";
        }else{
            return "redirect:../";
        }
    }

}
