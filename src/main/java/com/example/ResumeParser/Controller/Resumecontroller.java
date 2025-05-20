package com.example.ResumeParser.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.ResumeParser.Service.Resumeservice;
import com.example.ResumeParser.entity.User;
import com.example.ResumeParser.dto.ResumeWithSkillsDTO;
import com.example.ResumeParser.dto.Resumefilterrequest;
import com.example.ResumeParser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class Resumecontroller {

    @Autowired
    private Resumeservice resumeservice;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/hii")
    public String hello() {
        return "hello";
    }

    @PostMapping("/resumes/upload/{userId}")
    public ResponseEntity<String> uploadResume(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        System.out.println("Upload called with userId: " + userId);
        
        // ✅ Ensure the user exists before proceeding
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

        // ✅ Let the service handle associating resume with this user
        resumeservice.uploadResumeForUser(user.getId(), file);

        return ResponseEntity.ok("Resume uploaded and saved successfully for user with ID " + userId);
    }

    // New endpoint to get resumes by userId
    @GetMapping("/resumesByUserId")
    public ResponseEntity<List<ResumeWithSkillsDTO>> getResumesByUserId(@RequestParam("userId") Long userId) {
        List<ResumeWithSkillsDTO> resumes = resumeservice.getResumesByUserId(userId);
        return ResponseEntity.ok(resumes);
    }


@PostMapping("/filter-user-resumes")
public ResponseEntity<List<ResumeWithSkillsDTO>> filterUserResumes(@RequestBody Resumefilterrequest request) {
    List<ResumeWithSkillsDTO> filtered = resumeservice.filterResumesByUser(
        request.getUserId(),
        request.getSkills(),
        request.getMinYearsOfExperience()
    );
    return ResponseEntity.ok(filtered);
}


@DeleteMapping("/delete/{userId}")
public ResponseEntity<?> deleteUserResumes(@PathVariable Long userId) {
    resumeservice.deleteResumesByUserId(userId);
    return ResponseEntity.ok("User's resumes deleted successfully");
}
















}
