package com.ducle.chat_service.model.entity;

import java.time.Instant;

import com.ducle.chat_service.model.enums.ChatRoomMemberRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_room_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomMemberRole role;

    @Column(nullable = true)
    private Instant lastSeen;

    public ChatRoomMember(ChatRoom chatRoom, Long memberId, ChatRoomMemberRole role) {
        this.id = null;
        this.chatRoom = chatRoom;
        this.memberId = memberId;
        this.role = role;
        this.lastSeen = Instant.now();
    }

}
