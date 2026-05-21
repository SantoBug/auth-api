package com.douglas.auth_api.config;

import com.douglas.auth_api.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal (
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        //PEGA O HEARDER AUTHORIZATION DA REQUISICAO
        final String authHeader = request.getHeader("Authorization");
        // PERGUNTA 1: TEM TOKEN ? COMECA COM "Bearer " ?
        //SE NAO TEM, PULA O FILTRO
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //REMOVE O "Bearer " DO TOKEN
        final String jwt = authHeader.substring(7);

        // PERGUNTA 2 E 3: EXTRAI O EMAIL DO TOKEN E BUSCA NO BANCO
        final String email = jwtService.extractUsername(jwt);

        //SO CONTINUA SE EXTRAIU O EMAIL E O USUARIO AINDA NAO ESTA AUTENTICADO
        if (email != null &&
            SecurityContextHolder.getContext().getAuthentication() == null) {

            //BUSCA O USUARIO NO BANCO PELO EMAIL
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // PERGUNTA 4: O TOKEN É VALIDO?
            if (jwtService.isTokenValid(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null, // CREDENTIALS - NULL POIS JA VALIDAMOS PELO TOKEN
                    userDetails.getAuthorities()
                );
                // ADICIONA DETALHES DA REQUISICAO (IP, ETC)

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                //AVISA O SPRING SECURITY Q O USUARIO JA ESTA AUTENTICADO
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //PASSA A REQUISICAO PARA O PROXIMO FILTRO OU CONTROLLER
        filterChain.doFilter(request, response);
    }

}
