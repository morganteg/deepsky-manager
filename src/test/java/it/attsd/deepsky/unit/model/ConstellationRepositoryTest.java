package it.attsd.deepsky.unit.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.model.ConstellationRepository;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
//@ContextConfiguration(classes=ConstellationRepository.class)
public class ConstellationRepositoryTest {

//	@Mock
//	private EntityManager entityManager;

//	@Mock
//	private Query query;
	
//	@Autowired
//	private TestEntityManager entityManager;
	
	@Autowired
	private ConstellationRepository constellationRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	private final String ORION = "orion";

//	@Before
//	public void setUp() throws Exception {
//		MockitoAnnotations.initMocks(this);
//	}

//	@Test
//	public void testEmptyConstellationTable() {
//		String queryString = String.format("DELETE FROM %s", Constellation.class.getName());
//
//		when(entityManager.createQuery(queryString)).thenReturn(query);
//		constellationRepository.emptyTable();
//
//		verify(entityManager).createQuery(queryString);
//		verify(query).executeUpdate();
//		verify(entityManager).flush();
//	}

	@Test
	public void testGetAllConstellationsWhenDBIsEmpty() {
//		entityManager.find(Constellation.class, 1L);
		
//		String queryString = String.format("SELECT t FROM %s t", Constellation.class.getName());
//		List<Constellation> constellations = new ArrayList<Constellation>();
//
//		when(entityManager.createQuery(queryString)).thenReturn(query);
//		when(query.getResultList()).thenReturn(constellations);
//		List<Constellation> constellationsResult = constellationRepository.findAll();
//
//		verify(entityManager).createQuery(queryString);
//		verify(query).getResultList();

		assertThat(constellationRepository.findAll()).isEmpty();
//		
//		verify(query).getResultList();
	}

//	@Test
//	public void testGetAllConstellationsWhenContainsTwo() {
//		Constellation orion = new Constellation(1L, ORION);
//		Constellation scorpion = new Constellation(1L, "scorpion");
//
//		List<Constellation> constellations = new ArrayList<Constellation>();
//		constellations.add(orion);
//		constellations.add(scorpion);
//
//		String queryString = String.format("SELECT t FROM %s t", Constellation.class.getName());
//
//		when(entityManager.createQuery(queryString)).thenReturn(query);
//		when(query.getResultList()).thenReturn(constellations);
//		List<Constellation> constellationsResult = constellationRepository.findAll();
//
//		verify(entityManager).createQuery(queryString);
//		verify(query).getResultList();
//
//		assertEquals(2, constellationsResult.size());
//	}
//
//	@Test
//	public void testGetConstellationByIdWhenIdIsPresent() {
//		Constellation orion = new Constellation(1L, ORION);
//
//		when(entityManager.find(Constellation.class, 1L)).thenReturn(orion);
//
//		Constellation constellationFound = constellationRepository.findById(1L);
//
//		verify(entityManager).find(Constellation.class, 1L);
//
//		assertNotNull(constellationFound);
//	}
//
//	@Test
//	public void testGetConstellationByIdWhenIdIsNotPresent() {
//		when(entityManager.find(Constellation.class, 1L)).thenReturn(null);
//
//		Constellation constellationFound = constellationRepository.findById(1L);
//
//		verify(entityManager).find(Constellation.class, 1L);
//
//		assertNull(constellationFound);
//	}
//
	

	@Test
	public void testGetConstellationByNameWhenIsNotPresent() {
		assertThat(constellationRepository.findByName(ORION)).isNull();
	}

	@Test
	public void testGetConstellationByNameWhenIsPresent() {
		Constellation orionSaved = entityManager.persistAndFlush(new Constellation(ORION));
		assertThat(constellationRepository.findByName(ORION)).isEqualTo(orionSaved);
	}
	
	@Test
	public void testAddConstellationWhenNotExists() {
		Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
		assertNotNull(orionSaved);
		
		assertThat(orionSaved).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(orionSaved).hasFieldOrPropertyWithValue("name", ORION);
	}
//
//	@Test
//	public void testAddConstellationWhenAlreadyExists() throws ConstellationAlreadyExistsException {
//		Constellation orion = new Constellation(ORION);
//
//		IllegalStateException exc = new IllegalStateException();
//		doThrow(exc).when(entityManager).persist(orion);
//
//		assertThrows(IllegalStateException.class, () -> constellationRepository.save(orion));
//	}
//	
//	@Test
//	public void testAddConstellationWithPersistenceException() throws ConstellationAlreadyExistsException {
//		Constellation orion = new Constellation(ORION);
//
//		PersistenceException exc = new PersistenceException();
//		doThrow(exc).when(entityManager).persist(orion);
//
//		assertThrows(PersistenceException.class, () -> constellationRepository.save(orion));
//	}
//
//	@Test
//	public void testUpdateConstellationWhenExists() throws ConstellationAlreadyExistsException {
//		Constellation orion = new Constellation(1L, ORION);
//
//		when(entityManager.find(Constellation.class, 1L)).thenReturn(orion);
//
//		Constellation orionFound = constellationRepository.findById(1L);
//
//		verify(entityManager).find(Constellation.class, 1L);
//
//		String nameChanged = orionFound.getName() + " changed";
//		orionFound.setName(nameChanged);
//
//		Constellation orionChangedMock = new Constellation(1L, nameChanged);
//
//		when(entityManager.merge(orionFound)).thenReturn(orionChangedMock);
//
//		Constellation orionChanged = constellationRepository.update(orionFound);
//		assertNotNull(orionChanged);
//		verify(entityManager).merge(orionFound);
//		verify(entityManager).flush();
//	}
//	
//	@Test
//	public void testDeleteConstellationWhenExists() {
//		Constellation orion = new Constellation(1L, ORION);
//		when(entityManager.find(Constellation.class, 1L)).thenReturn(orion);
//
//		constellationRepository.delete(1L);
//
//		verify(entityManager).remove(orion);
//		verify(entityManager).flush();
//	}
//
//	@Test
//	public void testDeleteConstellationWhenNotExists() {
//		when(entityManager.find(Constellation.class, 1L)).thenReturn(null);
//
//		constellationRepository.delete(1L);
//
//		verify(entityManager, times(0)).remove(1L);
//	}

}
