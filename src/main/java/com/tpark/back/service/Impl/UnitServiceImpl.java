package com.tpark.back.service.Impl;

import com.tpark.back.dao.UnitDAO;
import com.tpark.back.model.domain.UnitDomain;
import com.tpark.back.model.dto.UnitDTO;
import com.tpark.back.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UnitServiceImpl implements UnitService {

    private final UnitDAO unitDAO;

    @Autowired
    UnitServiceImpl(UnitDAO unitDAO){
        this.unitDAO = unitDAO;
    }

    @Override
    public List<UnitDTO> getUnitsByCourse(Integer courseId, String email) {
        return unitDAO.getUnitsByCourse(courseId, email)
                .stream().map(this::unitDomainToDTO).collect(Collectors.toList());
    }

    @Override
    public UnitDTO getUnit(Integer groupId, String email) {
        return unitDomainToDTO(unitDAO.getUnit(groupId, email));
    }

    @Override
    public void createUnit(UnitDTO unitDTO, String email) {
        unitDAO.createUnit(unitDTOToDomain(unitDTO), email);
    }

    @Override
    public void changeUnit(UnitDTO unitDTO, String email) {
        unitDAO.changeUnit(unitDTOToDomain(unitDTO), email);
    }

    @Override
    public void deleteUnit(int id, String email) {
        unitDAO.deleteUnit(id, email);
    }

    @Override
    public UnitDTO getUnitForStudent(Integer unitId, String student) {
        return unitDomainToDTO(unitDAO.getUnitForStudent(unitId, student));
    }

    @Override
    public List<UnitDTO> getUnitsByCourseForStudent(Integer courseId, String student) {
        return unitDAO.getUnitByCourseForStudent(courseId, student)
                .stream().map(this::unitDomainToDTO).collect(Collectors.toList());
    }

    @Override
    public List<UnitDTO> getAllUnits(String user) {
        return unitDAO.getUnits(user)
                .stream().map(this::unitDomainToDTO).collect(Collectors.toList());
    }

    @Override
    public List<UnitDTO> getAllUnitsForStudent(String student) {
        return unitDAO.getUnitsForStudent(student)
                .stream().map(this::unitDomainToDTO).collect(Collectors.toList());
    }

    private UnitDTO unitDomainToDTO(UnitDomain unitDomain) {
        UnitDTO unitDTO = new UnitDTO();
        unitDTO.setId(unitDomain.getId());
        unitDTO.setUnit_name(unitDomain.getUnit_name());
        unitDTO.setCourse_id(unitDomain.getCourse_id());
        unitDTO.setDescription(unitDomain.getDescription());
        unitDTO.setPrev_pos(unitDomain.getPrev_pos());
        unitDTO.setNext_pos(unitDomain.getNext_pos());
        unitDTO.setStatus(unitDomain.getStatus());
        unitDTO.setTags(unitDomain.getTags() == null ? null :
                Arrays.asList(unitDomain.getTags().split(",")));
        return unitDTO;
    }

    private UnitDomain unitDTOToDomain(UnitDTO unitDTO) {
        return UnitDomain.builder()
                .id(unitDTO.getId())
                .course_id(unitDTO.getCourse_id())
                .description(unitDTO.getDescription())
                .next_pos(unitDTO.getNext_pos())
                .prev_pos(unitDTO.getPrev_pos())
                .unit_name(unitDTO.getUnit_name())
                .status(unitDTO.getStatus())
                .tags(unitDTO.getTags().isEmpty() ? null : String.join(",",
                        unitDTO.getTags().stream().limit(3).collect(Collectors.toList())))
                .build();
    }
}
