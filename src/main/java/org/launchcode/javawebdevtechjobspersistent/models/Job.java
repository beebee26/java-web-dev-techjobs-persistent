package org.launchcode.javawebdevtechjobspersistent.models;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Job extends AbstractEntity{

    @ManyToOne
    private Employer employer;

    //@Size added - user must check at least one skill checkbox on add job form.
    @ManyToMany
    @Size(min=1, max=10, message="You must choose at least one skill or add a new skill. You may add a maximum of 10 skills.")
    private List<Skill> skills = new ArrayList<Skill>();

    public Job() {
    }

    public Job(Employer anEmployer, List someSkills) {
        super();
        this.employer = anEmployer;
        this.skills = someSkills;
    }

    // Getters and setters.

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public List getSkills() {
        return skills;
    }

    public void setSkills(List skills) {
        this.skills = skills;
    }
}
