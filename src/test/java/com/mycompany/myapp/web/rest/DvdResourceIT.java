package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Dvd;
import com.mycompany.myapp.domain.enumeration.State;
import com.mycompany.myapp.repository.DvdRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DvdResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DvdResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PERFORMER = "AAAAAAAAAA";
    private static final String UPDATED_PERFORMER = "BBBBBBBBBB";

    private static final String DEFAULT_RELEASE_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_RELEASE_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_DISC_COUNT = "AAAAAAAAAA";
    private static final String UPDATED_DISC_COUNT = "BBBBBBBBBB";

    private static final String DEFAULT_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_FORMAT = "BBBBBBBBBB";

    private static final String DEFAULT_LANG = "AAAAAAAAAA";
    private static final String UPDATED_LANG = "BBBBBBBBBB";

    private static final State DEFAULT_STATE = State.OK;
    private static final State UPDATED_STATE = State.AWAY;

    private static final Instant DEFAULT_ADDED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ADDED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/dvds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DvdRepository dvdRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDvdMockMvc;

    private Dvd dvd;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dvd createEntity(EntityManager em) {
        Dvd dvd = new Dvd()
            .name(DEFAULT_NAME)
            .performer(DEFAULT_PERFORMER)
            .releaseYear(DEFAULT_RELEASE_YEAR)
            .discCount(DEFAULT_DISC_COUNT)
            .format(DEFAULT_FORMAT)
            .lang(DEFAULT_LANG)
            .state(DEFAULT_STATE)
            .added(DEFAULT_ADDED);
        return dvd;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dvd createUpdatedEntity(EntityManager em) {
        Dvd dvd = new Dvd()
            .name(UPDATED_NAME)
            .performer(UPDATED_PERFORMER)
            .releaseYear(UPDATED_RELEASE_YEAR)
            .discCount(UPDATED_DISC_COUNT)
            .format(UPDATED_FORMAT)
            .lang(UPDATED_LANG)
            .state(UPDATED_STATE)
            .added(UPDATED_ADDED);
        return dvd;
    }

    @BeforeEach
    public void initTest() {
        dvd = createEntity(em);
    }

    @Test
    @Transactional
    void createDvd() throws Exception {
        int databaseSizeBeforeCreate = dvdRepository.findAll().size();
        // Create the Dvd
        restDvdMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dvd))
            )
            .andExpect(status().isCreated());

        // Validate the Dvd in the database
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeCreate + 1);
        Dvd testDvd = dvdList.get(dvdList.size() - 1);
        assertThat(testDvd.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDvd.getPerformer()).isEqualTo(DEFAULT_PERFORMER);
        assertThat(testDvd.getReleaseYear()).isEqualTo(DEFAULT_RELEASE_YEAR);
        assertThat(testDvd.getDiscCount()).isEqualTo(DEFAULT_DISC_COUNT);
        assertThat(testDvd.getFormat()).isEqualTo(DEFAULT_FORMAT);
        assertThat(testDvd.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testDvd.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testDvd.getAdded()).isEqualTo(DEFAULT_ADDED);
    }

    @Test
    @Transactional
    void createDvdWithExistingId() throws Exception {
        // Create the Dvd with an existing ID
        dvd.setId(1L);

        int databaseSizeBeforeCreate = dvdRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDvdMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dvd))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dvd in the database
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dvdRepository.findAll().size();
        // set the field null
        dvd.setName(null);

        // Create the Dvd, which fails.

        restDvdMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dvd))
            )
            .andExpect(status().isBadRequest());

        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDvds() throws Exception {
        // Initialize the database
        dvdRepository.saveAndFlush(dvd);

        // Get all the dvdList
        restDvdMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dvd.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].performer").value(hasItem(DEFAULT_PERFORMER)))
            .andExpect(jsonPath("$.[*].releaseYear").value(hasItem(DEFAULT_RELEASE_YEAR)))
            .andExpect(jsonPath("$.[*].discCount").value(hasItem(DEFAULT_DISC_COUNT)))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT)))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].added").value(hasItem(DEFAULT_ADDED.toString())));
    }

    @Test
    @Transactional
    void getDvd() throws Exception {
        // Initialize the database
        dvdRepository.saveAndFlush(dvd);

        // Get the dvd
        restDvdMockMvc
            .perform(get(ENTITY_API_URL_ID, dvd.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dvd.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.performer").value(DEFAULT_PERFORMER))
            .andExpect(jsonPath("$.releaseYear").value(DEFAULT_RELEASE_YEAR))
            .andExpect(jsonPath("$.discCount").value(DEFAULT_DISC_COUNT))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.added").value(DEFAULT_ADDED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDvd() throws Exception {
        // Get the dvd
        restDvdMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDvd() throws Exception {
        // Initialize the database
        dvdRepository.saveAndFlush(dvd);

        int databaseSizeBeforeUpdate = dvdRepository.findAll().size();

        // Update the dvd
        Dvd updatedDvd = dvdRepository.findById(dvd.getId()).get();
        // Disconnect from session so that the updates on updatedDvd are not directly saved in db
        em.detach(updatedDvd);
        updatedDvd
            .name(UPDATED_NAME)
            .performer(UPDATED_PERFORMER)
            .releaseYear(UPDATED_RELEASE_YEAR)
            .discCount(UPDATED_DISC_COUNT)
            .format(UPDATED_FORMAT)
            .lang(UPDATED_LANG)
            .state(UPDATED_STATE)
            .added(UPDATED_ADDED);

        restDvdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDvd.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDvd))
            )
            .andExpect(status().isOk());

        // Validate the Dvd in the database
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeUpdate);
        Dvd testDvd = dvdList.get(dvdList.size() - 1);
        assertThat(testDvd.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDvd.getPerformer()).isEqualTo(UPDATED_PERFORMER);
        assertThat(testDvd.getReleaseYear()).isEqualTo(UPDATED_RELEASE_YEAR);
        assertThat(testDvd.getDiscCount()).isEqualTo(UPDATED_DISC_COUNT);
        assertThat(testDvd.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testDvd.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testDvd.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testDvd.getAdded()).isEqualTo(UPDATED_ADDED);
    }

    @Test
    @Transactional
    void putNonExistingDvd() throws Exception {
        int databaseSizeBeforeUpdate = dvdRepository.findAll().size();
        dvd.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDvdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dvd.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dvd))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dvd in the database
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDvd() throws Exception {
        int databaseSizeBeforeUpdate = dvdRepository.findAll().size();
        dvd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDvdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dvd))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dvd in the database
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDvd() throws Exception {
        int databaseSizeBeforeUpdate = dvdRepository.findAll().size();
        dvd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDvdMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dvd))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dvd in the database
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDvdWithPatch() throws Exception {
        // Initialize the database
        dvdRepository.saveAndFlush(dvd);

        int databaseSizeBeforeUpdate = dvdRepository.findAll().size();

        // Update the dvd using partial update
        Dvd partialUpdatedDvd = new Dvd();
        partialUpdatedDvd.setId(dvd.getId());

        partialUpdatedDvd
            .releaseYear(UPDATED_RELEASE_YEAR)
            .discCount(UPDATED_DISC_COUNT)
            .format(UPDATED_FORMAT)
            .lang(UPDATED_LANG)
            .state(UPDATED_STATE)
            .added(UPDATED_ADDED);

        restDvdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDvd.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDvd))
            )
            .andExpect(status().isOk());

        // Validate the Dvd in the database
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeUpdate);
        Dvd testDvd = dvdList.get(dvdList.size() - 1);
        assertThat(testDvd.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDvd.getPerformer()).isEqualTo(DEFAULT_PERFORMER);
        assertThat(testDvd.getReleaseYear()).isEqualTo(UPDATED_RELEASE_YEAR);
        assertThat(testDvd.getDiscCount()).isEqualTo(UPDATED_DISC_COUNT);
        assertThat(testDvd.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testDvd.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testDvd.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testDvd.getAdded()).isEqualTo(UPDATED_ADDED);
    }

    @Test
    @Transactional
    void fullUpdateDvdWithPatch() throws Exception {
        // Initialize the database
        dvdRepository.saveAndFlush(dvd);

        int databaseSizeBeforeUpdate = dvdRepository.findAll().size();

        // Update the dvd using partial update
        Dvd partialUpdatedDvd = new Dvd();
        partialUpdatedDvd.setId(dvd.getId());

        partialUpdatedDvd
            .name(UPDATED_NAME)
            .performer(UPDATED_PERFORMER)
            .releaseYear(UPDATED_RELEASE_YEAR)
            .discCount(UPDATED_DISC_COUNT)
            .format(UPDATED_FORMAT)
            .lang(UPDATED_LANG)
            .state(UPDATED_STATE)
            .added(UPDATED_ADDED);

        restDvdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDvd.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDvd))
            )
            .andExpect(status().isOk());

        // Validate the Dvd in the database
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeUpdate);
        Dvd testDvd = dvdList.get(dvdList.size() - 1);
        assertThat(testDvd.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDvd.getPerformer()).isEqualTo(UPDATED_PERFORMER);
        assertThat(testDvd.getReleaseYear()).isEqualTo(UPDATED_RELEASE_YEAR);
        assertThat(testDvd.getDiscCount()).isEqualTo(UPDATED_DISC_COUNT);
        assertThat(testDvd.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testDvd.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testDvd.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testDvd.getAdded()).isEqualTo(UPDATED_ADDED);
    }

    @Test
    @Transactional
    void patchNonExistingDvd() throws Exception {
        int databaseSizeBeforeUpdate = dvdRepository.findAll().size();
        dvd.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDvdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dvd.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dvd))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dvd in the database
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDvd() throws Exception {
        int databaseSizeBeforeUpdate = dvdRepository.findAll().size();
        dvd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDvdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dvd))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dvd in the database
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDvd() throws Exception {
        int databaseSizeBeforeUpdate = dvdRepository.findAll().size();
        dvd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDvdMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dvd))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dvd in the database
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDvd() throws Exception {
        // Initialize the database
        dvdRepository.saveAndFlush(dvd);

        int databaseSizeBeforeDelete = dvdRepository.findAll().size();

        // Delete the dvd
        restDvdMockMvc
            .perform(delete(ENTITY_API_URL_ID, dvd.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
