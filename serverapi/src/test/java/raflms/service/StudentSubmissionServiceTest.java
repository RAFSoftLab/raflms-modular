package raflms.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import raflms.dtos.StudentAssignementResponse;
import raflms.dtos.StudentStartAssignmentRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentSubmissionServiceTest {

        @Autowired
        private StudentSubmissionService studSubmissionService;

        @Test
        public void testStartingAsignment(){
            StudentStartAssignmentRequest ssa = new StudentStartAssignmentRequest(4,"2019","RN","101","mojsupertest","grupa3","prvi termin");
            StudentAssignementResponse res = studSubmissionService.studentStartingAssigment(ssa);
            System.out.println(res);

        }
}