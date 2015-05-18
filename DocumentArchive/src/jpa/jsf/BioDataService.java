/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.jsf;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;

import jpa.BioData;
import jpa.comps.BioDataDAO;
import jpa.models.BioDataFacade;
import jpa.utils.DatabaseUtils;

/**
 *
 * @author Naeima
 */
@ManagedBean
@SessionScoped
public class BioDataService {
	@EJB
	private BioDataFacade bioDataFacade;
	private List<BioData> bioDatas = new ArrayList<BioData>();
	private BioData bioData = new BioData();
	private SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

	private BioDataDAO bioDataDAO = new BioDataDAO();

	public BioDataService() {
	}

	public BioData getBioData() {
		return bioData;
	}

	public void setBioData(BioData bioData) {
		this.bioData = bioData;
	}

	public List<BioData> getBioDatas() {
		return bioDatas;
	}

	public void setBioDatas(List<BioData> bioDatas) {
		this.bioDatas = bioDatas;
	}

	public BioDataDAO getBioDataDAO() {
		return bioDataDAO;
	}

	public void setBioDataDAO(BioDataDAO bioDataDAO) {
		this.bioDataDAO = bioDataDAO;
	}

	public void saveBioData(ActionEvent event) {
		// bioDataFacade.create(bioData);
		String sql = "INSERT INTO `management`.`bio_data` (`full_names`, `email`, `phone`, `nationality`, `sex`, `date_of_birth`, `address`) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement ps = DatabaseUtils.getConnection()
					.prepareStatement(sql);
			ps.setString(1, bioData.getFullNames());
			ps.setString(2, bioData.getEmail());
			ps.setString(3, bioData.getPhone());
			ps.setString(4, bioData.getNationality());
			ps.setString(5, bioData.getSex());
			ps.setString(6, formater.format(bioData.getDateOfBirth()));
			ps.setString(7, bioData.getAddress());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		addMessage("Saved Record: " + bioData.getFullNames());
		System.out.println(bioData);
		bioData = new BioData();
	}

	public List<BioData> getDataList() {
		List<BioData> datas=new ArrayList<BioData>();
		String sql = "SELECT * FROM bio_data";
		try {
			PreparedStatement ps = DatabaseUtils.getConnection()
					.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			BioData bioData;
			while(rs.next()){
				bioData=new BioData();
				bioData.setId(rs.getInt("id"));
				bioData.setFullNames(rs.getString("full_names"));
				bioData.setEmail(rs.getString("email"));
				bioData.setPhone(rs.getString("phone"));
				bioData.setNationality(rs.getString("nationality"));
				bioData.setSex(rs.getString("sex"));
				bioData.setDateOfBirth(rs.getDate("date_of_birth"));
				datas.add(bioData);
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return datas;

	}

	@PostConstruct
	public void init() {
		bioDatas=getDataList();
	}

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				summary, summary);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}
