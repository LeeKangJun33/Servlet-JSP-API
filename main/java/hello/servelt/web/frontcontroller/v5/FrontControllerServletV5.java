package hello.servelt.web.frontcontroller.v5;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servelt.web.frontcontroller.ModelView;
import hello.servelt.web.frontcontroller.MyView;
import hello.servelt.web.frontcontroller.v3.ControllerV3;
import hello.servelt.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servelt.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servelt.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servelt.web.frontcontroller.v4.ControllerV4;
import hello.servelt.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servelt.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servelt.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servelt.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import hello.servelt.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5",urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    /*
    private Map<String, ControllerV4> controllerMap = new HashMap<>(); // 지금은 아무 컨트롤러나 다 들어갈수있어야함  그래서 다 지원하기 위해서 오브젝트를 넣음.
     */
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdapters();

        //코드가 복잡해지니깐 초기화시켜준것.
    }

    private void initHandlerAdapters() {

        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        //V4 추가
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

       Object handler =  getHandler(request); // 로직을 깔끔하게 만들기위해서 초기해줌 디테일은 밑에 getHandler 메서드 보면된다, handler호출
        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        MyHandlerAdapter adapter = getHandlerAdapter(handler); //  MyHandlerAdapter가져옴

        ModelView mv = adapter.handle(request, response, handler);//handle 을 호

        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);

        view.render(mv.getModel(),request,response);

    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if(adapter.supports((ObjectMapper) handler)){
                       return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을수 없습니다. handler = "+handler);
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
       return handlerMappingMap.get(requestURI);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
