package org.revature.courseservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.revature.courseservice.dto.CourseDTO;
import org.revature.courseservice.dto.InstructorDTO;
import org.revature.courseservice.dto.ModuleDTO;
import org.revature.courseservice.entity.Course;
import org.revature.courseservice.entity.Instructor;
import org.revature.courseservice.entity.Module;
import org.revature.courseservice.entity.Tag;
import org.revature.courseservice.repository.CourseRepository;
import org.revature.courseservice.repository.InstructorRepository;
import org.revature.courseservice.repository.ModuleRepository;
import org.revature.courseservice.repository.TagRepository;
import org.revature.courseservice.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final TagRepository tagRepository;
    private final ModuleRepository moduleRepository;


    @Override
    public Instructor createInstructor(InstructorDTO instructorDTO) {


        // Business Rule: No two instructors with the same email
        instructorRepository.findByInstructorEmail(instructorDTO.getInstructorEmail())
                .ifPresent(existing -> {
                    throw new RuntimeException(
                            "Instructor already exists with email: " + instructorDTO.getInstructorEmail());
                });

        Instructor instructor = new Instructor();
        instructor.setInstructorName(instructorDTO.getInstructorName());
        instructor.setInstructorEmail(instructorDTO.getInstructorEmail());
        instructor.setInstructorSpecialization(instructorDTO.getInstructorSpecialization());
        instructor.setCourses(new ArrayList<>());  // Empty list — no courses yet

        Instructor saved = instructorRepository.save(instructor);

        log.info("Instructor created: id={}, name={}, specialization={}",
                saved.getInstructorId(), saved.getInstructorName(), saved.getInstructorSpecialization());

        return saved;
    }

    @Override
    @Transactional // Wraps in a single DB transaction - all or nothing
    public Course createCourse(CourseDTO courseDTO) {
        //Fetch the Instructor (validates if exists)
        Instructor instructor = instructorRepository.findById(courseDTO.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found: "
                + courseDTO.getInstructorId()));

        //build the Course Entity
        Course course = new Course();
        course.setCourseTitle(courseDTO.getCourseTitle());
        course.setCourseDescription(courseDTO.getCourseDescription());
        course.setInstructor(instructor); // <- Sets manyToOne realtionship
        course.setCourseCredits(courseDTO.getCourseCredits());
        course.setCourseActive(true);

        //Lets handle tags
        //For each tag name: find existing tag or create a new one
        if (courseDTO.getTagNames() != null) {
            List<Tag> tags = courseDTO.getTagNames().stream()
                    .map(tagName -> tagRepository.findByTagName(tagName)
                            .orElseGet(() -> {
                                Tag newtag = new Tag();
                                newtag.setTagName(tagName);
                                return tagRepository.save(newtag);

                            }))
                    .collect(Collectors.toList());
            course.setTags(tags);  //Sets ManyToMany Relationship
        }

        //Lets handle Modules
        /**
         * Save course first to get its Id, then attach modules
         *
         */
        Course addCourse = courseRepository.save(course);

        if (courseDTO.getModules() != null) {
            List<Module> modules = courseDTO.getModules().stream()
                    .map(moduleDTO -> {
                        Module module = new Module();
                        module.setModuleName(moduleDTO.getModuleName());
                        module.setModuleContent(moduleDTO.getModuleContent());
                        module.setOrderIndex(moduleDTO.getOrderIndex());
                        module.setDurationInHours(moduleDTO.getDurationInHours());
                        module.setCourse(addCourse);
                        return module;
                    })
                    .collect(Collectors.toList());
            moduleRepository.saveAll(modules);
            addCourse.setModules(modules);
        }

        return addCourse;
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found: " + id));
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> getAllCoursesByTag(String tagName) {
        return courseRepository.findByTagsName(tagName);
    }

    @Override
    public Course addModuleToCourse(Long courseId, ModuleDTO moduleDTO) {

        Course course = getCourseById(courseId);

        Module module = new Module();
        module.setModuleName(moduleDTO.getModuleName());
        module.setModuleContent(moduleDTO.getModuleContent());
        module.setOrderIndex(moduleDTO.getOrderIndex());
        module.setDurationInHours(moduleDTO.getDurationInHours());
        module.setCourse(course);

        moduleRepository.save(module);
        course.getModules().add(module);
        return course;
    }

    @Override
    public Course enrolleStudent(Long courseId, Long studentId) {
        Course course = getCourseById(courseId);
        if (course.getEnrolledStudentIds().contains(studentId)) {
            throw new RuntimeException("Student already enrolled in this course");
        }
        course.getEnrolledStudentIds().add(studentId);
        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long courseId) {

    }
}
