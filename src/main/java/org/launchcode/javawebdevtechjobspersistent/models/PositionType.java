package org.launchcode.javawebdevtechjobspersistent.models;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PositionType extends AbstractEntity {

    private String description;

    @OneToMany(mappedBy = "positionType")
    private List<Job> jobs = new ArrayList<>();

    public PositionType() {}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Job> getJobs(){
        return jobs;
    }
}
