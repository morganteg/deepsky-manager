package it.attsd.deepsky.unit.repository;

import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.repository.ConstellationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class ConstellationRepositoryTest {

    @Autowired
    private ConstellationRepository constellationRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final String ORION = "orion";

    @Test
    public void testJpaMapping() {
        Constellation orionSaved = entityManager.persistFlushFind(new Constellation(ORION));
        assertNotNull(orionSaved);
        assertThat(orionSaved.getId()).isPositive();
        assertThat(orionSaved.getName()).isEqualTo(ORION);
    }

    @Test
    public void testConstructor() {
        Constellation orion = new Constellation(1L, ORION);

        assertThat(orion.getId()).isEqualTo(1L);
    }

    @Test
    public void testEqualsObjectsAreEquals() {
        Constellation orion1 = new Constellation(1L, ORION);
        Constellation orion2 = new Constellation(1L, ORION);

        assertEquals(orion1, orion2);
    }

    @Test
    public void testEqualsObjectsAreDifferent() {
        Constellation orion1 = new Constellation(1L, ORION);
        Constellation orion2 = new Constellation(2L, ORION);

        assertNotEquals(orion1, orion2);
    }

    @Test
    public void testEqualsWithItself() {
        Constellation orion1 = new Constellation(1L, ORION);

        assertEquals(orion1, orion1);
    }

    @Test
    public void testEqualsWithNull() {
        Constellation orion1 = new Constellation(1L, ORION);

        assertNotEquals(orion1, null);
    }

    @Test
    public void testEqualsWithDifferentClass() {
        Constellation orion1 = new Constellation(1L, ORION);

        assertNotEquals(orion1, new String(""));
    }

    @Test
    public void testEqualsWithDifferentAttributes() {
        Constellation orion1 = new Constellation(1L, ORION);
        Constellation orion2 = new Constellation(2L, ORION);

        assertNotEquals(orion1, orion2);
    }

    @Test
    public void testGetConstellationByNameWhenIsPresent() {
        Constellation orionSaved = entityManager.persistAndFlush(new Constellation(ORION));

        assertThat(constellationRepository.findByName(ORION)).isEqualTo(orionSaved);
    }

    @Test
    public void testGetConstellationByNameWhenIsNotPresent() {
        assertThat(constellationRepository.findByName(ORION)).isNull();
    }

}
