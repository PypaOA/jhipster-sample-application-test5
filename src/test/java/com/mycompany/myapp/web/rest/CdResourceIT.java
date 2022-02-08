package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cd;
import com.mycompany.myapp.domain.enumeration.State;
import com.mycompany.myapp.repository.CdRepository;
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
 * Integration tests for the {@link CdResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CdResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PERFORMER = "AAAAAAAAAA";
    private static final String UPDATED_PERFORMER = "BBBBBBBBBB";

    private static final String DEFAULT_RELEASE_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_RELEASE_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_DISC_COUNT = "AAAAAAAAAA";
    private static final String UPDATED_DISC_COUNT = "BBBBBBBBBB";

    private static final String DEFAULT_MEDIUM = "AAAAAAAAAA";
    private static final String UPDATED_MEDIUM = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final State DEFAULT_STATE = State.OK;
    private static final State UPDATED_STATE = State.AWAY;

    private static final Instant DEFAULT_ADDED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ADDED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/cds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CdRepository cdRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCdMockMvc;

    private Cd cd;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cd createEntity(EntityManager em) {
        Cd cd = new Cd()
            .name(DEFAULT_NAME)
            .performer(DEFAULT_PERFORMER)
            .releaseYear(DEFAULT_RELEASE_YEAR)
            .discCount(DEFAULT_DISC_COUNT)
            .medium(DEFAULT_MEDIUM)
            .label(DEFAULT_LABEL)
            .state(DEFAULT_STATE)
            .added(DEFAULT_ADDED);
        return cd;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cd createUpdatedEntity(EntityManager em) {
        Cd cd = new Cd()
            .name(UPDATED_NAME)
            .performer(UPDATED_PERFORMER)
            .releaseYear(UPDATED_RELEASE_YEAR)
            .discCount(UPDATED_DISC_COUNT)
            .medium(UPDATED_MEDIUM)
            .label(UPDATED_LABEL)
            .state(UPDATED_STATE)
            .added(UPDATED_ADDED);
        return cd;
    }

    @BeforeEach
    public void initTest() {
        cd = createEntity(em);
    }

    @Test
    @Transactional
    void createCd() throws Exception {
        int databaseSizeBeforeCreate = cdRepository.findAll().size();
        // Create the Cd
        restCdMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cd))
            )
            .andExpect(status().isCreated());

        // Validate the Cd in the database
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeCreate + 1);
        Cd testCd = cdList.get(cdList.size() - 1);
        assertThat(testCd.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCd.getPerformer()).isEqualTo(DEFAULT_PERFORMER);
        assertThat(testCd.getReleaseYear()).isEqualTo(DEFAULT_RELEASE_YEAR);
        assertThat(testCd.getDiscCount()).isEqualTo(DEFAULT_DISC_COUNT);
        assertThat(testCd.getMedium()).isEqualTo(DEFAULT_MEDIUM);
        assertThat(testCd.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testCd.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testCd.getAdded()).isEqualTo(DEFAULT_ADDED);
    }

    @Test
    @Transactional
    void createCdWithExistingId() throws Exception {
        // Create the Cd with an existing ID
        cd.setId(1L);

        int databaseSizeBeforeCreate = cdRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCdMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cd))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cd in the database
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cdRepository.findAll().size();
        // set the field null
        cd.setName(null);

        // Create the Cd, which fails.

        restCdMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cd))
            )
            .andExpect(status().isBadRequest());

        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCds() throws Exception {
        // Initialize the database
        cdRepository.saveAndFlush(cd);

        // Get all the cdList
        restCdMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cd.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].performer").value(hasItem(DEFAULT_PERFORMER)))
            .andExpect(jsonPath("$.[*].releaseYear").value(hasItem(DEFAULT_RELEASE_YEAR)))
            .andExpect(jsonPath("$.[*].discCount").value(hasItem(DEFAULT_DISC_COUNT)))
            .andExpect(jsonPath("$.[*].medium").value(hasItem(DEFAULT_MEDIUM)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].added").value(hasItem(DEFAULT_ADDED.toString())));
    }

    @Test
    @Transactional
    void getCd() throws Exception {
        // Initialize the database
        cdRepository.saveAndFlush(cd);

        // Get the cd
        restCdMockMvc
            .perform(get(ENTITY_API_URL_ID, cd.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cd.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.performer").value(DEFAULT_PERFORMER))
            .andExpect(jsonPath("$.releaseYear").value(DEFAULT_RELEASE_YEAR))
            .andExpect(jsonPath("$.discCount").value(DEFAULT_DISC_COUNT))
            .andExpect(jsonPath("$.medium").value(DEFAULT_MEDIUM))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.added").value(DEFAULT_ADDED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCd() throws Exception {
        // Get the cd
        restCdMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCd() throws Exception {
        // Initialize the database
        cdRepository.saveAndFlush(cd);

        int databaseSizeBeforeUpdate = cdRepository.findAll().size();

        // Update the cd
        Cd updatedCd = cdRepository.findById(cd.getId()).get();
        // Disconnect from session so that the updates on updatedCd are not directly saved in db
        em.detach(updatedCd);
        updatedCd
            .name(UPDATED_NAME)
            .performer(UPDATED_PERFORMER)
            .releaseYear(UPDATED_RELEASE_YEAR)
            .discCount(UPDATED_DISC_COUNT)
            .medium(UPDATED_MEDIUM)
            .label(UPDATED_LABEL)
            .state(UPDATED_STATE)
            .added(UPDATED_ADDED);

        restCdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCd.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCd))
            )
            .andExpect(status().isOk());

        // Validate the Cd in the database
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeUpdate);
        Cd testCd = cdList.get(cdList.size() - 1);
        assertThat(testCd.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCd.getPerformer()).isEqualTo(UPDATED_PERFORMER);
        assertThat(testCd.getReleaseYear()).isEqualTo(UPDATED_RELEASE_YEAR);
        assertThat(testCd.getDiscCount()).isEqualTo(UPDATED_DISC_COUNT);
        assertThat(testCd.getMedium()).isEqualTo(UPDATED_MEDIUM);
        assertThat(testCd.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testCd.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testCd.getAdded()).isEqualTo(UPDATED_ADDED);
    }

    @Test
    @Transactional
    void putNonExistingCd() throws Exception {
        int databaseSizeBeforeUpdate = cdRepository.findAll().size();
        cd.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cd.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cd))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cd in the database
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCd() throws Exception {
        int databaseSizeBeforeUpdate = cdRepository.findAll().size();
        cd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cd))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cd in the database
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCd() throws Exception {
        int databaseSizeBeforeUpdate = cdRepository.findAll().size();
        cd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCdMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cd))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cd in the database
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCdWithPatch() throws Exception {
        // Initialize the database
        cdRepository.saveAndFlush(cd);

        int databaseSizeBeforeUpdate = cdRepository.findAll().size();

        // Update the cd using partial update
        Cd partialUpdatedCd = new Cd();
        partialUpdatedCd.setId(cd.getId());

        partialUpdatedCd.performer(UPDATED_PERFORMER).releaseYear(UPDATED_RELEASE_YEAR).state(UPDATED_STATE);

        restCdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCd.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCd))
            )
            .andExpect(status().isOk());

        // Validate the Cd in the database
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeUpdate);
        Cd testCd = cdList.get(cdList.size() - 1);
        assertThat(testCd.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCd.getPerformer()).isEqualTo(UPDATED_PERFORMER);
        assertThat(testCd.getReleaseYear()).isEqualTo(UPDATED_RELEASE_YEAR);
        assertThat(testCd.getDiscCount()).isEqualTo(DEFAULT_DISC_COUNT);
        assertThat(testCd.getMedium()).isEqualTo(DEFAULT_MEDIUM);
        assertThat(testCd.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testCd.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testCd.getAdded()).isEqualTo(DEFAULT_ADDED);
    }

    @Test
    @Transactional
    void fullUpdateCdWithPatch() throws Exception {
        // Initialize the database
        cdRepository.saveAndFlush(cd);

        int databaseSizeBeforeUpdate = cdRepository.findAll().size();

        // Update the cd using partial update
        Cd partialUpdatedCd = new Cd();
        partialUpdatedCd.setId(cd.getId());

        partialUpdatedCd
            .name(UPDATED_NAME)
            .performer(UPDATED_PERFORMER)
            .releaseYear(UPDATED_RELEASE_YEAR)
            .discCount(UPDATED_DISC_COUNT)
            .medium(UPDATED_MEDIUM)
            .label(UPDATED_LABEL)
            .state(UPDATED_STATE)
            .added(UPDATED_ADDED);

        restCdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCd.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCd))
            )
            .andExpect(status().isOk());

        // Validate the Cd in the database
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeUpdate);
        Cd testCd = cdList.get(cdList.size() - 1);
        assertThat(testCd.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCd.getPerformer()).isEqualTo(UPDATED_PERFORMER);
        assertThat(testCd.getReleaseYear()).isEqualTo(UPDATED_RELEASE_YEAR);
        assertThat(testCd.getDiscCount()).isEqualTo(UPDATED_DISC_COUNT);
        assertThat(testCd.getMedium()).isEqualTo(UPDATED_MEDIUM);
        assertThat(testCd.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testCd.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testCd.getAdded()).isEqualTo(UPDATED_ADDED);
    }

    @Test
    @Transactional
    void patchNonExistingCd() throws Exception {
        int databaseSizeBeforeUpdate = cdRepository.findAll().size();
        cd.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cd.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cd))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cd in the database
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCd() throws Exception {
        int databaseSizeBeforeUpdate = cdRepository.findAll().size();
        cd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cd))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cd in the database
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCd() throws Exception {
        int databaseSizeBeforeUpdate = cdRepository.findAll().size();
        cd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCdMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cd))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cd in the database
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCd() throws Exception {
        // Initialize the database
        cdRepository.saveAndFlush(cd);

        int databaseSizeBeforeDelete = cdRepository.findAll().size();

        // Delete the cd
        restCdMockMvc
            .perform(delete(ENTITY_API_URL_ID, cd.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
