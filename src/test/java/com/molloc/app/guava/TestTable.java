package com.molloc.app.guava;

import org.junit.Test;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.molloc.app.BaseTest;

public class TestTable extends BaseTest
{

	@Test
	public void tableTest()
	{
		Table<String, Integer, String> aTable = HashBasedTable.<String, Integer, String> create();

		for (char a = 'A'; a <= 'C'; ++a)
		{
			for (Integer b = 1; b <= 3; ++b)
			{
				aTable.put(Character.toString(a), b, String.format("%c%d", a, b));
			}
		}

		log(aTable);

		log(aTable.column(2));
		log(aTable.row("B"));
		log(aTable.get("B", 2));

		log(aTable.contains("D", 1));
		log(aTable.containsColumn(3));
		log(aTable.containsRow("C"));
		log(aTable.columnMap());
		log(aTable.rowMap());
		log(aTable.remove("B", 3));

		log(aTable.size());
		log(aTable.rowMap().size());
		log(aTable.columnMap().size());
	}
}
