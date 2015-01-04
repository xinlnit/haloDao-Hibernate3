package gen;

import javax.annotation.Resource;

import org.junit.Test;

import com.ht.codegen.service.GenService;
import com.ht.utils.junit.BaseServiceTestCase;

public class Codegen extends BaseServiceTestCase {
	@Resource
	private GenService genService;

	@Test
	public void testGen() {
		   genService.gen(true);
		}

	
	

}
