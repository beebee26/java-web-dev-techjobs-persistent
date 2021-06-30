package org.launchcode.javawebdevtechjobspersistent.controllers;

import org.launchcode.javawebdevtechjobspersistent.models.Employer;
import org.launchcode.javawebdevtechjobspersistent.models.Job;
import org.launchcode.javawebdevtechjobspersistent.models.Skill;
import org.launchcode.javawebdevtechjobspersistent.models.data.EmployerRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.JobRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import java.util.Optional;
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

    @RequestMapping("")
    public String index(Model model) {

        model.addAttribute("title", "My Jobs");
        model.addAttribute("jobs", jobRepository.findAll());

        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("title", "Add Job");
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        model.addAttribute(new Job());
        return "add";
    }

    //Added required=false for skills to prevent whitelabel error if no skills are checked. User is given error message.
    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                    Errors errors, Model model, @RequestParam int employerId, @RequestParam(required = false) List<Integer> skills) {
        //If user does not enter job name or skills, add job form is reloaded with employer and skills options.
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            model.addAttribute("skills", skillRepository.findAll());
            model.addAttribute("employers", employerRepository.findAll());
            return "add";
        }
        //Get list of skills by skill ids and add the list of skills to the skills list for that job.
        List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
        if (skillObjs.isEmpty()) {
            model.addAttribute("title", "Add Job");
            return "add";
        } else {
            newJob.setSkills(skillObjs);
        }
        //Get the employer object by id and if it exists, add it to the employer object for that job.
        Optional<Employer> employerMatch = employerRepository.findById(employerId);
        if (employerMatch.isPresent()) {
            Employer employer = employerMatch.get();
            newJob.setEmployer(employer);
        } else {
            model.addAttribute("title", "Add Job");
            return "add";
        }
        //Load the job name, employer, skills into the database
        jobRepository.save(newJob);
        model.addAttribute("job", jobRepository.findAll());
        return "redirect:";
    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {

        //Get the job object by its ID if it exists and pass that object to the view page.
        Optional optJob = jobRepository.findById(jobId);
        if (optJob.isPresent()) {
            Job job = (Job) optJob.get();
            model.addAttribute("job", job);
            return "view";
        } else {
            return "redirect:";
        }
    }
}
