package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.enumeration.State;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Cd.
 */
@Entity
@Table(name = "cd")
public class Cd implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "performer")
    private String performer;

    @Column(name = "release_year")
    private String releaseYear;

    @Column(name = "disc_count")
    private String discCount;

    @Column(name = "medium")
    private String medium;

    @Column(name = "label")
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @Column(name = "added")
    private Instant added;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cd id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Cd name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerformer() {
        return this.performer;
    }

    public Cd performer(String performer) {
        this.setPerformer(performer);
        return this;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getReleaseYear() {
        return this.releaseYear;
    }

    public Cd releaseYear(String releaseYear) {
        this.setReleaseYear(releaseYear);
        return this;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDiscCount() {
        return this.discCount;
    }

    public Cd discCount(String discCount) {
        this.setDiscCount(discCount);
        return this;
    }

    public void setDiscCount(String discCount) {
        this.discCount = discCount;
    }

    public String getMedium() {
        return this.medium;
    }

    public Cd medium(String medium) {
        this.setMedium(medium);
        return this;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLabel() {
        return this.label;
    }

    public Cd label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public State getState() {
        return this.state;
    }

    public Cd state(State state) {
        this.setState(state);
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Instant getAdded() {
        return this.added;
    }

    public Cd added(Instant added) {
        this.setAdded(added);
        return this;
    }

    public void setAdded(Instant added) {
        this.added = added;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cd)) {
            return false;
        }
        return id != null && id.equals(((Cd) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cd{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", performer='" + getPerformer() + "'" +
            ", releaseYear='" + getReleaseYear() + "'" +
            ", discCount='" + getDiscCount() + "'" +
            ", medium='" + getMedium() + "'" +
            ", label='" + getLabel() + "'" +
            ", state='" + getState() + "'" +
            ", added='" + getAdded() + "'" +
            "}";
    }
}
