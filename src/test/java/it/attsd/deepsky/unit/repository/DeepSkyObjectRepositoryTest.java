package it.attsd.deepsky.unit.repository;

import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.model.DeepSkyObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
public class DeepSkyObjectRepositoryTest {

    @Autowired
    private DeepSkyObjectRepository deepSkyObjectRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final String ORION = "orion";
    private final String M42 = "m42";
    private final String M43 = "m43";

    @Test
    public void testJpaMapping() {
        Constellation orionSaved = entityManager.persistFlushFind(new Constellation(ORION));

        DeepSkyObject m42Saved = entityManager.persistFlushFind(new DeepSkyObject(M42, orionSaved));
        assertNotNull(m42Saved);
        assertThat(m42Saved.getId()).isPositive();
        assertThat(m42Saved.getName()).isEqualTo(M42);
        assertThat(m42Saved.getConstellation()).isEqualTo(orionSaved);
    }

    @Test
    public void testEqualsObjectsAreEquals() {
        DeepSkyObject dso1 = new DeepSkyObject(1L, M42, new Constellation(1L, ORION));
        DeepSkyObject dso2 = new DeepSkyObject(1L, M42, new Constellation(1L, ORION));

        assertEquals(dso1, dso2);
    }

    @Test
    public void testEqualsObjectsAreDifferent() {
        DeepSkyObject dso1 = new DeepSkyObject(1L, M42, new Constellation(1L, ORION));
        DeepSkyObject dso2 = new DeepSkyObject(2L, M42, new Constellation(1L, ORION));

        assertNotEquals(dso1, dso2);
    }

    @Test
    public void testEqualsWithItself() {
        DeepSkyObject dso1 = new DeepSkyObject(1L, M42, new Constellation(1L, ORION));

        assertEquals(dso1, dso1);
    }

    @Test
    public void testEqualsWithNull() {
        DeepSkyObject dso1 = new DeepSkyObject(1L, M42, new Constellation(1L, ORION));

        assertNotEquals(dso1, null);
    }

    @Test
    public void testEqualsWithDifferentClass() {
        DeepSkyObject dso1 = new DeepSkyObject(1L, M42, new Constellation(1L, ORION));

        assertNotEquals(dso1, new String(""));
    }


    @Test
    public void testGetDeepSkyObjectByNameWhenIsPresent() {
        Constellation orionSaved = entityManager.persistAndFlush(new Constellation(ORION));

        DeepSkyObject m42Saved = entityManager.persistFlushFind(new DeepSkyObject(M42, orionSaved));

        assertThat(deepSkyObjectRepository.findByName(M42)).isEqualTo(m42Saved);
    }

    @Test
    public void testGetDeepSkyObjectByNameWhenIsNotPresent() {
        assertThat(deepSkyObjectRepository.findByName(ORION)).isNull();
    }

}
