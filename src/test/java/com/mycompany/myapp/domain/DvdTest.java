package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DvdTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dvd.class);
        Dvd dvd1 = new Dvd();
        dvd1.setId(1L);
        Dvd dvd2 = new Dvd();
        dvd2.setId(dvd1.getId());
        assertThat(dvd1).isEqualTo(dvd2);
        dvd2.setId(2L);
        assertThat(dvd1).isNotEqualTo(dvd2);
        dvd1.setId(null);
        assertThat(dvd1).isNotEqualTo(dvd2);
    }
}
