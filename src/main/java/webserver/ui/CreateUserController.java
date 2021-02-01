package webserver.ui;

import db.DataBase;
import model.User;
import webserver.domain.HttpRequest;
import webserver.domain.HttpResponse;

public class CreateUserController extends AbstractController {
    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        DataBase.addUser(new User(
                httpRequest.getParameter("userId"),
                httpRequest.getParameter("password"),
                httpRequest.getParameter("name"),
                httpRequest.getParameter("email")
        ));
    }
}
