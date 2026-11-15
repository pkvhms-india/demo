package com.varicent.common.theme.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.sql.QueryBuilder;
import com.varicent.common.theme.dao.ThemeDao;
import com.varicent.common.theme.domain.Theme;
import com.varicent.core.database.Database;

@Repository("themeDao")
public class JdbcThemeDaoImpl implements ThemeDao {
	private static final String SFDC_COLOR = "#00305E";
	private static final String DEFAULT_COLOR = "#96AAC2";
	private final RowMapper<Theme> themeMapper = new RowMapper<Theme>() {
		@Override
		public Theme mapRow(ResultSet rs, int rowNumber) throws SQLException {
			Theme.CustomThemeType themeType = Theme.CustomThemeType.valueOf(rs.getInt("WebThemeType"));
			String headerColor = themeType == Theme.CustomThemeType.SFDC ? "#00305E" : "#96AAC2";
			return new Theme(rs.getBoolean("IsEnabled"), headerColor, rs.getBytes("HeaderImage"), themeType);
		}
	};
	private final JdbcOperations operations;

	@Autowired
	public JdbcThemeDaoImpl(DataSource ds) {
		this.operations = Database.getOperations(ds);
	}

	@Override
	public Theme getTheme() {
		QueryDefinition query = new QueryDefinition("Theme");
		query.select("Theme", "IsEnabled");
		query.select("Theme", "HeaderImage");
		query.select("Theme", "WebThemeType");
		query.newConstraint("Theme", "IsEnabled", Constraint.Operator.Equals, 1);
		QueryBuilder qb = Database.getQueryBuilder();
		List<Theme> result = this.operations.query(qb.toSql(query), this.themeMapper);
		return result.isEmpty() ? null : (Theme) result.get(0);
	}
}
