package com.charles.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.charles.todolist.user.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Pegar a autenticaçao (usuario e senha)
        var authorization = request.getHeader("Authorization");

        var authEncoded = authorization.substring("Basic".length()).trim();

        byte[] authDecode =  Base64.getDecoder().decode(authEncoded);

        var authString = new String(authDecode);

        //[Usuario]:[Senha]
        String[] credentials =authString.split(":");
        String username = credentials[0];
        String password = credentials[1];

        System.out.println("user_password");
        System.out.println(username + " " + password);
        //valida usuari
        var user = this.userRepository.findByUsername(username);

        if(user == null){
            response.sendError(401);
        }else{
            //valida senha
            var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

            if(passwordVerify.verified){
                filterChain.doFilter(request, response);
            }else{
                response.sendError(401);
            }

            //segue o fluxo


        }
    }
}
