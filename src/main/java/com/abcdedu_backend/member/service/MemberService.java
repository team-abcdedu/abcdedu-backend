package com.abcdedu_backend.member.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.infra.file.FileDirectory;
import com.abcdedu_backend.infra.file.FileHandler;
import com.abcdedu_backend.member.dto.LoginTokenDTO;
import com.abcdedu_backend.member.dto.request.LoginRequest;
import com.abcdedu_backend.member.dto.request.SignUpRequest;
import com.abcdedu_backend.member.dto.request.UpdateMemberInfoRequest;
import com.abcdedu_backend.member.dto.response.MemberBasicInfoResponse;
import com.abcdedu_backend.member.dto.response.MemberInfoResponse;
import com.abcdedu_backend.member.dto.response.MemberNameAndRoleResponse;
import com.abcdedu_backend.member.dto.response.ReissueResponse;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.entity.RefreshToken;
import com.abcdedu_backend.member.repository.MemberRepository;
import com.abcdedu_backend.member.repository.RefreshTokenRepository;
import com.abcdedu_backend.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final FileHandler fileHandler;

    @Transactional
    public void signUp(SignUpRequest request){
        checkDuplicateEmail(request);
        Member signUpMember = createBasicMember(request);
        memberRepository.save(signUpMember);
    }

    @Transactional
    public void adminSignUp(SignUpRequest request) {
        checkDuplicateEmail(request);
        Member signUpMember = createAdminMember(request);
        memberRepository.save(signUpMember);
    }

    @Transactional
    public LoginTokenDTO login(LoginRequest request) {
        Member findMember = memberRepository.findByEmail(request.email())
                .orElseThrow(()-> new ApplicationException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.password() ,findMember.getEncodedPassword())){
            throw new ApplicationException(ErrorCode.LOGIN_FAILED);
        }

        String accessToken = jwtUtil.createAccessToken(findMember.getId());
        String refreshToken = jwtUtil.createRefreshToken(findMember.getId());

        refreshTokenRepository.save(new RefreshToken(refreshToken, findMember.getId()));

        return LoginTokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public ReissueResponse reissue(String refreshToken) {
        refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN));

        Long memberId = jwtUtil.getMemberIdFromRefreshToken(refreshToken);

        String accessToken = jwtUtil.createAccessToken(memberId);

        return new ReissueResponse(accessToken);
    }

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

    private Member createAdminMember(SignUpRequest request) {
       return createMember(request, MemberRole.ADMIN);
    }

    private Member createBasicMember(SignUpRequest request) {
        return createMember(request, MemberRole.BASIC);
    }

    public Member checkMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    private Member createMember(SignUpRequest request, MemberRole role) {
        Member member = Member.builder()
                .name(request.name())
                .email(request.email())
                .encodedPassword(passwordEncoder.encode(request.password()))
                .role(role)
                .build();
        return member;
    }

    private void checkDuplicateEmail(SignUpRequest request) {
        String signUpEmail = request.email();
        Optional<Member> findMember = memberRepository.findByEmail(signUpEmail);
        if (findMember.isPresent()) {
            throw new ApplicationException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }

    public MemberBasicInfoResponse getMemberBasicInfo(Long memberId) {
        Member member = checkMember(memberId);
        return MemberBasicInfoResponse.builder()
                .name(member.getName())
                .role(member.getRole().getName())
                .email(member.getEmail())
                .build();
    }

    public void checkAdminPermission(Member member) {
        if (!member.isAdmin()){
            throw new ApplicationException(ErrorCode.ADMIN_VALID_PERMISSION);
        }
    }
}
