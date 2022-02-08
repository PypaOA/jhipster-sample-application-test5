package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Cd;
import com.mycompany.myapp.repository.CdRepository;
import com.mycompany.myapp.service.CdService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cd}.
 */
@Service
@Transactional
public class CdServiceImpl implements CdService {

    private final Logger log = LoggerFactory.getLogger(CdServiceImpl.class);

    private final CdRepository cdRepository;

    public CdServiceImpl(CdRepository cdRepository) {
        this.cdRepository = cdRepository;
    }

    @Override
    public Cd save(Cd cd) {
        log.debug("Request to save Cd : {}", cd);
        return cdRepository.save(cd);
    }

    @Override
    public Optional<Cd> partialUpdate(Cd cd) {
        log.debug("Request to partially update Cd : {}", cd);

        return cdRepository
            .findById(cd.getId())
            .map(existingCd -> {
                if (cd.getName() != null) {
                    existingCd.setName(cd.getName());
                }
                if (cd.getPerformer() != null) {
                    existingCd.setPerformer(cd.getPerformer());
                }
                if (cd.getReleaseYear() != null) {
                    existingCd.setReleaseYear(cd.getReleaseYear());
                }
                if (cd.getDiscCount() != null) {
                    existingCd.setDiscCount(cd.getDiscCount());
                }
                if (cd.getMedium() != null) {
                    existingCd.setMedium(cd.getMedium());
                }
                if (cd.getLabel() != null) {
                    existingCd.setLabel(cd.getLabel());
                }
                if (cd.getState() != null) {
                    existingCd.setState(cd.getState());
                }
                if (cd.getAdded() != null) {
                    existingCd.setAdded(cd.getAdded());
                }

                return existingCd;
            })
            .map(cdRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cd> findAll(Pageable pageable) {
        log.debug("Request to get all Cds");
        return cdRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cd> findOne(Long id) {
        log.debug("Request to get Cd : {}", id);
        return cdRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cd : {}", id);
        cdRepository.deleteById(id);
    }
}
