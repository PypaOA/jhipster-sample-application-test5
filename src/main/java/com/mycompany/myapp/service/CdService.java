package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Cd;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Cd}.
 */
public interface CdService {
    /**
     * Save a cd.
     *
     * @param cd the entity to save.
     * @return the persisted entity.
     */
    Cd save(Cd cd);

    /**
     * Partially updates a cd.
     *
     * @param cd the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Cd> partialUpdate(Cd cd);

    /**
     * Get all the cds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cd> findAll(Pageable pageable);

    /**
     * Get the "id" cd.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Cd> findOne(Long id);

    /**
     * Delete the "id" cd.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
