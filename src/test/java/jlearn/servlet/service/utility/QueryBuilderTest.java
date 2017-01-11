package jlearn.servlet.service.utility;

import org.junit.Test;

import static org.junit.Assert.*;

public class QueryBuilderTest {
    @Test
    public void testToString() throws Exception {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder
                .selectColumns("col1", "col2")
                .table("table")
                .andWhere("a=1")
                .andWhere("b=2")
                .orderBy("c desc")
                .limit(10)
                .offset(20);

        String sql = queryBuilder.toString();
        assertEquals("SELECT col1, col2 FROM table WHERE a=1 AND b=2 ORDER BY c desc LIMIT 10 OFFSET 20", sql);
    }
}