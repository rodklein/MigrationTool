package migrations;

import static com.jmigration.Migration.column;
import static com.jmigration.Migration.createTable;
import static com.jmigration.Migration.primaryKeyColumn;

import java.sql.Types;

import com.jmigration.Migration;
import com.jmigration.MigrationUnit;

public class Sample02 implements MigrationUnit {

	@Override
	public String version() {
		return "10.1 005";
	}
	
	public Migration createTablePessoa() {
		return createTable("PESSOAS_X")
			.add(primaryKeyColumn("ID_PESSOA").as(Types.NUMERIC).size(6).notNull().autoIncrement("SQ_PESSOA_X"))
			.add(column("NM_PESSOA").as(Types.VARCHAR).size(40).notNull())
			.add(column("DT_NASCIMENTO").as(Types.DATE).notNull());
	}
	
	

	@Override
	public String name() {
		return "sample02";
	}

}
