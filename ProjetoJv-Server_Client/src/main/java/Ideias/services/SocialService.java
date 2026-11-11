package Ideias.services;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Ideias.dto.*;
import Ideias.entities.*;
import Ideias.exceptions.ConflictException;
import Ideias.exceptions.ResourceNotFoundException;
import Ideias.repositories.*;

@Service
@Transactional
public class SocialService {
    private final UtilizadorRepository users;
    private final ClienteRepository clients;
    private final IdeiaRepository ideas;
    private final ComentarioRepository comments;
    private final GrupoRepository groups;
    private final AmizadeRepository friendships;
    private final ConversaRepository conversations;
    private final MensagemRepository messages;
    private final PreferenciasRepository preferences;
    private final NotificacaoRepository notifications;

    public SocialService(UtilizadorRepository users, ClienteRepository clients, IdeiaRepository ideas,
            ComentarioRepository comments, GrupoRepository groups, AmizadeRepository friendships,
            ConversaRepository conversations, MensagemRepository messages,
            PreferenciasRepository preferences, NotificacaoRepository notifications) {
        this.users = users; this.clients = clients; this.ideas = ideas; this.comments = comments;
        this.groups = groups; this.friendships = friendships; this.conversations = conversations;
        this.messages = messages; this.preferences = preferences; this.notifications = notifications;
    }

    public List<Ideia> favoritos(Long userId) { return user(userId).getIdeiasFav().stream().toList(); }
    public boolean favorito(Long userId, Long ideaId, boolean save) {
        Utilizador user = user(userId); Ideia idea = idea(ideaId);
        if (save) return user.getIdeiasFav().add(idea);
        user.getIdeiasFav().remove(idea); return false;
    }
    public int gostos(Long ideaId) { return idea(ideaId).getGostadaPor().size(); }
    public boolean gostado(Long ideaId, Long userId) {
        return idea(ideaId).getGostadaPor().stream().anyMatch(user -> user.getId().equals(userId));
    }
    public int gostar(Long ideaId, Long userId, boolean like) {
        Ideia idea = idea(ideaId); Utilizador user = user(userId);
        if (like) user.getIdeiasGostadas().add(idea); else user.getIdeiasGostadas().remove(idea);
        return idea.getGostadaPor().size();
    }

    public List<ComentarioResponseDto> listarComentarios(Long ideaId, Long userId) {
        idea(ideaId);
        return comments.findByIdeia_IdOrderByCriadoEmAsc(ideaId).stream()
                .map(comment -> comentarioDto(comment, userId)).toList();
    }
    public ComentarioResponseDto comentar(Long ideaId, ComentarioCriarDto dto) {
        Ideia idea = idea(ideaId); Utilizador author = user(dto.autorId());
        Comentario comment = new Comentario(); comment.setIdeia(idea); comment.setAutor(author);
        comment.setTexto(dto.texto().trim()); comment = comments.save(comment);
        if (!idea.getCliente().getId().equals(author.getId()))
            notify(idea.getCliente(), "COMENTARIO", author.getNome() + " comentou a tua ideia " + idea.getNome() + ".", "ideiaPage.html?id=" + ideaId);
        return comentarioDto(comment, author.getId());
    }
    public void removerComentario(Long commentId, Long userId) {
        Comentario comment = comments.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comentário não encontrado."));
        if (!comment.getAutor().getId().equals(userId)) throw new IllegalArgumentException("Só o autor pode remover o comentário.");
        comments.delete(comment);
    }
    public boolean gostarComentario(Long commentId, Long userId, boolean like) {
        Comentario comment = comments.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comentário não encontrado."));
        Utilizador user = user(userId);
        if (like) return comment.getGostadaPor().add(user);
        comment.getGostadaPor().remove(user); return false;
    }
    private ComentarioResponseDto comentarioDto(Comentario comment, Long userId) {
        boolean liked = userId != null && comment.getGostadaPor().stream().anyMatch(user -> user.getId().equals(userId));
        return new ComentarioResponseDto(comment.getId(), comment.getTexto(), comment.getAutor().getId(),
                comment.getAutor().getNome(), comment.getGostadaPor().size(), liked, comment.getCriadoEm());
    }

    public List<GrupoResponseDto> listarGrupos(Long userId) {
        user(userId);
        return groups.findAll().stream().map(group -> grupoDto(group, userId)).toList();
    }
    public GrupoResponseDto criarGrupo(GrupoCriarDto dto) {
        Cliente owner = clients.findById(dto.criadorId()).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
        Grupo group = new Grupo(); group.setNome(dto.nome().trim()); group.setDescricao(dto.descricao().trim());
        group.setCriador(owner); group.getMembros().add(owner);
        return grupoDto(groups.save(group), owner.getId());
    }
    public GrupoResponseDto juntarGrupo(Long groupId, Long userId, boolean join) {
        Grupo group = groups.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado."));
        Utilizador user = user(userId);
        if (join) group.getMembros().add(user); else if (!group.getCriador().getId().equals(userId)) group.getMembros().remove(user);
        return grupoDto(group, userId);
    }
    public void removerGrupo(Long groupId, Long userId) {
        Grupo group = groups.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado."));
        if (!group.getCriador().getId().equals(userId)) throw new IllegalArgumentException("Só quem criou o grupo o pode eliminar.");
        groups.delete(group);
    }
    private GrupoResponseDto grupoDto(Grupo group, Long userId) {
        return new GrupoResponseDto(group.getId(), group.getNome(), group.getDescricao(), group.getCriador().getId(),
                group.getCriador().getNome(), group.getMembros().stream().map(Utilizador::getId).toList(),
                group.getMembros().stream().anyMatch(member -> member.getId().equals(userId)));
    }

    public List<AmizadeResponseDto> listarAmizades(Long userId) {
        user(userId);
        return friendships.findByRequerente_IdOrDestinatario_Id(userId, userId).stream().map(this::amizadeDto).toList();
    }
    public AmizadeResponseDto pedirAmizade(AmizadeCriarDto dto) {
        if (dto.requerenteId().equals(dto.destinatarioId())) throw new IllegalArgumentException("Não podes enviar um pedido a ti próprio.");
        Utilizador from = user(dto.requerenteId()), to = user(dto.destinatarioId());
        var existing = friendships.findByRequerente_IdAndDestinatario_IdOrRequerente_IdAndDestinatario_Id(from.getId(), to.getId(), to.getId(), from.getId());
        if (existing.isPresent()) throw new ConflictException("Já existe um pedido ou amizade entre estas contas.");
        Amizade friendship = new Amizade(); friendship.setRequerente(from); friendship.setDestinatario(to);
        friendship = friendships.save(friendship);
        notify(to, "AMIZADE", from.getNome() + " enviou-te um pedido de amizade.", "amigosPage.html");
        return amizadeDto(friendship);
    }
    public AmizadeResponseDto aceitarAmizade(Long friendshipId, Long userId) {
        Amizade friendship = friendship(friendshipId);
        if (!friendship.getDestinatario().getId().equals(userId)) throw new IllegalArgumentException("Só o destinatário pode aceitar o pedido.");
        if (!"PENDENTE".equals(friendship.getEstado())) throw new ConflictException("Este pedido já foi tratado.");
        friendship.setEstado("ACEITE"); return amizadeDto(friendship);
    }
    public void removerAmizade(Long friendshipId, Long userId) {
        Amizade friendship = friendship(friendshipId);
        if (!friendship.getRequerente().getId().equals(userId) && !friendship.getDestinatario().getId().equals(userId))
            throw new IllegalArgumentException("Não tens acesso a este pedido.");
        friendships.delete(friendship);
    }
    private Amizade friendship(Long id) { return friendships.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pedido de amizade não encontrado.")); }
    private AmizadeResponseDto amizadeDto(Amizade item) {
        return new AmizadeResponseDto(item.getId(), item.getRequerente().getId(), item.getRequerente().getNome(),
                item.getDestinatario().getId(), item.getDestinatario().getNome(), item.getEstado(), item.getCriadoEm());
    }

    public List<ConversaResponseDto> listarConversas(Long userId) {
        Utilizador current = user(userId);
        return conversations.findDistinctByParticipantes_Id(userId).stream()
                .sorted(Comparator.comparing(this::ultimaData, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(conversation -> conversaDto(conversation, current)).toList();
    }
    public ConversaResponseDto criarConversa(ConversaCriarDto dto) {
        if (dto.utilizadorId().equals(dto.contactoId())) throw new IllegalArgumentException("Não podes criar uma conversa contigo próprio.");
        Utilizador current = user(dto.utilizadorId()), contact = user(dto.contactoId());
        for (Conversa item : conversations.findDistinctByParticipantes_Id(current.getId()))
            if (item.getParticipantes().size() == 2 && item.getParticipantes().contains(contact)) return conversaDto(item, current);
        Conversa conversation = new Conversa(); conversation.getParticipantes().add(current); conversation.getParticipantes().add(contact);
        return conversaDto(conversations.save(conversation), current);
    }
    public List<MensagemResponseDto> listarMensagens(Long conversationId, Long userId) {
        Conversa conversation = conversa(conversationId); ensureParticipant(conversation, userId);
        return messages.findByConversa_IdOrderByCriadoEmAsc(conversationId).stream().map(this::mensagemDto).toList();
    }
    public MensagemResponseDto enviarMensagem(Long conversationId, MensagemCriarDto dto) {
        Conversa conversation = conversa(conversationId); ensureParticipant(conversation, dto.remetenteId());
        Mensagem message = new Mensagem(); message.setConversa(conversation); message.setRemetente(user(dto.remetenteId())); message.setTexto(dto.texto().trim());
        Mensagem savedMessage = messages.save(message);
        conversation.getParticipantes().stream().filter(member -> !member.getId().equals(dto.remetenteId())).forEach(recipient ->
                notify(recipient, "MENSAGEM", savedMessage.getRemetente().getNome() + " enviou-te uma mensagem.", "mensagens.html"));
        return mensagemDto(savedMessage);
    }
    public void apagarMensagens(Long conversationId, Long userId) {
        ensureParticipant(conversa(conversationId), userId); messages.deleteByConversa_Id(conversationId);
    }
    private Conversa conversa(Long id) { return conversations.findById(id).orElseThrow(() -> new ResourceNotFoundException("Conversa não encontrada.")); }
    private void ensureParticipant(Conversa conversation, Long userId) {
        if (conversation.getParticipantes().stream().noneMatch(user -> user.getId().equals(userId)))
            throw new IllegalArgumentException("Não tens acesso a esta conversa.");
    }
    private LocalDateTime ultimaData(Conversa conversation) { return conversation.getMensagens().stream().map(Mensagem::getCriadoEm).filter(java.util.Objects::nonNull).max(LocalDateTime::compareTo).orElse(null); }
    private ConversaResponseDto conversaDto(Conversa conversation, Utilizador current) {
        Utilizador contact = conversation.getParticipantes().stream().filter(user -> !user.getId().equals(current.getId())).findFirst().orElse(current);
        Mensagem latest = conversation.getMensagens().stream().max(Comparator.comparing(Mensagem::getCriadoEm, Comparator.nullsFirst(Comparator.naturalOrder()))).orElse(null);
        return new ConversaResponseDto(conversation.getId(), contact.getId(), contact.getNome(), latest == null ? "" : latest.getTexto(), latest == null ? null : latest.getCriadoEm());
    }
    private MensagemResponseDto mensagemDto(Mensagem message) {
        return new MensagemResponseDto(message.getId(), message.getRemetente().getId(), message.getRemetente().getNome(), message.getTexto(), message.getCriadoEm());
    }

    public PreferenciasDto obterPreferencias(Long userId) {
        user(userId); Preferencias item = preferences.findById(userId).orElseGet(() -> criarPreferencias(userId));
        return new PreferenciasDto(item.isNovasIdeias(), item.isComentarios());
    }
    public PreferenciasDto guardarPreferencias(Long userId, PreferenciasDto dto) {
        Preferencias item = preferences.findById(userId).orElseGet(() -> criarPreferencias(userId));
        item.setNovasIdeias(dto.novasIdeias()); item.setComentarios(dto.comentarios());
        preferences.save(item); return dto;
    }
    private Preferencias criarPreferencias(Long userId) { Preferencias item = new Preferencias(); item.setUtilizador(user(userId)); return preferences.save(item); }
    public List<NotificacaoResponseDto> listarNotificacoes(Long userId) {
        user(userId);
        return notifications.findByDestinatario_IdOrderByCriadoEmDesc(userId).stream().map(item ->
                new NotificacaoResponseDto(item.getId(), item.getTipo(), item.getTexto(), item.getLink(), item.isLida(), item.getCriadoEm())).toList();
    }
    public void marcarNotificacaoLida(Long notificationId, Long userId) {
        Notificacao item = notifications.findById(notificationId).orElseThrow(() -> new ResourceNotFoundException("Notificação não encontrada."));
        if (!item.getDestinatario().getId().equals(userId)) throw new IllegalArgumentException("Não tens acesso a esta notificação.");
        item.setLida(true);
    }
    private void notify(Utilizador recipient, String type, String text, String link) {
        Preferencias pref = preferences.findById(recipient.getId()).orElse(null);
        if (pref != null && (("COMENTARIO".equals(type) && !pref.isComentarios()) || ("IDEIA".equals(type) && !pref.isNovasIdeias()))) return;
        Notificacao item = new Notificacao(); item.setDestinatario(recipient); item.setTipo(type); item.setTexto(text); item.setLink(link); notifications.save(item);
    }
    private Utilizador user(Long id) { return users.findById(id).orElseThrow(() -> new ResourceNotFoundException("Utilizador não encontrado.")); }
    private Ideia idea(Long id) { return ideas.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ideia não encontrada.")); }
}
