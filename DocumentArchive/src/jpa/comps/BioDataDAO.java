package jpa.comps;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import jpa.BioData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component("bioDataDAO")
public class BioDataDAO {
	private NamedParameterJdbcTemplate jdbc;
	
	public BioDataDAO(){
		System.out.println("jkhasdfajkhsTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
	}
	
	@Autowired
	public void setDataSource(DataSource jdbc){
		this.jdbc=new NamedParameterJdbcTemplate(jdbc);
		
	}
	
	/*public List<BioData> findAll() {
		return jdbc.query("select * from bio_data", new RowMapper<BioData>() {

			@Override
			public BioData mapRow(ResultSet rs, int arg1) throws SQLException {
				BioData data=new BioData();
				data.setFullNames(rs.getString("full_names"));
				data.setEmail(rs.getString("email"));
				data.setPhone(rs.getString("phone"));
				data.setNationality(rs.getString("nationality"));
				data.setSex(rs.getString("sex"));
				data.setDateOfBirth(rs.getDate("date_of_birth"));
				data.setAddress(rs.getString("address"));
				return data;
			}
		});
	}
	*/

}
