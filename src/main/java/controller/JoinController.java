package controller;

import domain.HttpRequest;
import domain.HttpResponse;
import service.MemberService;

public class JoinController implements Controller {
	private static final MemberService memberService = new MemberService();

	@Override
	public void execute(HttpRequest request, HttpResponse response) {
		memberService.joinMember(request.getParameter());
		response.sendRedirect("/index.html", false);
	}
}
