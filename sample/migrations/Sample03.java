package migrations;

import static com.jmigration.Migration.*;

import java.sql.Types;

import com.jmigration.Migration;
import com.jmigration.MigrationUnit;

public class Sample03 implements MigrationUnit {

	@Override
	public String version() {
		return "004";
	}
	
	public Migration createTablePessoa() {
		return createTable("PESSOAS_B")
			.add(column("ID_PESSOA").as(Types.NUMERIC).size(6).notNull())
			.add(column("NM_PESSOA").as(Types.VARCHAR).size(40).notNull())
			.add(column("DT_NASCIMENTO").as(Types.DATE).notNull());
	}

	@Override
	public String name() {
		return "sample04";
	}

}
