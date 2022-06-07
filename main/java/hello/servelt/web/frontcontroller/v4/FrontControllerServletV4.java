package hello.servelt.web.frontcontroller.v4;

import hello.servelt.web.frontcontroller.ModelView;
import hello.servelt.web.frontcontroller.MyView;
import hello.servelt.web.frontcontroller.v3.ControllerV3;
import hello.servelt.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servelt.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servelt.web.frontcontroller.v4.controller.MemberSaveControllerV4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV4",urlPatterns = "/front-controller/v4/*") // v1 / 하위에 어떤게 들어와도 이 서블릿이 무조건 호출이 됨 이렇게 매핑 하면됨
public class FrontControllerServletV4 extends HttpServlet {

    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v4/members/new-form",new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save",new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members",new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("FrontControllerServletV4.service");

        // /front-controller/v4/members
        String requestURI = request.getRequestURI();

        ControllerV4 controller = controllerMap.get(requestURI);

        if (controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //paramMap
        Map<String, String> paramMap = createParamMap(request); //단축키 사용 메서드로 만듬 option.command,m
        Map<String ,Object> model = new HashMap<>(); //이 부분이 추가 Model 이 Param 안에[ 들어갔기 때문에.
        String viewName = controller.process(paramMap, model);

        MyView view = viewResolver(viewName);
        view.render(model , request,response);
    }

    // /WEB-INF/views/new-form.jsp
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String,String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paraName -> paramMap.put(paraName, request.getParameter(paraName)));
        return paramMap;
    }
}
