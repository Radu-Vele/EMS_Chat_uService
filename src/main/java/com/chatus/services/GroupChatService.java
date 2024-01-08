package com.chatus.services;

import com.chatus.dtos.groupChat.GroupCompleteDto;
import com.chatus.dtos.groupChat.GroupCreateDto;
import com.chatus.dtos.groupChat.GroupMemberDto;
import com.chatus.dtos.message.MessageCompleteDto;
import com.chatus.dtos.message.MessageSaveInChatDto;
import com.chatus.entities.GroupChat;
import com.chatus.entities.Message;
import com.chatus.entities.User;
import com.chatus.exceptions.ActionNotAllowedException;
import com.chatus.exceptions.DocumentNotFoundException;
import com.chatus.repositories.GroupChatRepository;
import com.chatus.utils.ChatRoomUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.chatus.constants.DatabaseConstants.MAX_MESSAGES_IN_CHAT;
import static com.chatus.constants.ExceptionConstants.MEMBER_NOT_IN_CHAT;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupChatService {
    private final GroupChatRepository groupChatRepository;
    private final UserService userService;
    private final MessageService messageService;
    private final ModelMapper modelMapper;
    private final ChatRoomUtil chatRoomUtil;

    public String create(GroupCreateDto groupCreateDto) throws DocumentNotFoundException {
        GroupChat groupChat = new GroupChat();
        groupChat.setName(groupCreateDto.getName());
        groupChat.setMembers(new ArrayList<>());
        List<User> members = new ArrayList<>();
        for(String userEmail : groupCreateDto.getMemberEmails()) {
            Optional<User> user = this.userService.getUserByEmail(userEmail);
            user.ifPresent(value -> {
                groupChat.getMembers().add(value);
                members.add(value);
            });
        }
        GroupChat savedChat = this.groupChatRepository.save(groupChat);
        for (User currUser : members) {
            this.userService.addChatInternal(currUser, savedChat);
        }
        return savedChat.getId();
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
        this.groupChatRepository.save(groupChat);
    }

    public void addMember(GroupMemberDto groupMemberDto, String requesterEmailAddress) throws DocumentNotFoundException, ActionNotAllowedException {
        GroupChat groupChat = this.retrieveGroupAndCheckMembership(requesterEmailAddress, groupMemberDto.getGroupId());
        User user = this.userService.getUserByEmail(groupMemberDto.getMemberEmail())
                .orElseThrow(DocumentNotFoundException::new);
        groupChat.getMembers().add(user);
        this.userService.addChatInternal(user, groupChat);
        this.groupChatRepository.save(groupChat);
    }

    public void removeMember(GroupMemberDto groupMemberDto, String requesterEmailAddress) throws DocumentNotFoundException, ActionNotAllowedException {
        GroupChat groupChat = this.retrieveGroupAndCheckMembership(requesterEmailAddress, groupMemberDto.getGroupId());
        int memberIndex = groupChat.getMembers().indexOf(new User(groupMemberDto.getMemberEmail()));
        if (memberIndex == -1) {
            throw new DocumentNotFoundException();
        }
        User memberToRemove = groupChat.getMembers().get(memberIndex);
        groupChat.getMembers().remove(new User(groupMemberDto.getMemberEmail()));
        this.userService.removeChatInternal(memberToRemove, groupChat);
        this.groupChatRepository.save(groupChat);
    }

    public void delete(String id, String requesterEmailAddress) throws DocumentNotFoundException, ActionNotAllowedException {
        GroupChat groupChat = this.retrieveGroupAndCheckMembership(requesterEmailAddress, id);
        groupChat.getMembers()
                .forEach( m -> { this.userService.removeChatInternal(m, groupChat); });
        this.groupChatRepository.delete(groupChat);
    }
    public String createMessageAndAddToGroup(MessageSaveInChatDto messageSaveDto, String requesterEmailAddress) throws DocumentNotFoundException, ActionNotAllowedException {
        GroupChat groupChat = this.retrieveGroupAndCheckMembership(requesterEmailAddress, messageSaveDto.getChatRoomId());
        if (!messageSaveDto.getSenderEmail().equals(requesterEmailAddress)) {
            throw new ActionNotAllowedException(); //only the requester can perform this action
        }
        Message newMessage = this.modelMapper.map(messageSaveDto, Message.class);
        newMessage.setId(null);
        newMessage.setChatRoom(groupChat.getId());
        Message messageInDb = this.messageService.saveInternal(newMessage);
        this.chatRoomUtil.addMessage(messageInDb, groupChat);
        this.groupChatRepository.save(groupChat);
        return messageInDb.getId();
    }

    public GroupCompleteDto getById(String id, String requesterEmailAddress) throws DocumentNotFoundException, ActionNotAllowedException {
        GroupChat groupChat = this.retrieveGroupAndCheckMembership(requesterEmailAddress, id);
        return GroupCompleteDto.builder()
                .id(groupChat.getId())
                .name(groupChat.getName())
                .membersEmails(groupChat.getMembers() == null ? new ArrayList<>() : groupChat.getMembers().stream()
                        .map(User::getEmailAddress)
                        .toList())
                .recentMessages(groupChat.getMessages() == null ? new ArrayList<>() : groupChat.getMessages().stream()
                        .map(m -> this.modelMapper.map(m, MessageCompleteDto.class))
                        .toList())
                .build();
    }

    public void editMessage(MessageCompleteDto messageCompleteDto, String requesterEmailAddress) throws DocumentNotFoundException, ActionNotAllowedException {
        GroupChat groupChat = this.retrieveGroupAndCheckMembership(requesterEmailAddress, messageCompleteDto.getChatRoomId());
        List<Message> messagesEmbedded = groupChat.getMessages();
        for(int i = 0; i < messagesEmbedded.size(); i++) {
            if (messagesEmbedded.get(i).getId().equals(messageCompleteDto.getId())) {
                Message previousMessage = groupChat.getMessages().get(i);
                this.messageService.editInternal(previousMessage, messageCompleteDto);
            }
        }
        this.messageService.edit(messageCompleteDto);
        this.groupChatRepository.save(groupChat);
    }

    public List<MessageCompleteDto> getMessagesBefore(String chatRoomId, Long timestamp, String requesterEmailAddress) throws DocumentNotFoundException, ActionNotAllowedException {
        GroupChat groupChat = this.retrieveGroupAndCheckMembership(requesterEmailAddress, chatRoomId);
        return this.messageService.getMessagesBefore(chatRoomId, timestamp);
    }
}
