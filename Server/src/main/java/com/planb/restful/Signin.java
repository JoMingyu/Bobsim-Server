package com.planb.restful;

import com.planb.support.routing.API;
import com.planb.support.routing.REST;
import com.planb.support.routing.Route;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@Route(uri = "/auth/signup", method = HttpMethod.POST)
@API(functionCategory = "계정", summary = "회원가입")
@REST(requestBody = "id: String, pw: String", successCode = 201, failureCode = 204, etc = "아이디가 겹칠 경우 204")
public class Signin implements Handler<RoutingContext> {
	@Override
	public void handle(RoutingContext ctx) {
		String id = ctx.request().getFormAttribute("id");
		String password = ctx.request().getFormAttribute("password");
	}
}
