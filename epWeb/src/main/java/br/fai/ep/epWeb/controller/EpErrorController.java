package br.fai.ep.epWeb.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class EpErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(final HttpServletRequest request) {
        final Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            final Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                System.out.println("ERRO DE NOT_FOUND >>>>>>> 404");
                return "redirect:/not-found";

            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                System.out.println("ERRO DE SERVER_ERROR >>>>>>> 500");
                return "redirect:/not-found";
            }
        }
        return "redirect:/not-found";
    }
}