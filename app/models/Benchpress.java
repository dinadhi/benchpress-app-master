package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;

/**
 * Company entity managed by Ebean
 */
@Entity
public class Benchpress extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String url;

    @Constraints.Required
    public Long parallelism;

}