package migrations;

import static com.jmigration.Migration.*;

import java.sql.Types;

import com.jmigration.Migration;
import com.jmigration.MigrationUnit;

public class Sample01 implements MigrationUnit {

	@Override
	public String version() {
		return "001";
	}
	
	public Migration createTablePessoa() {
		return createTable("PESSOAS_Z")
			.add(column("ID_PESSOA").as(Types.NUMERIC).size(6).notNull())
			.add(column("NM_PESSOA").as(Types.VARCHAR).size(40).notNull())
			.add(column("DT_NASCIMENTO").as(Types.DATE).notNull());
	}

	@Override
	public String name() {
		return "sample01";
	}

}
