package com.abcdedu_backend.member.application;

import com.abcdedu_backend.member.application.domain.Member;
import com.abcdedu_backend.member.application.dto.MemberBasicInfoDto;
import com.abcdedu_backend.member.application.dto.MemberInfoDto;
import com.abcdedu_backend.member.application.dto.NameAndRoleDto;
import com.abcdedu_backend.member.application.dto.command.UpdateMemberInfoCommand;

public interface MemberInfoUseCase {

    MemberInfoDto getMemberInfo(Long memberId);

    void updateMemberInfo(UpdateMemberInfoCommand command);

    NameAndRoleDto getMemberNameAndRoleInfo(Long memberId);

    MemberBasicInfoDto getMemberBasicInfo(Long memberId);

    void updatePassword(Long memberId, String newPassword);

    void updatePassword(String toEmail, String newPassword);

    Member checkMember(Long memberId);

}
