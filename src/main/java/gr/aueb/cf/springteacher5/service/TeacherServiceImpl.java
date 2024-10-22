package gr.aueb.cf.springteacher5.service;


import gr.aueb.cf.springteacher5.dto.TeacherInsertDTO;
import gr.aueb.cf.springteacher5.dto.TeacherUpdateDTO;
import gr.aueb.cf.springteacher5.mapper.Mapper;
import gr.aueb.cf.springteacher5.model.Teacher;
import gr.aueb.cf.springteacher5.repository.TeacherRepository;
import gr.aueb.cf.springteacher5.service.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class TeacherServiceImpl implements ITeacherService {

    private final TeacherRepository teacherRepository;

//    @Autowired
//    public TeacherServiceImpl(TeacherRepository teacherRepository) {
//        this.teacherRepository = teacherRepository;
//    }

    @Transactional
    @Override
    public Teacher insertTeacher(TeacherInsertDTO dto) throws Exception {
        Teacher teacher = null;

        try {
            teacher = teacherRepository.save(Mapper.mapToTeacher(dto));
            if(teacher.getId() == null) {
                throw new Exception("Insert error");
            }
            log.info("Inserted teacher with id: " + teacher.getId());
        } catch (Exception e) {
            log.error("insert error: " + e.getMessage());
            throw e;
        }
        return teacher;
    }

    @Override
    @Transactional
    public Teacher updateTeacher(TeacherUpdateDTO dto) throws EntityNotFoundException {
        Teacher teacher;
        Teacher updatedTeacher;
        try {
            teacher = teacherRepository.findTeacherById(dto.getId());
            if (teacher == null) throw new EntityNotFoundException(Teacher.class, dto.getId());
            updatedTeacher = teacherRepository.save(Mapper.mapToTeacher(dto));
            log.info("Updated teacher with id: " + updatedTeacher.getId());


        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return updatedTeacher;
    }

    @Override
    @Transactional
    public Teacher deleteTeacher(Long id) throws EntityNotFoundException {
        Teacher teacher;

        try {
            teacher = teacherRepository.findTeacherById(id);
            if (teacher == null) throw new EntityNotFoundException(Teacher.class, id);
            teacherRepository.deleteById(id);
            log.info("Deleted teacher with id: " + id);

        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return teacher;
    }

    @Override
    public List<Teacher> getTeachersByLastname(String lastname) throws EntityNotFoundException {
        List<Teacher> teachers = new ArrayList<>();

        try {
            teachers = teacherRepository.findByLastnameStartingWith(lastname);
            if(teachers.isEmpty()) throw new EntityNotFoundException(Teacher.class,0L);
            log.info("Found " + teachers.size() + " teachers with lastname starting with: " + lastname);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return teachers;

    }

    @Override
    public Teacher getTeacherById(Long id) throws EntityNotFoundException {
        Teacher teacher;

        try {
            teacher = teacherRepository.findTeacherById(id);
            if(teacher == null) throw new EntityNotFoundException(Teacher.class, id);
            log.info("Found " + teacher.getId() + " teacher with id: " + id);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return teacher;
    }
}
