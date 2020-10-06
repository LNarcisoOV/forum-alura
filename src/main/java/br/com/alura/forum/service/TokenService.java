package br.com.alura.forum.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.alura.forum.controller.form.LoginForm;
import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	@Value("${forum.jwt.expiration}")
	private String expiration;
	
	@Value("${forum.jwt.secret}")
	private String secret;

	public String gerarToken(LoginForm authentication) {
		Date hoje = new Date();
		Date dataExpircao = new Date(hoje.getTime() + Long.parseLong(expiration));
		return Jwts.builder().setIssuer("FORUM API")
				.setSubject(authentication.getEmail())
				.setIssuedAt(hoje)
				.setExpiration(dataExpircao)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

}
