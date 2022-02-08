package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CdTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cd.class);
        Cd cd1 = new Cd();
        cd1.setId(1L);
        Cd cd2 = new Cd();
        cd2.setId(cd1.getId());
        assertThat(cd1).isEqualTo(cd2);
        cd2.setId(2L);
        assertThat(cd1).isNotEqualTo(cd2);
        cd1.setId(null);
        assertThat(cd1).isNotEqualTo(cd2);
    }
}
