package it.attsd.deepsky.unit.repository;

import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.repository.ConstellationRepository;
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
public class ConstellationRepositoryTest {

	@Autowired
	private ConstellationRepository constellationRepository;

	@Autowired
	private TestEntityManager entityManager;

	private final String ORION = "orion";
	private final String SCORPIUS = "scorpius";

	@Test
	public void testJpaMapping() {
		Constellation orionSaved = entityManager.persistFlushFind(new Constellation(ORION));
		assertNotNull(orionSaved);
		assertThat(orionSaved.getId()).isPositive();
		assertThat(orionSaved.getName()).isEqualTo(ORION);
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
//	
//	@Test
//	public void testGetAllConstellationsWhenDBIsEmpty() {
//		assertThat(constellationRepository.findAll()).isEmpty();
//	}
//
//	@Test
//	public void testGetAllConstellationsWhenContainsMoreThanOne() {
//		Constellation orionSaved = entityManager.persistAndFlush(new Constellation(ORION));
////		assertNotNull(orionSaved);
////		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
////		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);
//
//		Constellation scorpiusSaved = entityManager.persistAndFlush(new Constellation(SCORPIUS));
////		assertNotNull(scorpiusSaved);
////		assertThat(scorpiusSaved).hasFieldOrPropertyWithValue("id", 2L);
////		assertThat(scorpiusSaved).hasFieldOrPropertyWithValue("name", SCORPIUS);
//
//		assertThat(constellationRepository.findAll()).containsExactly(orionSaved, scorpiusSaved);
//	}
//
//	@Test
//	public void testGetConstellationByIdWhenExists() {
//		Constellation orionSaved = entityManager.persistAndFlush(new Constellation(ORION));
//		assertNotNull(orionSaved);
//		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
//		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);
//
//		assertThat(constellationRepository.findById(1L)).isEqualTo(Optional.of(orionSaved));
//	}
//
//	@Test
//	public void testGetConstellationByIdWhenNotExists() {
//		assertThrows(NoSuchElementException.class, () -> constellationRepository.findById(1L).get());
//	}

	@Test
	public void testGetConstellationByNameWhenIsPresent() {
		Constellation orionSaved = entityManager.persistAndFlush(new Constellation(ORION));
//		assertNotNull(orionSaved);
//		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
//		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);

		assertThat(constellationRepository.findByName(ORION)).isEqualTo(orionSaved);
	}

	@Test
	public void testGetConstellationByNameWhenIsNotPresent() {
		assertThat(constellationRepository.findByName(ORION)).isNull();
	}

//	@Test
//	public void testAddConstellationWhenNotExists() {
//		Constellation orionSaved = constellationRepository.saveAndFlush(new Constellation(ORION));
//		assertNotNull(orionSaved);
//		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
//		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);
//	}
//
//	@Test
//	public void testAddConstellationWhenAlreadyExistsWithSameName() {
//		Constellation orionSaved = entityManager.persistAndFlush(new Constellation(ORION));
//		assertNotNull(orionSaved);
//		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
//		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);
//
//		assertThrows(DataIntegrityViolationException.class,
//				() -> constellationRepository.saveAndFlush(new Constellation(ORION)));
//	}
//
//	@Test
//	public void testUpdateConstellationWhenExists() {
//		String nameChanged = ORION + " changed";
//
//		Constellation orionSaved = entityManager.persistAndFlush(new Constellation(ORION));
//		assertNotNull(orionSaved);
//		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
//		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);
//
//		orionSaved.setName(nameChanged);
//
//		Constellation orionUpdated = constellationRepository.saveAndFlush(orionSaved);
//		assertThat(orionUpdated).isNotNull();
//		assertThat(orionUpdated).hasFieldOrPropertyWithValue("id", 1L);
//		assertThat(orionUpdated).hasFieldOrPropertyWithValue("name", nameChanged);
//	}
//
////	@Test
////	public void testUpdateConstellationWhenNotExists() {
////		String nameChanged = ORION + " changed";
////		
////		Constellation orionExisting = entityManager.find(Constellation.class, 1L);
////		assertNull(orionExisting);
////		
//////		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
//////		assertNotNull(orionSaved);
//////		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
//////		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);
////		
////		orion.setId(1L);
////		orion.setName(nameChanged);
////		Constellation orionUpdated = constellationRepository.save(orion);
////		assertNotNull(orionUpdated);
////		assertThat(orionUpdated).hasFieldOrPropertyWithValue("id", 1L);
////		assertThat(orionUpdated).hasFieldOrPropertyWithValue("name", nameChanged);
////	}
//
//	@Test
//	public void testDeleteConstellationWhenExists() {
//		Constellation orionSaved = entityManager.persistAndFlush(new Constellation(ORION));
//		assertNotNull(orionSaved);
//		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
//		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);
//
//		Constellation orionExisting = entityManager.find(Constellation.class, 1L);
//		assertNotNull(orionSaved);
//		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
//		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);
//
//		constellationRepository.delete(orionExisting);
//
//		assertThat(entityManager.find(Constellation.class, 1L)).isNull();
//	}
//
//	@Test
//	public void testDeleteConstellationWhenNotExists() {
//		assertThrows(EmptyResultDataAccessException.class, () -> constellationRepository.deleteById(1L));
//	}

}
