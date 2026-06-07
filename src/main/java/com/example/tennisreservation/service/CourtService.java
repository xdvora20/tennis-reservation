package com.example.tennisreservation.service;

import com.example.tennisreservation.dao.CourtDao;
import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourtService {

    private final CourtDao courtDao;

    public Court getById(Long id) {
        return courtDao
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Court not found: " + id));
    }

    public Court getByNumber(Integer courtNumber) {
        return courtDao
                .findByCourtNumber(courtNumber)
                .orElseThrow(() -> new NotFoundException("Court not found: number " + courtNumber));
    }

    public List<Court> getAll() {
        return courtDao.findAll();
    }

    public Court create(Court court) {
        return courtDao.save(court);
    }

    public Court update(Court court) {
        return courtDao.save(court);
    }

    public void delete(Long id) {
        if (!courtDao.deleteById(id)) {
            throw new NotFoundException("Court not found: " + id);
        }
    }
}
