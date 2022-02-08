package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Dvd;
import com.mycompany.myapp.repository.DvdRepository;
import com.mycompany.myapp.service.DvdService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Dvd}.
 */
@RestController
@RequestMapping("/api")
public class DvdResource {

    private final Logger log = LoggerFactory.getLogger(DvdResource.class);

    private static final String ENTITY_NAME = "jhipsterTest5Dvd";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DvdService dvdService;

    private final DvdRepository dvdRepository;

    public DvdResource(DvdService dvdService, DvdRepository dvdRepository) {
        this.dvdService = dvdService;
        this.dvdRepository = dvdRepository;
    }

    /**
     * {@code POST  /dvds} : Create a new dvd.
     *
     * @param dvd the dvd to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dvd, or with status {@code 400 (Bad Request)} if the dvd has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dvds")
    public ResponseEntity<Dvd> createDvd(@Valid @RequestBody Dvd dvd) throws URISyntaxException {
        log.debug("REST request to save Dvd : {}", dvd);
        if (dvd.getId() != null) {
            throw new BadRequestAlertException("A new dvd cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dvd result = dvdService.save(dvd);
        return ResponseEntity
            .created(new URI("/api/dvds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dvds/:id} : Updates an existing dvd.
     *
     * @param id the id of the dvd to save.
     * @param dvd the dvd to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dvd,
     * or with status {@code 400 (Bad Request)} if the dvd is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dvd couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dvds/{id}")
    public ResponseEntity<Dvd> updateDvd(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Dvd dvd)
        throws URISyntaxException {
        log.debug("REST request to update Dvd : {}, {}", id, dvd);
        if (dvd.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dvd.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dvdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dvd result = dvdService.save(dvd);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dvd.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dvds/:id} : Partial updates given fields of an existing dvd, field will ignore if it is null
     *
     * @param id the id of the dvd to save.
     * @param dvd the dvd to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dvd,
     * or with status {@code 400 (Bad Request)} if the dvd is not valid,
     * or with status {@code 404 (Not Found)} if the dvd is not found,
     * or with status {@code 500 (Internal Server Error)} if the dvd couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dvds/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dvd> partialUpdateDvd(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Dvd dvd)
        throws URISyntaxException {
        log.debug("REST request to partial update Dvd partially : {}, {}", id, dvd);
        if (dvd.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dvd.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dvdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dvd> result = dvdService.partialUpdate(dvd);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dvd.getId().toString())
        );
    }

    /**
     * {@code GET  /dvds} : get all the dvds.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dvds in body.
     */
    @GetMapping("/dvds")
    public ResponseEntity<List<Dvd>> getAllDvds(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Dvds");
        Page<Dvd> page = dvdService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dvds/:id} : get the "id" dvd.
     *
     * @param id the id of the dvd to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dvd, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dvds/{id}")
    public ResponseEntity<Dvd> getDvd(@PathVariable Long id) {
        log.debug("REST request to get Dvd : {}", id);
        Optional<Dvd> dvd = dvdService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dvd);
    }

    /**
     * {@code DELETE  /dvds/:id} : delete the "id" dvd.
     *
     * @param id the id of the dvd to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dvds/{id}")
    public ResponseEntity<Void> deleteDvd(@PathVariable Long id) {
        log.debug("REST request to delete Dvd : {}", id);
        dvdService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
