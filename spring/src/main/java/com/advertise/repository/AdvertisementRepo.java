package com.advertise.repository;

import com.advertise.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementRepo extends JpaRepository<Advertisement, Integer> {

}
