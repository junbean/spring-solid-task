package com.puzzlix.solid_task.domain.issue;

import com.puzzlix.solid_task.domain.issue.dto.IssueRequest;
import com.puzzlix.solid_task.domain.project.Project;
import com.puzzlix.solid_task.domain.project.ProjectRepository;
import com.puzzlix.solid_task.domain.user.User;
import com.puzzlix.solid_task.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service // IOC
@RequiredArgsConstructor
@Transactional
public class IssueService {

    // 구체 클래스가 아닌, IssueRepository 라는 역할(인터페이스)에만 의존한다
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    // 수정 기능 중에 Update <--/ 이슈 ID
    // 1. 이슈 존재 여부 확인 -- SELECT
    // 2.
    // 3. JPA 사용 (수정 전략) --> dirty checking
    public Issue updateIssue(Long issueId, IssueRequest.Update request) {
        // 이슈ID가 실존하는 issue인지 판별
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 이슈를 찾을 수 없습니다."));

        // 넘어온 값이 담당자 할당 여부에 따로 분기 처리 되어야 함
        if(request.getAssignee_id() != null) {
            User assignee = userRepository.findById(request.getAssignee_id())
                    .orElseThrow(() -> new NoSuchElementException("해당 ID에 담당자를 찾을 수 없습니다"));
            // 담당자 할당
            issue.setAssignee(assignee);
        } else {
            // 담당자 해제 처리
            issue.setAssignee(null);
        }

        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());

        // JPA 변경 감지 (Dirty Checking) 덕분에 save() 명시적으로 호출하지 않아도
        // 트랜잭션이 끝날 때 변경된 내용이 DB에 자동으로 반영된다.

        return issue;
    }

    public void deleteIssue(Long issueId) {
        // 삭제는 DB에서 오류가 있나?
        if(!issueRepository.existsById(issueId)) {
            throw new NoSuchElementException("해당 ID의 이슈를 찾을 수 없습니다");
        }

        // 추후 고민..
        issueRepository.deleteById(issueId);
    }


    // 이슈 생성 로직
    public Issue createIssue(IssueRequest.Create request) {
        // 보고자 ID -> 실제 회원이 있는가?
        User reporter = userRepository.findById(request.getReporterId())
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다"));

        // 프로젝트 ID 검증
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 프로젝트를 찾을 수 없습니다"));

        Issue newIssue = new Issue();
        newIssue.setTitle(request.getTitle());
        newIssue.setDescription(request.getDescription());
        newIssue.setReporter(reporter);
        newIssue.setProject(project);
        
        // 이슈 -> TODO
        newIssue.setIssueStatus(IssueStatus.TODO);

        return issueRepository.save(newIssue);
    }

    // 모든 이슈 조회
    // 여기서는 성능상의 이유로 readOnly만 해줌
    @Transactional(readOnly = true)
    public List<Issue> findIssues() {
        return issueRepository.findAll();
    }



}
