package org.launchcode.javawebdevtechjobspersistent.controllers;

import org.launchcode.javawebdevtechjobspersistent.models.Employer;
import org.launchcode.javawebdevtechjobspersistent.models.Job;
import org.launchcode.javawebdevtechjobspersistent.models.PositionType;
import org.launchcode.javawebdevtechjobspersistent.models.Skill;
import org.launchcode.javawebdevtechjobspersistent.models.data.EmployerRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.JobRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.PositionTypeRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private PositionTypeRepository positionTypeRepository;

    @RequestMapping("")
    public String index(Model model) {

        model.addAttribute("title", "My Jobs");

        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("title", "Add Job");
        model.addAttribute(new Job());
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("positionTypes", positionTypeRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());

        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                       Errors errors, Model model, @RequestParam int employerId,
                                    @RequestParam List<Integer> skills,
                                    @RequestParam int positionTypeId) {

        if(!errors.hasErrors()){
            Optional optEmployer = employerRepository.findById(employerId);
            if(optEmployer.isPresent()) {
                Employer employer = (Employer) optEmployer.get();
                List<Skill> skillObjs = (List<Skill>)skillRepository.findAllById(skills);
                PositionType positionType = (PositionType)positionTypeRepository.findById(positionTypeId).get();
                newJob.setEmployer(employer);
                newJob.setSkills(skillObjs);
                newJob.setPositionType(positionType);
                jobRepository.save(newJob);
            }
            return "redirect:/list";
        }else{
            model.addAttribute("title", "Add Job");
            return "add";
        }

//        newJob.setEmployer(employerRepository.findById(employerId)));


    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {

        Optional jobOpt = jobRepository.findById(jobId);
        if(jobOpt.isEmpty()){
            model.addAttribute("title", "No job matches ID: " + jobId);
            return "view";
        }
        Job job = (Job)jobOpt.get();
        model.addAttribute("title", "Job:" + job.getName());
        model.addAttribute("job", job);

        return "view";
    }


}
