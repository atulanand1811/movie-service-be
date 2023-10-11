package com.moviebookingapp.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value = "movie")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
	
    private ObjectId _id;
    private String movieName;
    private String theatreName;
    private Integer noOfTicketsAvailable;
    private String ticketsStatus;

    public Movie(String movieName, String theatreName, Integer noOfTicketsAvailable, String ticketsStatus) {
        this.movieName = movieName;
        this.theatreName = theatreName;
        this.noOfTicketsAvailable = noOfTicketsAvailable;
        this.ticketsStatus = ticketsStatus;
    }

    

    public Movie(String movieName, String theatreName, Integer noOfTicketsAvailable) {
        this.movieName = movieName;
        this.theatreName = theatreName;
        this.noOfTicketsAvailable = noOfTicketsAvailable;
    }

    public Movie(ObjectId _id, String movieName, String theatreName, Integer noOfTicketsAvailable) {
        this._id = _id;
        this.movieName = movieName;
        this.theatreName = theatreName;
        this.noOfTicketsAvailable = noOfTicketsAvailable;
    }
}
