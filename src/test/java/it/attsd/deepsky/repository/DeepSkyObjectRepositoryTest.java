package it.attsd.deepsky.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.model.DeepSkyObject;

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

//	private final Constellation orion = new Constellation(1L, "orion");
//	private final DeepSkyObjectType nebula = new DeepSkyObjectType("nebula");
//	private final DeepSkyObject m42 = new DeepSkyObject(1L, M42, orion, nebula);
//	private final DeepSkyObject m43 = new DeepSkyObject(2, M43, orion, nebula);

	@Test
	public void testJpaMapping() {
		Constellation orionSaved = entityManager.persistFlushFind(new Constellation(ORION));

		DeepSkyObject m42Saved = entityManager.persistFlushFind(new DeepSkyObject(M42, orionSaved));
		assertNotNull(m42Saved);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("name", M42);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("constellation", orionSaved);
	}
	
	@Test
	public void testGetAllDeepSkyObjectsWhenDBIsEmpty() {
		assertThat(deepSkyObjectRepository.findAll()).isEmpty();
	}
	
	@Test
	public void testGetAllDeepSkyObjectsWhenContainsMoreThanOne() {
		Constellation orionSaved = entityManager.persistAndFlush(new Constellation(ORION));
		assertNotNull(orionSaved);
		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);
		
		DeepSkyObject m42Saved = entityManager.persistFlushFind(new DeepSkyObject(M42, orionSaved));
		assertNotNull(m42Saved);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("name", M42);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("constellation", orionSaved);
		
		DeepSkyObject m43Saved = entityManager.persistFlushFind(new DeepSkyObject(M43, orionSaved));
		assertNotNull(m43Saved);
		assertThat(m43Saved).hasFieldOrPropertyWithValue("id", 2L);
		assertThat(m43Saved).hasFieldOrPropertyWithValue("name", M43);
		assertThat(m43Saved).hasFieldOrPropertyWithValue("constellation", orionSaved);

		assertThat(deepSkyObjectRepository.findAll()).containsExactly(m42Saved, m43Saved);
	}
	
	@Test
	public void testGetDeepSkyObjectByIdWhenExists() {
		Constellation orionSaved = entityManager.persistAndFlush(new Constellation(ORION));
		assertNotNull(orionSaved);
		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);
		
		DeepSkyObject m42Saved = entityManager.persistFlushFind(new DeepSkyObject(M42, orionSaved));
		assertNotNull(m42Saved);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("name", M42);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("constellation", orionSaved);

		assertThat(deepSkyObjectRepository.findById(1L)).isEqualTo(Optional.of(m42Saved));
	}
	
	@Test
	public void testGetDeepSkyObjectByIdWhenNotExists() {
		assertThrows(NoSuchElementException.class, () -> deepSkyObjectRepository.findById(1L).get());
	}
	
	@Test
	public void testGetDeepSkyObjectByNameWhenIsPresent() {
		Constellation orionSaved = entityManager.persistAndFlush(new Constellation(ORION));
		assertNotNull(orionSaved);
		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);
		
		DeepSkyObject m42Saved = entityManager.persistFlushFind(new DeepSkyObject(M42, orionSaved));
		assertNotNull(m42Saved);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("name", M42);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("constellation", orionSaved);

		assertThat(deepSkyObjectRepository.findByName(M42)).isEqualTo(m42Saved);
	}
	
	@Test
	public void testGetDeepSkyObjectByNameWhenIsNotPresent() {
		assertThat(deepSkyObjectRepository.findByName(ORION)).isNull();
	}
	
	@Test
	public void testAddDeepSkyObjectWhenNotExists() {
		Constellation orionSaved = entityManager.persistAndFlush(new Constellation(ORION));
		assertNotNull(orionSaved);
		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);
		
		DeepSkyObject m42Saved = deepSkyObjectRepository.saveAndFlush(new DeepSkyObject(M42, orionSaved));
		assertNotNull(m42Saved);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("name", M42);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("constellation", orionSaved);
	}
	
	@Test
	public void testAddConstellationWhenAlreadyExistsWithSameName() {
		Constellation orionSaved = entityManager.persistAndFlush(new Constellation(ORION));
		assertNotNull(orionSaved);
		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);
		
		DeepSkyObject m42Saved = entityManager.persistAndFlush(new DeepSkyObject(M42, orionSaved));
		assertNotNull(m42Saved);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("name", M42);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("constellation", orionSaved);

		assertThrows(DataIntegrityViolationException.class,
				() -> deepSkyObjectRepository.saveAndFlush(new DeepSkyObject(M42, orionSaved)));
	}
	
	@Test
	public void testUpdateDeepSkyObjectWhenExists() {
		String nameChanged = M42 + " changed";

		Constellation orionSaved = entityManager.persistAndFlush(new Constellation(ORION));
		assertNotNull(orionSaved);
		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);
		
		DeepSkyObject m42Saved = entityManager.persistAndFlush(new DeepSkyObject(M42, orionSaved));
		assertNotNull(m42Saved);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("name", M42);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("constellation", orionSaved);

		m42Saved.setName(nameChanged);

		DeepSkyObject m42Updated = deepSkyObjectRepository.saveAndFlush(m42Saved);
		assertNotNull(m42Updated);
		assertThat(m42Updated).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(m42Updated).hasFieldOrPropertyWithValue("name", nameChanged);
		assertThat(m42Updated).hasFieldOrPropertyWithValue("constellation", orionSaved);
	}
	
	@Test
	public void testDeleteDeepSkyObjectWhenExists() {
		Constellation orionSaved = entityManager.persistAndFlush(new Constellation(ORION));
		assertNotNull(orionSaved);
		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);
		
		DeepSkyObject m42Saved = entityManager.persistAndFlush(new DeepSkyObject(M42, orionSaved));
		assertNotNull(m42Saved);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("name", M42);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("constellation", orionSaved);

		DeepSkyObject m42Existing = entityManager.find(DeepSkyObject.class, 1L);
		assertNotNull(m42Saved);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("name", M42);
		assertThat(m42Saved).hasFieldOrPropertyWithValue("constellation", orionSaved);

		deepSkyObjectRepository.delete(m42Existing);

		assertThat(entityManager.find(DeepSkyObject.class, 1L)).isNull();
	}

	@Test
	public void testDeleteDeepSkyObjectWhenNotExists() {
		assertThrows(EmptyResultDataAccessException.class, () -> deepSkyObjectRepository.deleteById(1L));
	}

}
