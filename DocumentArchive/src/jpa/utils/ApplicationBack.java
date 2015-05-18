package jpa.utils;

import java.util.List;

import jpa.Announcement;
import jpa.BioData;
import jpa.Documents;
import jpa.Messages;
import jpa.Schedule;

public class ApplicationBack {
	
	private static List<Schedule> schedules;
	private static List<Announcement> announcements;
	private static List<BioData> bioDatas;
	private static List<Messages> messages;
	private static List<Documents> documents;
	public static List<Schedule> getSchedules() {
		return schedules;
	}
	public static void setSchedules(List<Schedule> schedules) {
		ApplicationBack.schedules = schedules;
	}
	public static List<Announcement> getAnnouncements() {
		return announcements;
	}
	public static void setAnnouncements(List<Announcement> announcements) {
		ApplicationBack.announcements = announcements;
	}
	public static List<BioData> getBioDatas() {
		return bioDatas;
	}
	public static void setBioDatas(List<BioData> bioDatas) {
		ApplicationBack.bioDatas = bioDatas;
	}
	public static List<Messages> getMessages() {
		return messages;
	}
	public static void setMessages(List<Messages> messages) {
		ApplicationBack.messages = messages;
	}
	public static List<Documents> getDocuments() {
		return documents;
	}
	public static void setDocuments(List<Documents> documents) {
		ApplicationBack.documents = documents;
	}
	
	

}
