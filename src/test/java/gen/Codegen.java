package gen;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.google.gson.Gson;
import com.ht.codegen.entity.ViewColumn;
import com.ht.codegen.entity.ViewTable;
import com.ht.codegen.service.ViewColumnService;
import com.ht.codegen.service.ViewTableService;
import com.ht.halo.hibernate3.HaloMap;
import com.ht.utils.junit.BaseServiceTestCase;

public class Codegen extends BaseServiceTestCase {
	@Resource
	private ViewTableService viewTableService;
	@Resource
	private ViewColumnService viewColumnService;

	@Test
	public void testFindViewTableByName() {
		ViewTable viewTable = viewTableService.findViewTable(new HaloMap()
		.set("tableName", "base_company"));
		System.out.println(new Gson().toJson(viewTable));
	}

	@Test
	public void testFindViewColumnList() {
		List<ViewColumn> viewColumns = viewColumnService.findViewColumnList(new HaloMap()
		.set("tableName", "base_company"));
		System.out.println(new Gson().toJson(viewColumns));
	}

}
