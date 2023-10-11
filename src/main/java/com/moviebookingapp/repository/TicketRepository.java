package com.moviebookingapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.moviebookingapp.models.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String> {

	@Query(value = "{'movieName' : ?0,'theatreName' : ?1}", fields = "{_id:0, seatNumber:1}")
	List<Ticket> findSeats(String movieName, String theatreName);

	List<Ticket> findByMovieName(String movieName);

}
