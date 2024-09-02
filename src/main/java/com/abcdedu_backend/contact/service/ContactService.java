package com.abcdedu_backend.contact.service;

import com.abcdedu_backend.contact.dto.request.ContactCreateRequest;
import com.abcdedu_backend.contact.dto.response.ContactListResponse;
import com.abcdedu_backend.contact.dto.response.ContactResponse;
import com.abcdedu_backend.contact.entity.Contact;
import com.abcdedu_backend.contact.entity.ContactType;
import com.abcdedu_backend.contact.repository.ContactRepository;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public Long createContact(ContactCreateRequest contactCreateRequest, ContactType contactType) {
        Contact contact = Contact.builder()
                .userName(contactCreateRequest.userName())
                .email(contactCreateRequest.email())
                .phoneNumber(contactCreateRequest.phoneNumber())
                .contactType(contactType)
                .content(contactCreateRequest.content())
                .title(contactCreateRequest.title())
                .build();
        Contact savedContact = contactRepository.save(contact);
        return savedContact.getId();
    }


    public List<ContactListResponse> readListContact() {
        List<Contact> contacts = contactRepository.findAll();
        return contacts.stream()
                .map(contact -> ContactListResponse.builder()
                        .createdAt(contact.getCreatedAt())
                        .title(contact.getTitle())
                        .contactType(contact.getContactType())
                        .userName(contact.getUserName())
                        .build())
                .collect(Collectors.toList());
    }

    public ContactResponse readContact(Long contactId) {
        Contact findContact = checkContact(contactId);
        return ContactResponse.builder()
                .contactType(findContact.getContactType())
                .title(findContact.getTitle())
                .userName(findContact.getUserName())
                .phoneNumber(findContact.getPhoneNumber())
                .email(findContact.getEmail())
                .content(findContact.getContent())
                .build();
    }


    public Contact checkContact(Long contactId) {
        return contactRepository.findById(contactId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CONTACT_NOT_FOUND));
    }
}
