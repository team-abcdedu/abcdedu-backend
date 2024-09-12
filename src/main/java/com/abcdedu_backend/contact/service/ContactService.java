package com.abcdedu_backend.contact.service;

import com.abcdedu_backend.contact.dto.request.ContactCreateRequest;
import com.abcdedu_backend.contact.dto.response.ContactListResponse;
import com.abcdedu_backend.contact.dto.response.ContactGetResponse;
import com.abcdedu_backend.contact.entity.Contact;
import com.abcdedu_backend.contact.entity.ContactType;
import com.abcdedu_backend.contact.repository.ContactRepository;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final MemberService memberService;

    @Transactional
    public Long createContact(ContactCreateRequest contactCreateRequest) {
        Contact contact = Contact.builder()
                .userName(contactCreateRequest.userName())
                .email(contactCreateRequest.email())
                .phoneNumber(contactCreateRequest.phoneNumber())
                .contactType(ContactType.of(contactCreateRequest.type()))
                .content(contactCreateRequest.content())
                .title(contactCreateRequest.title())
                .build();
        Contact savedContact = contactRepository.save(contact);
        return savedContact.getId();
    }


    public Page<ContactListResponse> readListContact(Long memberId, Pageable pageable) {
        checkPermission(memberId);
        Page<Contact> contacts = contactRepository.findAll(pageable);
        return contacts.map(contact -> ContactListResponse.builder()
                        .contactId(contact.getId())
                        .createdAt(contact.getCreatedAt())
                        .title(contact.getTitle())
                        .type(contact.getContactType().getType())
                        .userName(contact.getUserName())
                        .build());
    }

    public ContactGetResponse readContact(Long contactId, Long memberId) {
        checkPermission(memberId);
        Contact findContact = checkContact(contactId);
        return ContactGetResponse.builder()
                .type(findContact.getContactType().getType())
                .title(findContact.getTitle())
                .userName(findContact.getUserName())
                .phoneNumber(findContact.getPhoneNumber())
                .email(findContact.getEmail())
                .content(findContact.getContent())
                .createdAt(findContact.getCreatedAt())
                .build();
    }


    public Contact checkContact(Long contactId) {
        return contactRepository.findById(contactId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CONTACT_NOT_FOUND));
    }

    public void checkPermission(Long memberId) {
        if (!memberService.checkMember(memberId).isAdmin()) {
            throw new ApplicationException(ErrorCode.ADMIN_VALID_PERMISSION);
        }
    }

}
