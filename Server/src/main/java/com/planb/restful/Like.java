package com.planb.restful;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;

import com.planb.support.routing.API;
import com.planb.support.routing.REST;
import com.planb.support.routing.Route;
import com.planb.support.utilities.MySQL;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@Route(uri = "/meal", method = HttpMethod.POST)
@API(functionCategory = "급식", summary = "급식")
@REST(requestBody = "date: String, type: String, (breakfast, lunch, dinner), index: int(zero-based)", successCode = 200, failureCode = 204, etc = "해당 급식 정보 없을 때 204")
public class Like implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		String date = ctx.request().getFormAttribute("date");
		String type = ctx.request().getFormAttribute("type");
		int index = Integer.parseInt(ctx.request().getFormAttribute("index"));
		
		ResultSet rs = MySQL.executeQuery("SELECT * FROM meal WHERE date=?", date);
		try {
			rs.next();
			JSONArray target;
			
			if(type.equals("breakfast")) {
				target = new JSONArray(rs.getString("breakfast_like"));
				target.put(index, target.getInt(index) + 1);
				
				MySQL.executeUpdate("UPDATE meal SET breakfast_like=? WHERE date=?", target.toString(), date);
			} else if(type.equals("lunch")) {
				target = new JSONArray(rs.getString("lunch_like"));
				target.put(index, target.getInt(index) + 1);
				
				MySQL.executeUpdate("UPDATE meal SET lunch_like=? WHERE date=?", target.toString(), date);
			} else if(type.equals("dinner")) {
				target = new JSONArray(rs.getString("dinner_like"));
				target.put(index, target.getInt(index) + 1);
				
				MySQL.executeUpdate("UPDATE meal SET dinner_like=? WHERE date=?", target.toString(), date);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
