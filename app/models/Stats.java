package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Stats entity managed by Ebean
 */
@Entity
public class Stats extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String status;

    @Constraints.Required
    public Long timeTaken;

    @ManyToOne
    public Benchpress benchpress;

}