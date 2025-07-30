//package com.ezedin.auth_service.configuration;
//
//import com.ezedin.auth_service.service.authService;
//import com.ezedin.auth_service.service.jwtService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.filter.OncePerRequestFilter;
//import java.io.IOException;
//
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//
//        private final authService userService;
//        private  final jwtService jwtservice;
//        @Override
//        protected void doFilterInternal(
//                @NonNull HttpServletRequest request,
//                @NonNull HttpServletResponse response,
//                @NonNull FilterChain filterChain
//    ) throws ServletException, IOException {
//            final String authHeader = request.getHeader("Authorization");
//            final String jwt;
//            final String userName;
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//            jwt = authHeader.substring(7);
//            userName=jwtservice.extractUsername(jwt);
//            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                UserDetails userDetails=this.userService.getUserByUserName(userName);
//                if(jwtservice.isTokenValid(jwt, userDetails)) {
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                            userDetails,
//                            null,
//                            userDetails.getAuthorities()
//                    );
//                    authToken.setDetails(
//                            new WebAuthenticationDetailsSource().buildDetails(request)
//                    );
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//
//                }
//            }
//            filterChain.doFilter(request, response);
//        }
//
//    }
//}
