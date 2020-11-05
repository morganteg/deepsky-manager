package it.attsd.deepsky;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class DeepskyAppApplicationTest {

	@Test
	public void testApplicationStart() {
		DeepskyAppApplication.main(new String[] {});
	}

}
