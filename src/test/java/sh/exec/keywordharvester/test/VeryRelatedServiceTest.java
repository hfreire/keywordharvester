package sh.exec.keywordharvester.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import sh.exec.keywordharvester.service.KeywordHarvesterService;
import sh.exec.keywordharvester.service.VeryRelatedService;

@RunWith(MockitoJUnitRunner.class)
public class VeryRelatedServiceTest {
	@InjectMocks
	private VeryRelatedService veryRelatedService;
	
	@Before
    public void init() {
    }
 
    @Test
    public void testIsSupervisorResponsibleForUserTrue() {
    }
}
