package controller;

import domain.HttpRequest;
import domain.HttpResponse;
import exception.InvalidTemplateException;
import model.User;
import service.MemberService;
import template.HandlebarTemplateEngine;

import java.util.List;

public class MembersController implements Controller {
	private static final MemberService memberService = new MemberService();
	private static final HandlebarTemplateEngine handlebars = new HandlebarTemplateEngine();

	@Override
	public void execute(HttpRequest request, HttpResponse response) {
		try {
			if (request.isLoginCookie()) {
				List<User> members = memberService.getAllMembers();
				response.forwardBody(handlebars.apply("/user/list", members).getBytes());
			}
			response.sendRedirect("/user/login.html", false);
		} catch (InvalidTemplateException e) {
			throw e;
		}
	}
}
