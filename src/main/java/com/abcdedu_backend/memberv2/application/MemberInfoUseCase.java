package com.abcdedu_backend.memberv2.application;

import com.abcdedu_backend.memberv2.application.domain.Member;
import com.abcdedu_backend.memberv2.application.dto.MemberBasicInfoDto;
import com.abcdedu_backend.memberv2.application.dto.MemberInfoDto;
import com.abcdedu_backend.memberv2.application.dto.NameAndRoleDto;
import com.abcdedu_backend.memberv2.application.dto.command.UpdateMemberInfoCommand;

public interface MemberInfoUseCase {

    MemberInfoDto getMemberInfo(Long memberId);

    void updateMemberInfo(UpdateMemberInfoCommand command);

    NameAndRoleDto getMemberNameAndRoleInfo(Long memberId);

    MemberBasicInfoDto getMemberBasicInfo(Long memberId);

    void updatePassword(Long memberId, String newPassword);

    void updatePassword(String toEmail, String newPassword);

    Member checkMember(Long memberId);

}
