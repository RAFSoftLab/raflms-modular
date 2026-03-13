package raflms.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import raflms.dtos.StudentAssignmentResponse;
import raflms.dtos.StudentStartAssignmentRequest;

@SpringBootTest
@ActiveProfiles("test")
class StudentSubmissionServiceTest {

        @Autowired
        private StudentSubmissionService studSubmissionService;

        @Test
        public void testStartingAsignment(){
            StudentStartAssignmentRequest ssa = new StudentStartAssignmentRequest(4,"2019","RN","101","mojprvitestOOP","grupa1","termin1");
            StudentAssignmentResponse res = studSubmissionService.studentStartingAssigment(ssa);
            System.out.println(res);

        }
}