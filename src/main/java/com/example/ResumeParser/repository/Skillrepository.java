package com.example.ResumeParser.repository;

import com.example.ResumeParser.entity.Skill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Skillrepository extends JpaRepository<Skill, Long> {

 
    Optional<Skill> findByName(String name);
}
