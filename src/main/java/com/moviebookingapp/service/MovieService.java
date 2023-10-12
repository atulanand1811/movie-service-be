package com.moviebookingapp.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviebookingapp.exception.MoviesNotFound;
import com.moviebookingapp.exception.SeatAlreadyBooked;
import com.moviebookingapp.models.Movie;
import com.moviebookingapp.models.Ticket;
import com.moviebookingapp.repository.MovieRepository;
import com.moviebookingapp.repository.TicketRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MovieService {

	@Autowired
	MovieRepository mRepo;

	@Autowired
	TicketRepository tRepo;
	

	public List<Movie> getAllMovies() {
		return mRepo.findAll();
	}

	public List<Movie> getMovieByName(String movieName) {
		return mRepo.findByMovieName(movieName);
	}

	public String bookTickets(Ticket ticket, String movieName) {

		String response = "";
//		List<Ticket> allTickets = tRepo.findSeats(movieName, ticket.getTheatreName());
//		for (Ticket each : allTickets) {
//			for (int i = 0; i < ticket.getNoOfTickets(); i++) {
//				if (each.getSeatNumber().contains(ticket.getSeatNumber().get(i))) {
//					log.debug("seat is already booked");
//					throw new SeatAlreadyBooked("Seat number " + ticket.getSeatNumber().get(i) + " is already booked");
//				}
//			}
//		}
		Movie movie = mRepo.findAvailableTickets(movieName, ticket.getTheatreName()).get(0);
		Integer noOfTicketsAvailable = movie.getNoOfTicketsAvailable();
		if (noOfTicketsAvailable >= ticket.getNoOfTickets()) {

			log.info("available tickets " + noOfTicketsAvailable);
			movie.setNoOfTicketsAvailable(noOfTicketsAvailable - ticket.getNoOfTickets());
			mRepo.save(movie);
			tRepo.save(ticket);
			log.debug(ticket.getLoginId() + " booked " + ticket.getNoOfTickets() + " tickets");
			response = ticket.getNoOfTickets() + " Tickets of " + movieName + " Booked Successfully";
			return response;
		} else {
			log.debug("tickets sold out");
			response = "\"All tickets sold out\"";
			return response;
		}
	}

	public List<Ticket> getAllBookedTickets(String movieName) {
		List<Ticket> ticketList = tRepo.findByMovieName(movieName);
		return ticketList;
	}
	
	public String updateTicketsStatus(Movie movie, String movieName) {
		String ticketStatus = "BOOK ASAP";
		String res = "";
		List<Movie> availableMovies = mRepo.findAvailableTickets(movieName,movie.getTheatreName());
        if(availableMovies.isEmpty()){
            mRepo.save(movie);
            res = "Movie added successfully";
        }else{
            ObjectId objectId = availableMovies.get(0).get_id();
            if(availableMovies.get(0).getNoOfTicketsAvailable() <= 0) {
            	ticketStatus = "SOLD OUT";
            }
            Movie newMovie = new Movie(
                    objectId,
                    movieName,
                    movie.getTheatreName(),
                    availableMovies.get(0).getNoOfTicketsAvailable(), 
                    ticketStatus
            );
            mRepo.save(newMovie);
            res = "Movie Updated successfully";
        }
        return res;
	}
	
	public String deleteMovie(String movieName) {
		String res = "No movies Available with moviename";
		List<Movie> availableMovies = mRepo.findByMovieName(movieName);
        if(availableMovies.isEmpty()){
            throw new MoviesNotFound("No movies Available with moviename "+ movieName);
        }
        else {
            mRepo.deleteByMovieName(movieName);
            res = "Movie deleted successfully";
        }
        return res;
	}

	public String addMovie(Movie movie) {
		List<Movie> movieList = mRepo.findAvailableTickets(movie.getMovieName(),movie.getTheatreName());
		if(movieList!=null && !movieList.isEmpty()) {
			Movie movieNew = movieList.get(0);
			movieNew.setNoOfTicketsAvailable(movie.getNoOfTicketsAvailable());
			Movie movie2 = mRepo.save(movieNew);
			return movie2 != null ? movie.getMovieName() + " Movie Details Updated SuccessFully !!" : "Operation Failed!!";

		}
		Movie save = mRepo.save(movie);
		
		return save != null ? movie.getMovieName() + " Movie Added SuccessFully !!" : "Operation Failed!!";
	}

}
