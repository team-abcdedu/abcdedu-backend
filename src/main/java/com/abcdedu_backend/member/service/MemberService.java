package com.abcdedu_backend.member.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.infra.file.FileDirectory;
import com.abcdedu_backend.infra.file.FileHandler;
import com.abcdedu_backend.member.repository.MemberRepository;
import com.abcdedu_backend.member.dto.request.UpdateMemberInfoRequest;
import com.abcdedu_backend.member.dto.response.MemberBasicInfoResponse;
import com.abcdedu_backend.member.dto.response.MemberInfoResponse;
import com.abcdedu_backend.member.dto.response.MemberNameAndRoleResponse;
import com.abcdedu_backend.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileHandler fileHandler;


    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = checkMember(memberId);

        String imageUrl = null;
        if (member.getImageObjectKey() != null) {
            imageUrl = fileHandler.getPresignedUrl(member.getImageObjectKey());
        }

        return MemberInfoResponse.of(member, imageUrl);
    }

    @Transactional
    public void updateMemberInfo(Long memberId, UpdateMemberInfoRequest request, MultipartFile file) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        String uploadImageObjectKey = file == null
                ? null
                : fileHandler.upload(file, FileDirectory.PROFILE_IMAGE, memberId.toString());

        findMember.updateProfile(request.name(), uploadImageObjectKey, request.school(), request.studentId());
    }

    public MemberNameAndRoleResponse getMemberNameAndRoleInfo(Long memberId) {
        Member member = checkMember(memberId);

        return MemberNameAndRoleResponse.builder()
                .name(member.getName())
                .role(member.getRole().getName())
                .build();
    }


    public Member checkMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    public void checkDuplicateEmail(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (findMember.isPresent()) {
            throw new ApplicationException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    public MemberBasicInfoResponse getMemberBasicInfo(Long memberId) {
        Member member = checkMember(memberId);
        return MemberBasicInfoResponse.builder()
                .name(member.getName())
                .role(member.getRole().getName())
                .email(member.getEmail())
                .build();
    }

    @Transactional
    public void updatePassword(String email, String newPassword) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationException(ErrorCode.EMAIL_NOT_FOUND));

        String newEncodedPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(newEncodedPassword);

        log.info("멤버 ID : {} 의 비밀번호가 임시 비밀번호로 변경되었습니다.", member.getId());
    }

    @Transactional
    public void updatePassword(Long memberId, String newPassword) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        String newEncodedPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(newEncodedPassword);

        log.info("멤버 ID : {} 의 비밀번호가 변경되었습니다.", memberId);
    }

}
