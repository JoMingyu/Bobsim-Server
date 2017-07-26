package com.planb.support.utilities;

import java.util.Calendar;
import java.util.List;

import org.hyunjun.school.School;
import org.hyunjun.school.SchoolException;
import org.hyunjun.school.SchoolMenu;
import org.json.JSONArray;

public class Parser extends Thread {
	@Override
	public void run() {
		while(true) {
			Calendar cal = Calendar.getInstance();
			
			try {
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH) + 1;
				
				School api = new School(School.Type.HIGH, School.Region.DAEJEON, "G100000170");
				List<SchoolMenu> menus = api.getMonthlyMenu(year, month);
				
				int maxDate = cal.getActualMaximum(Calendar.DATE);
				
				for(int i = 0; i < maxDate; i++) {
					SchoolMenu dayMenu = menus.get(i);
					JSONArray[] menuArray = new JSONArray[3];
					JSONArray[] likeArray = new JSONArray[3];
					
					String[] meals = new String[3];
					int[] mealsCount = new int[3];
					
					meals[0] = dayMenu.breakfast;
					meals[1] = dayMenu.lunch;
					meals[2] = dayMenu.dinner;
					
					for(int j = 0; j < 3; j++) {
						menuArray[j] = new JSONArray();
						likeArray[j] = new JSONArray();
						
						for(String time: meals[j].split("\n")) {
							String menu = time.split("\\*")[0];
							menuArray[j].put(menu);
							mealsCount[j]++;
						}
						
						for(int k = 0; k < mealsCount[j]; k++) {
							likeArray[j].put(0);
						}
					}
					
					String today = String.format("%04d-%02d-%02d", year, month, i + 1);
					MySQL.executeUpdate("DELETE FROM meal WHERE date=?", today);
					MySQL.executeUpdate("INSERT INTO meal(date, breakfast, breakfast_like, lunch, lunch_like, dinner, dinner_like) VALUES(?, ?, ?, ?, ?, ?, ?)", today, menuArray[0].toString(), likeArray[0].toString(), menuArray[1].toString(), likeArray[1].toString(), menuArray[2].toString(), likeArray[2].toString());
				}
				
				Thread.sleep(1000 * 3600 * 24);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (SchoolException e) {
				e.printStackTrace();
			}
		}
	}
}
