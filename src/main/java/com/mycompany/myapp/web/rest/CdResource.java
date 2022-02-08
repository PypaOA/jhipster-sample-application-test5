package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Cd;
import com.mycompany.myapp.repository.CdRepository;
import com.mycompany.myapp.service.CdService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Cd}.
 */
@RestController
@RequestMapping("/api")
public class CdResource {

    private final Logger log = LoggerFactory.getLogger(CdResource.class);

    private static final String ENTITY_NAME = "jhipsterTest5Cd";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CdService cdService;

    private final CdRepository cdRepository;

    public CdResource(CdService cdService, CdRepository cdRepository) {
        this.cdService = cdService;
        this.cdRepository = cdRepository;
    }

    /**
     * {@code POST  /cds} : Create a new cd.
     *
     * @param cd the cd to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cd, or with status {@code 400 (Bad Request)} if the cd has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cds")
    public ResponseEntity<Cd> createCd(@Valid @RequestBody Cd cd) throws URISyntaxException {
        log.debug("REST request to save Cd : {}", cd);
        if (cd.getId() != null) {
            throw new BadRequestAlertException("A new cd cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cd result = cdService.save(cd);
        return ResponseEntity
            .created(new URI("/api/cds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cds/:id} : Updates an existing cd.
     *
     * @param id the id of the cd to save.
     * @param cd the cd to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cd,
     * or with status {@code 400 (Bad Request)} if the cd is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cd couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cds/{id}")
    public ResponseEntity<Cd> updateCd(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Cd cd)
        throws URISyntaxException {
        log.debug("REST request to update Cd : {}, {}", id, cd);
        if (cd.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cd.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cd result = cdService.save(cd);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cd.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cds/:id} : Partial updates given fields of an existing cd, field will ignore if it is null
     *
     * @param id the id of the cd to save.
     * @param cd the cd to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cd,
     * or with status {@code 400 (Bad Request)} if the cd is not valid,
     * or with status {@code 404 (Not Found)} if the cd is not found,
     * or with status {@code 500 (Internal Server Error)} if the cd couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cds/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cd> partialUpdateCd(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Cd cd)
        throws URISyntaxException {
        log.debug("REST request to partial update Cd partially : {}, {}", id, cd);
        if (cd.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cd.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cd> result = cdService.partialUpdate(cd);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cd.getId().toString())
        );
    }

    /**
     * {@code GET  /cds} : get all the cds.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cds in body.
     */
    @GetMapping("/cds")
    public ResponseEntity<List<Cd>> getAllCds(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Cds");
        Page<Cd> page = cdService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cds/:id} : get the "id" cd.
     *
     * @param id the id of the cd to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cd, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cds/{id}")
    public ResponseEntity<Cd> getCd(@PathVariable Long id) {
        log.debug("REST request to get Cd : {}", id);
        Optional<Cd> cd = cdService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cd);
    }

    /**
     * {@code DELETE  /cds/:id} : delete the "id" cd.
     *
     * @param id the id of the cd to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cds/{id}")
    public ResponseEntity<Void> deleteCd(@PathVariable Long id) {
        log.debug("REST request to delete Cd : {}", id);
        cdService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
