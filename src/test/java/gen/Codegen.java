package gen;

import javax.annotation.Resource;

import org.junit.Test;

import com.google.gson.Gson;
import com.ht.codegen.entity.ViewTable;
import com.ht.codegen.service.ViewTableService;
import com.ht.utils.junit.BaseServiceTestCase;

public class Codegen extends BaseServiceTestCase{
	@Resource
  private ViewTableService viewTableService;
  @Test
  public void testFindViewTableByName(){
	 ViewTable viewTable = viewTableService.findViewTableByName("base_company");
	 System.out.println(viewTable.getCreateTime());
    System.out.println(new Gson().toJson(viewTable));
  }

}
