package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Dvd;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dvd entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DvdRepository extends JpaRepository<Dvd, Long> {}
