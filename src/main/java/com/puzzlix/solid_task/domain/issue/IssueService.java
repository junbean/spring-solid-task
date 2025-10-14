package com.puzzlix.solid_task.domain.issue;

import com.puzzlix.solid_task.domain.issue.dto.IssueRequest;
import com.puzzlix.solid_task.domain.project.Project;
import com.puzzlix.solid_task.domain.project.ProjectRepository;
import com.puzzlix.solid_task.domain.user.Role;
import com.puzzlix.solid_task.domain.user.User;
import com.puzzlix.solid_task.domain.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service // IOC
@RequiredArgsConstructor
@Transactional
public class IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public Issue updateIssueStatus(
            Long issueId,
            IssueStatus status,
            String requestUserEmail,
            Role userRole
    ) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NoSuchElementException("해당 ID의 이슈를 찾을 수 없습니다."));
        //관리자가 아니거나 담당자가 아니면 상태를 변경 못함.
        if(userRole != Role.ADMIN && issue.getAssignee().getEmail().equals(requestUserEmail)){
            throw new SecurityException("이슈 살태를 변경할 권한이 없습니다.");
        }

        issue.setIssueStatus(status);
        return issue;
    }


    /**
     * 수정 기능
     * JPA 사용 (수정 전략) --> dirty checking
     * @param issueId
     * @param request
     * @param requestUserEmail
     * @return
     */
    public Issue updateIssue(
            Long issueId,
            IssueRequest.Update request,
            String requestUserEmail
    ) {
        User requestUser = userRepository.findByEmail(requestUserEmail)
                .orElseThrow(() -> new NoSuchElementException("요청한 사용자를 찾을 수 없습니다"));

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 이슈를 찾을 수 없습니다"));

        // 인가 처리 -> 관리자라면 수정 가능 하게 변경하자.
        // 인가 로직
        boolean isAdmin = requestUser.getRole() == Role.ADMIN;
        boolean isReporter = requestUser.getId().equals(issue.getReporter().getId());
        if(isAdmin == false && isReporter == false) {
            throw new SecurityException("이슈를 수정할 권한이 없습니다.");
        }

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

        // JPA 변경 감지(Dirty Checking) 덕분에 save() 명시적으로 호출 하지 않아도
        // 트랜잭션이 끝날 때 변경된 내용이 DB 에 자동으로 반영 된다.
        return issue;
    }


    /**
     * 이슈 삭제
     * 인가 처리 - ADMIN만 삭제 가능
     * @param issueId
     * @param requestUserEmail
     */
    public void deleteIssue(
            Long issueId,
            String requestUserEmail,
            Role userRole
    ) {
//        if(!issueRepository.existsById(issueId)) {
//            throw new NoSuchElementException("해당 ID의 이슈를 찾을 수 없습니다");
//        }

//        User user = userRepository.findByEmail(requestUserEmail)
//                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다"));

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NoSuchElementException("해당 하는 ID의 이슈를 찾을 수 없습니다."));

        if(userRole != Role.ADMIN && issue.getReporter().getEmail().equals(requestUserEmail) == false) {
            throw new SecurityException("삭제할 권한이 없습니다");
        }

        // 추후 고민..
        issueRepository.deleteById(issueId);
    }


    /**
     * 이슈 생성 로직
     * @param request
     * @return
     */
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

    /**
     * 모든 이슈 조회
     * 성능상의 이유로 readOnly만 해줌
     * @return
     */
    @Transactional(readOnly = true)
    public List<Issue> findIssues() {
        return issueRepository.findAll();
    }
}
