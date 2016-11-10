package jlearn.servlet.service.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class QueryBuilder
{
    private ArrayList<String> whereParts;
    private ArrayList<Object> params;
    private String orderBy;
    private Integer limit;
    private Integer offset;
    private String select;
    private String table;

    public QueryBuilder()
    {
        this.whereParts = new ArrayList<>();
        this.params = new ArrayList<>();
    }

    public QueryBuilder andWhere(String condition, Object... params)
    {
        this.whereParts.add(condition);
        this.params.addAll(Arrays.asList(params));
        return this;
    }

    public QueryBuilder selectCount()
    {
        select = "count(1)";
        return this;
    }

    public QueryBuilder selectColumns(String... columns)
    {
        select = String.join(", ", columns);
        return this;
    }

    public QueryBuilder table(String table)
    {
        this.table = table;
        return this;
    }

    public QueryBuilder orderBy(String orderBy)
    {
        this.orderBy = orderBy;
        return this;
    }

    public QueryBuilder limit(Integer limit)
    {
        this.limit = limit;
        return this;
    }

    public QueryBuilder offset(Integer offset)
    {
        this.offset = offset;
        return this;
    }

    @Override
    public String toString()
    {
        int wherePartsCnt = whereParts.size();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(select);
        sb.append(" FROM ");
        sb.append(table);
        if (wherePartsCnt > 0) {
            sb.append(" WHERE ");
            sb.append(whereParts.get(0));
            for (int i = 1; i < wherePartsCnt; i++) {
                sb.append(" AND ");
                sb.append(whereParts.get(i));
            }
        }
        if (orderBy != null) {
            sb.append(" ORDER BY ");
            sb.append(orderBy);
        }
        if (limit != null) {
            sb.append(" LIMIT ");
            sb.append(limit);
        }
        if (offset != null) {
            sb.append(" OFFSET ");
            sb.append(offset);
        }
        return sb.toString();
    }

    public PreparedStatement createPreparedStatement(Connection conn) throws SQLException
    {
        String sql = toString();
        PreparedStatement st = conn.prepareStatement(sql);
        int n = params.size();
        for (int i = 0; i < n; i++) {
            st.setObject(i + 1, params.get(i));
        }
        return st;
    }
}
