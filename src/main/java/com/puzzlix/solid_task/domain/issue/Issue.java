package com.puzzlix.solid_task.domain.issue;

public class Issue {
    private Long id;
    private String title;
    private String description;
    private IssueStatus issueStatus; // 시작 진행중 완료 상태 (범위)

    // 추후 연관 관계 필드
    private Long projectId;

    // 누가 요청 (보고)
    private Long reporterId;

    // 담당자
    private Long assignId;
}

// 칸반 보드???

// pk
// 타이틀
// 내용
// 진행 상태

// 프로젝트 pk
// 보호자 (누가 요청 했는지)
// 담당자

