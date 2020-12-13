package it.attsd.deepsky;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeepskyAppApplicationTest {

	@Test
	public void testApplicationStart() {
		DeepskyAppApplication.main(new String[] {});
	}

}
