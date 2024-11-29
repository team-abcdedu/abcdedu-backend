package com.abcdedu_backend.member.application;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.infra.file.FileDirectory;
import com.abcdedu_backend.infra.file.FileHandler;

import com.abcdedu_backend.member.domain.Member;
import com.abcdedu_backend.member.application.dto.MemberBasicInfoDto;
import com.abcdedu_backend.member.application.dto.MemberInfoDto;
import com.abcdedu_backend.member.application.dto.NameAndRoleDto;
import com.abcdedu_backend.member.application.dto.command.UpdateMemberInfoCommand;
import com.abcdedu_backend.member.application.out.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberInfoService implements MemberInfoUseCase{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileHandler fileHandler;

    @Override
    public MemberInfoDto getMemberInfo(Long memberId) {
        Member member = checkMember(memberId);

        String imageUrl = null;
        if (member.getImageObjectKey() != null) {
            imageUrl = fileHandler.getPresignedUrl(member.getImageObjectKey());
        }

        return MemberInfoDto.of(member, imageUrl);
    }

    @Override
    @Transactional
    public void updateMemberInfo(UpdateMemberInfoCommand command) {
        String uploadImageObjectKey = command.file() == null
                ? null
                : fileHandler.upload(command.file(), FileDirectory.PROFILE_IMAGE, command.memberId().toString());
        memberRepository.updateMemberInfo(command.memberId(), command.name(), command.school(), command.studentId(), uploadImageObjectKey);
    }

    @Override
    public NameAndRoleDto getMemberNameAndRoleInfo(Long memberId) {
        Member member = checkMember(memberId);
        return new NameAndRoleDto(member.getName(), member.getRole().getName());
    }

    public MemberBasicInfoDto getMemberBasicInfo(Long memberId) {
        Member member = checkMember(memberId);
        return MemberBasicInfoDto.of(member);
    }

    @Override
    @Transactional
    public void updatePassword(String email, String newPassword) {
        String newEncodedPassword = passwordEncoder.encode(newPassword);
        memberRepository.updatePassword(email, newEncodedPassword);

        log.info("멤버 email : {} 의 비밀번호가 임시 비밀번호로 변경되었습니다.", email);
    }

    @Override
    @Transactional
    public void updatePassword(Long memberId, String newPassword) {
        String newEncodedPassword = passwordEncoder.encode(newPassword);
        memberRepository.updatePassword(memberId, newEncodedPassword);

        log.info("멤버 ID : {} 의 비밀번호가 변경되었습니다.", memberId);
    }

    public Member checkMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
