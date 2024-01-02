package com.chatus.services;

import com.chatus.constants.DatabaseConstants;
import com.chatus.dtos.GroupCompleteDto;
import com.chatus.dtos.GroupCreateDto;
import com.chatus.dtos.GroupMemberDto;
import com.chatus.dtos.GroupMessageDto;
import com.chatus.entities.GroupChat;
import com.chatus.entities.Message;
import com.chatus.entities.User;
import com.chatus.exceptions.ActionNotAllowedException;
import com.chatus.exceptions.DocumentNotFoundException;
import com.chatus.repositories.GroupChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.chatus.constants.DatabaseConstants.MAX_MESSAGES_IN_CHAT;
import static com.chatus.constants.ExceptionConstants.MEMBER_NOT_IN_CHAT;

@Service
@RequiredArgsConstructor
public class GroupChatService {
    private final GroupChatRepository groupChatRepository;
    private final UserService userService;
    private final MessageService messageService;
    public String create(GroupCreateDto groupCreateDto) {
        GroupChat groupChat = new GroupChat();
        groupChat.setName(groupCreateDto.getName());
        for(String userEmail : groupCreateDto.getMemberEmails()) {
            Optional<User> user = this.userService.getUserByEmail(userEmail);
            user.ifPresent(value -> groupChat.getMembers().add(value));
        }
        return this.groupChatRepository.save(groupChat).getId();
    }

    public GroupChat retrieveGroupAndCheckMembership(String memberEmailAddress, String groupId) throws DocumentNotFoundException, ActionNotAllowedException {
        GroupChat groupChat = this.groupChatRepository.findById(groupId)
                .orElseThrow(DocumentNotFoundException::new);
        if(memberNotInGroup(groupChat, memberEmailAddress)) {
            throw new ActionNotAllowedException(MEMBER_NOT_IN_CHAT);
        }
        return groupChat;
    }

    public boolean memberNotInGroup(GroupChat group, String userEmail) {
        return !group.getMembers().contains(new User(userEmail));
    }

    public void rename(GroupCompleteDto groupCompleteDto, String requesterEmailAddress) throws DocumentNotFoundException, ActionNotAllowedException {
        GroupChat groupChat = this.retrieveGroupAndCheckMembership(requesterEmailAddress, groupCompleteDto.getId());
        groupChat.setName(groupCompleteDto.getName());
    }

    public void addMember(GroupMemberDto groupMemberDto, String requesterEmailAddress) throws DocumentNotFoundException, ActionNotAllowedException {
        GroupChat groupChat = this.retrieveGroupAndCheckMembership(requesterEmailAddress, groupMemberDto.getGroupId());
        User user = this.userService.getUserByEmail(groupMemberDto.getMemberEmail())
                .orElseThrow(DocumentNotFoundException::new);
        groupChat.getMembers().add(user);
    }

    public void removeMember(GroupMemberDto groupMemberDto, String requesterEmailAddress) throws DocumentNotFoundException, ActionNotAllowedException {
        GroupChat groupChat = this.retrieveGroupAndCheckMembership(requesterEmailAddress, groupMemberDto.getGroupId());
        if (!groupChat.getMembers().contains(new User(groupMemberDto.getMemberEmail()))) {
            throw new DocumentNotFoundException();
        }
        groupChat.getMembers().remove(new User(groupMemberDto.getMemberEmail()));
    }

    public void delete(String id, String requesterEmailAddress) throws DocumentNotFoundException, ActionNotAllowedException {
        GroupChat groupChat = this.retrieveGroupAndCheckMembership(requesterEmailAddress, id);
        this.groupChatRepository.delete(groupChat);
    }

    public void addMessage(GroupMessageDto groupMessageDto, String requesterEmailAddress) throws DocumentNotFoundException, ActionNotAllowedException {
        GroupChat groupChat = this.retrieveGroupAndCheckMembership(requesterEmailAddress, groupMessageDto.getGroupId());
        Message message = this.messageService.getMessageObjectById(groupMessageDto.getMessageId());
        if (groupChat.getMessages().size() > MAX_MESSAGES_IN_CHAT) {
            groupChat.getMessages().remove(0); // pop oldest element
        }
        groupChat.getMessages().add(message);
    }
}
