package hello.servelt.web.frontcontroller.v5;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servelt.web.frontcontroller.ModelView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface MyHandlerAdapter {

    boolean supports(ObjectMapper handler);

    ModelView handle(HttpServletRequest request, HttpServletResponse response,Object handler)throws ServletException, IOException;
}
