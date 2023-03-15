package com.mballem.demoajax.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.mballem.demoajax.domain.Emissor;
import com.mballem.demoajax.repository.PromocaoRepository;
import com.mballem.demoajax.service.NotificacaoService;

@Controller
public class NotificacaoController {

	@Autowired
	private PromocaoRepository repository;
	
	@Autowired
	private NotificacaoService service;
	
	@GetMapping("/promocao/notificacao")
	public SseEmitter enviarNotificacao() throws IOException {
		
		SseEmitter emitter = new SseEmitter(0L);
		
		Emissor emissor = new Emissor(emitter, getDtCadastroUltimaPromocao());
		service.onOpen(emissor);
		service.addEmissor(emissor);
		
		emissor.getEmitter().onCompletion(() -> service.removeEmissor(emissor));
		
		System.out.println("> size after add: " + service.getEmissores().size());
		
		
		return emissor.getEmitter();
	}
	
	private LocalDateTime getDtCadastroUltimaPromocao() {
		return repository.findPromocaoComUltimaData();
	}
	
}
