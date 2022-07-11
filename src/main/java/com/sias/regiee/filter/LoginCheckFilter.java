package com.sias.regiee.filter;

import com.alibaba.fastjson.JSON;
import com.sias.regiee.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.LogRecord;

/**
 * @author Edgar
 * @create 2022-07-11 10:33
 * @faction:
 */


/*一：filterName是过滤器的名字，urlPatterns是过滤所有的请求地址*/
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    /*0.路径匹配器（是一个方法）
    *   目的为了使用这个方法
    *   把请求过来的参数和
    *   已有的参数进行比较，看是否在这个范围内
    *   在的话，往下面执行，不在的话，执行另外一个方法*/
    public static final AntPathMatcher PATH_MATCHER =new AntPathMatcher();

    /*1.这个使用的是过滤器
    *   过滤器比拦截器功能实现简单*/
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /*01.ServletRequest需要强转成HttpServletRequest
        *    这样的话，在去处理方法的时候，才更加的方便
        *    request.getRequestURI()是获取拦截地址中
        *    的数据，{}是占位符，把获取的请求数据写在这个位置上
        *    */
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        /*02.获取本次请求的Url*/
        String requestURI = request.getRequestURI();
        log.info("拦截到的请求：{}",requestURI);
        //定义不需要处理请求的路径，"/backend/**",后面的**，表示这个文件夹下面文件夹下面的文件
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };
        /*03.判断本次是否需要处理*/
        boolean check = check(urls, requestURI);
        /*04.不需要处理直接放行*/
        if (check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        /*05.判断登陆状态，如果已经登陆，直接放行*/
        if (request.getSession().getAttribute("employee") !=null){
            log.info("用户已经登陆，用户id为{}",request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户没有登陆+++++++++");
        /*06.如果没有登陆返回未登陆结果，通过输出流的方式向客户端响应数据
        *    客户端收到数据之后，去访问这个NOTLOGIN对应的地址*/
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;


    }

    /*2.路径匹配器，检查本次请求是否需要放行
    *   math方法是看看requestURI是否在URL放行
    *   路径范围内，不完全一样，只要是在这个范围内就可以
    *   有一个return，就中断这个方法，把得到的结果给调用者*/
    public boolean check (String [] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
