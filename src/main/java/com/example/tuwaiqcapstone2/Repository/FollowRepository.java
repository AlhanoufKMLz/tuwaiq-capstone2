package com.example.tuwaiqcapstone2.Repository;

import com.example.tuwaiqcapstone2.Model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {

    Follow findFollowById(Integer id);

    List<Follow> findFollowByFollowerId(Integer followerId);

    List<Follow> findFollowByFollowingId(Integer followingId);
}
