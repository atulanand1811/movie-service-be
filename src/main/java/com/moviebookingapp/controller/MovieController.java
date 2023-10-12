package com.moviebookingapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviebookingapp.exception.MoviesNotFound;
import com.moviebookingapp.models.Movie;
import com.moviebookingapp.models.Ticket;
import com.moviebookingapp.service.MovieService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1.0/moviebooking")
@OpenAPIDefinition(info = @Info(title = "Movie Application API", description = "This API provides endpoints for managing movies."))
@Slf4j
@CrossOrigin("*")
public class MovieController {

	@Autowired
	MovieService mService;

	@GetMapping("/all")
//	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "search all movies")
	public ResponseEntity<List<Movie>> getAllMovies() {
		log.debug("here u can access all the available movies");
		List<Movie> movieList = mService.getAllMovies();
		if (movieList.isEmpty()) {
			log.debug("currently no movies are available");
			throw new MoviesNotFound("No Movies are available");
		} else {
			log.debug("listed the available movies");
			return new ResponseEntity<>(movieList, HttpStatus.OK);
		}
	}

	@GetMapping("/hello")
	public ResponseEntity<String> sayHello(){
		return new ResponseEntity<>("Hello, My Backend deployed", HttpStatus.OK);
	}
	
	@GetMapping("/movies/search/{movieName}")
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "search movies by movie name")
	public ResponseEntity<List<Movie>> getMovieByName(@PathVariable String movieName) {
		log.debug("here search a movie by its name");
		List<Movie> movieList = mService.getMovieByName(movieName);
		if (movieList.isEmpty()) {
			log.debug("currently no movies are available");
			throw new MoviesNotFound("Movies Not Found");
		} else
			log.debug("listed the available movies with title:" + movieName);
		return new ResponseEntity<>(movieList, HttpStatus.OK);
	}

	@PostMapping("/{movieName}/add")
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "book ticket")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> bookTickets(@RequestBody Ticket ticket, @PathVariable String movieName) {
		log.debug(ticket.getLoginId() + " entered to book tickets");
		String response = mService.bookTickets(ticket, movieName);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getallbookedtickets/{movieName}")
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "get all booked tickets(Admin Only)")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<Ticket>> getAllBookedTickets(@PathVariable String movieName) {
		return new ResponseEntity<>(mService.getAllBookedTickets(movieName), HttpStatus.OK);
	}

	@PutMapping("/{movieName}/update")
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "updates movie(admin only)")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> updateTicketsStatus(@RequestBody Movie movie, @PathVariable String movieName) {
		String status = mService.updateTicketsStatus(movie, movieName);
		return new ResponseEntity<>(status, HttpStatus.OK);
	}

	@DeleteMapping("/{movieName}/delete")
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "delete a movie(Admin Only)")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteMovie(@PathVariable String movieName) {

		String deleteMovie = mService.deleteMovie(movieName);
		return new ResponseEntity<>(deleteMovie, HttpStatus.OK);
	}

}
