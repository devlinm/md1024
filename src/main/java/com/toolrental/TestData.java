package com.toolrental;

import com.toolrental.domain.entity.Tool;
import com.toolrental.domain.entity.ToolType;
import com.toolrental.domain.repository.Repository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Test data for use when testing using curl at command line.
 */
@Component
@Profile("test")
public class TestData {

    @Autowired
    private Repository<Tool, String> toolRepository;

    @Autowired
    private Repository<ToolType, String> toolTypeRepository;

    @PostConstruct
    public void setupTestData(){
        // this is the test data from the functional spec
        toolRepository.create(new Tool("CHNS", "Chainsaw", "Stihl"));
        toolRepository.create(new Tool("LADW", "Ladder", "Werner"));
        toolRepository.create(new Tool("JAKD", "Jackhammer", "DeWalt"));
        toolRepository.create(new Tool("JAKR", "Jackhammer", "Ridgid"));
        toolTypeRepository.create(new ToolType("Ladder", 1.99, true, true, false));
        toolTypeRepository.create(new ToolType("Chainsaw", 1.49, true, false, true));
        toolTypeRepository.create(new ToolType("Jackhammer", 2.99, true, false, false));
    }
}
