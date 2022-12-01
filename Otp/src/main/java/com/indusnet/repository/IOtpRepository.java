package com.indusnet.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.indusnet.model.OtpModel;

/**
 * This interface connect the to the database
 */
@Repository
public interface IOtpRepository extends JpaRepository<OtpModel, Integer>{

	
	public Optional<OtpModel> findByEmail(String email);
	
	@Query("select u.secret from OtpModel u where u.email = :email")
	public String getSecretKey(@Param("email") String email);
}
