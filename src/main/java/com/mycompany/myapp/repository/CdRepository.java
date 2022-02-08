package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cd;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cd entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CdRepository extends JpaRepository<Cd, Long> {}
