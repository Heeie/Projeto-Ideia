package Ideias.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Ideias.dto.*;
import Ideias.entities.Ideia;
import Ideias.services.SocialService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import Ideias.security.ActorGuard;

@RestController
@RequestMapping("/api")
public class SocialController {
    private final SocialService social;
    public SocialController(SocialService social) { this.social = social; }

    @GetMapping("/utilizadores/{userId}/favoritos")
    public List<IdeiaResponseDto> favoritos(@PathVariable Long userId, HttpServletRequest request) {
        ActorGuard.requireCurrentUser(request, userId);
        return social.favoritos(userId).stream().map(IdeiaController::resposta).toList();
    }
    @PutMapping("/utilizadores/{userId}/favoritos/{ideaId}")
    public FavoritoDto adicionarFavorito(@PathVariable Long userId, @PathVariable Long ideaId, HttpServletRequest request) {
        ActorGuard.requireCurrentUser(request, userId);
        social.favorito(userId, ideaId, true); return new FavoritoDto(ideaId, true);
    }
    @DeleteMapping("/utilizadores/{userId}/favoritos/{ideaId}")
    public FavoritoDto removerFavorito(@PathVariable Long userId, @PathVariable Long ideaId, HttpServletRequest request) {
        ActorGuard.requireCurrentUser(request, userId);
        social.favorito(userId, ideaId, false); return new FavoritoDto(ideaId, false);
    }
    @GetMapping("/ideias/{ideaId}/gostos")
    public GostosDto estadoGostos(@PathVariable Long ideaId, @RequestParam(required = false) Long utilizadorId, HttpServletRequest request) {
        if (utilizadorId != null) ActorGuard.requireCurrentUser(request, utilizadorId);
        social.exigirAcessoIdeia(ideaId, ActorGuard.currentUserId(request));
        return new GostosDto(social.gostos(ideaId), utilizadorId != null && social.gostado(ideaId, utilizadorId));
    }
    @PutMapping("/ideias/{ideaId}/gostos/{userId}")
    public GostosDto gostar(@PathVariable Long ideaId, @PathVariable Long userId, HttpServletRequest request) {
        ActorGuard.requireCurrentUser(request, userId);
        social.gostar(ideaId, userId, true); return new GostosDto(social.gostos(ideaId), true);
    }
    @DeleteMapping("/ideias/{ideaId}/gostos/{userId}")
    public GostosDto retirarGosto(@PathVariable Long ideaId, @PathVariable Long userId, HttpServletRequest request) {
        ActorGuard.requireCurrentUser(request, userId);
        social.gostar(ideaId, userId, false); return new GostosDto(social.gostos(ideaId), false);
    }

    @GetMapping("/ideias/{ideaId}/comentarios")
    public List<ComentarioResponseDto> comentarios(@PathVariable Long ideaId,
            @RequestParam(required = false) Long utilizadorId, HttpServletRequest request) {
        if (utilizadorId != null) ActorGuard.requireCurrentUser(request, utilizadorId);
        Long actorId = ActorGuard.currentUserId(request);
        return social.listarComentarios(ideaId, actorId);
    }
    @PostMapping("/ideias/{ideaId}/comentarios")
    public ResponseEntity<ComentarioResponseDto> comentar(@PathVariable Long ideaId,
            @Valid @RequestBody ComentarioCriarDto dto, HttpServletRequest request) {
        ActorGuard.requireCurrentUser(request, dto.autorId());
        return ResponseEntity.ok(social.comentar(ideaId, dto));
    }
    @DeleteMapping("/comentarios/{commentId}")
    public ResponseEntity<Void> removerComentario(@PathVariable Long commentId, @RequestParam Long utilizadorId, HttpServletRequest request) {
        ActorGuard.requireCurrentUser(request, utilizadorId);
        social.removerComentario(commentId, utilizadorId); return ResponseEntity.noContent().build();
    }
    @PutMapping("/comentarios/{commentId}/gostos/{userId}")
    public ResponseEntity<Void> gostarComentario(@PathVariable Long commentId, @PathVariable Long userId, HttpServletRequest request) {
        ActorGuard.requireCurrentUser(request, userId);
        social.gostarComentario(commentId, userId, true); return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/comentarios/{commentId}/gostos/{userId}")
    public ResponseEntity<Void> retirarGostoComentario(@PathVariable Long commentId, @PathVariable Long userId, HttpServletRequest request) {
        ActorGuard.requireCurrentUser(request, userId);
        social.gostarComentario(commentId, userId, false); return ResponseEntity.noContent().build();
    }

    @GetMapping("/grupos")
    public List<GrupoResponseDto> grupos(@RequestParam Long utilizadorId, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, utilizadorId); return social.listarGrupos(utilizadorId); }
    @PostMapping("/grupos")
    public ResponseEntity<GrupoResponseDto> criarGrupo(@Valid @RequestBody GrupoCriarDto dto, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, dto.criadorId()); return ResponseEntity.ok(social.criarGrupo(dto)); }
    @PutMapping("/grupos/{groupId}/membros/{userId}")
    public GrupoResponseDto juntarGrupo(@PathVariable Long groupId, @PathVariable Long userId, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, userId); return social.juntarGrupo(groupId, userId, true); }
    @DeleteMapping("/grupos/{groupId}/membros/{userId}")
    public GrupoResponseDto sairGrupo(@PathVariable Long groupId, @PathVariable Long userId, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, userId); return social.juntarGrupo(groupId, userId, false); }
    @DeleteMapping("/grupos/{groupId}")
    public ResponseEntity<Void> removerGrupo(@PathVariable Long groupId, @RequestParam Long utilizadorId, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, utilizadorId); social.removerGrupo(groupId, utilizadorId); return ResponseEntity.noContent().build(); }

    @GetMapping("/amizades")
    public List<AmizadeResponseDto> amizades(@RequestParam Long utilizadorId, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, utilizadorId); return social.listarAmizades(utilizadorId); }
    @PostMapping("/amizades")
    public ResponseEntity<AmizadeResponseDto> pedirAmizade(@Valid @RequestBody AmizadeCriarDto dto, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, dto.requerenteId()); return ResponseEntity.ok(social.pedirAmizade(dto)); }
    @PutMapping("/amizades/{friendshipId}/aceitar")
    public AmizadeResponseDto aceitarAmizade(@PathVariable Long friendshipId, @RequestParam Long utilizadorId, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, utilizadorId); return social.aceitarAmizade(friendshipId, utilizadorId); }
    @DeleteMapping("/amizades/{friendshipId}")
    public ResponseEntity<Void> removerAmizade(@PathVariable Long friendshipId, @RequestParam Long utilizadorId, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, utilizadorId); social.removerAmizade(friendshipId, utilizadorId); return ResponseEntity.noContent().build(); }

    @GetMapping("/conversas")
    public List<ConversaResponseDto> conversas(@RequestParam Long utilizadorId, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, utilizadorId); return social.listarConversas(utilizadorId); }
    @PostMapping("/conversas")
    public ResponseEntity<ConversaResponseDto> criarConversa(@Valid @RequestBody ConversaCriarDto dto, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, dto.utilizadorId()); return ResponseEntity.ok(social.criarConversa(dto)); }
    @GetMapping("/conversas/{conversationId}/mensagens")
    public List<MensagemResponseDto> mensagens(@PathVariable Long conversationId, @RequestParam Long utilizadorId, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, utilizadorId); return social.listarMensagens(conversationId, utilizadorId); }
    @PostMapping("/conversas/{conversationId}/mensagens")
    public ResponseEntity<MensagemResponseDto> enviarMensagem(@PathVariable Long conversationId, @Valid @RequestBody MensagemCriarDto dto, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, dto.remetenteId()); return ResponseEntity.ok(social.enviarMensagem(conversationId, dto)); }
    @DeleteMapping("/conversas/{conversationId}/mensagens")
    public ResponseEntity<Void> apagarMensagens(@PathVariable Long conversationId, @RequestParam Long utilizadorId, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, utilizadorId); social.apagarMensagens(conversationId, utilizadorId); return ResponseEntity.noContent().build(); }

    @GetMapping("/utilizadores/{userId}/preferencias")
    public PreferenciasDto preferencias(@PathVariable Long userId, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, userId); return social.obterPreferencias(userId); }
    @PutMapping("/utilizadores/{userId}/preferencias")
    public PreferenciasDto guardarPreferencias(@PathVariable Long userId, @RequestBody PreferenciasDto dto, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, userId); return social.guardarPreferencias(userId, dto); }
    @GetMapping("/utilizadores/{userId}/notificacoes")
    public List<NotificacaoResponseDto> notificacoes(@PathVariable Long userId, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, userId); return social.listarNotificacoes(userId); }
    @PutMapping("/notificacoes/{notificationId}/lida")
    public ResponseEntity<Void> marcarLida(@PathVariable Long notificationId, @RequestParam Long utilizadorId, HttpServletRequest request) { ActorGuard.requireCurrentUser(request, utilizadorId); social.marcarNotificacaoLida(notificationId, utilizadorId); return ResponseEntity.noContent().build(); }
}
