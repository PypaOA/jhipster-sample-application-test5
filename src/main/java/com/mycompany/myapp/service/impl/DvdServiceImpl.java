package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Dvd;
import com.mycompany.myapp.repository.DvdRepository;
import com.mycompany.myapp.service.DvdService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dvd}.
 */
@Service
@Transactional
public class DvdServiceImpl implements DvdService {

    private final Logger log = LoggerFactory.getLogger(DvdServiceImpl.class);

    private final DvdRepository dvdRepository;

    public DvdServiceImpl(DvdRepository dvdRepository) {
        this.dvdRepository = dvdRepository;
    }

    @Override
    public Dvd save(Dvd dvd) {
        log.debug("Request to save Dvd : {}", dvd);
        return dvdRepository.save(dvd);
    }

    @Override
    public Optional<Dvd> partialUpdate(Dvd dvd) {
        log.debug("Request to partially update Dvd : {}", dvd);

        return dvdRepository
            .findById(dvd.getId())
            .map(existingDvd -> {
                if (dvd.getName() != null) {
                    existingDvd.setName(dvd.getName());
                }
                if (dvd.getPerformer() != null) {
                    existingDvd.setPerformer(dvd.getPerformer());
                }
                if (dvd.getReleaseYear() != null) {
                    existingDvd.setReleaseYear(dvd.getReleaseYear());
                }
                if (dvd.getDiscCount() != null) {
                    existingDvd.setDiscCount(dvd.getDiscCount());
                }
                if (dvd.getFormat() != null) {
                    existingDvd.setFormat(dvd.getFormat());
                }
                if (dvd.getLang() != null) {
                    existingDvd.setLang(dvd.getLang());
                }
                if (dvd.getState() != null) {
                    existingDvd.setState(dvd.getState());
                }
                if (dvd.getAdded() != null) {
                    existingDvd.setAdded(dvd.getAdded());
                }

                return existingDvd;
            })
            .map(dvdRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Dvd> findAll(Pageable pageable) {
        log.debug("Request to get all Dvds");
        return dvdRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Dvd> findOne(Long id) {
        log.debug("Request to get Dvd : {}", id);
        return dvdRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dvd : {}", id);
        dvdRepository.deleteById(id);
    }
}
