package com.afb.dpd.orangemoney.jpa.tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CloseStream {
	
	
	/* Fermeture  du resultset */
	public static void fermeturesStream( ResultSet resultSet ) {
		if ( resultSet != null ) {
			try {
				resultSet.close();  
			} catch ( SQLException e ) {
			}
			
			try {
				resultSet.getStatement().close(); 
			} catch ( SQLException e ) {
			}
			
			try {
				resultSet.getStatement().getConnection().close(); 
			} catch ( SQLException e ) {
			}
		}
	}
	
	
	/* Fermeture  du resultset */
	/*
	public static void fermeturesStream( ResultSet resultSet ) {
		if ( resultSet != null ) {
			try {
				resultSet.close();
			} catch ( SQLException e ) {
				System.out.println( "Échec de la fermeture du ResultSet: " + e.getMessage() );
			}
		}
	}
	*/
	
	/* Fermeture  du statement */
	public static void fermeturesStream( Statement statement ) {
		if ( statement != null ) {
			try {
				statement.close();
			} catch ( SQLException e ) {
				System.out.println( "Échec de la fermeture du Statement: " + e.getMessage() );
			}
		}
	}
	
	/* Fermeture  de la connexion */
	public static void fermeturesStream( Connection connexion ) {
		if ( connexion != null ) {
			try {
				connexion.close();
			} catch ( SQLException e ) {
				System.out.println( "Échec de la fermeture de la connexion : " + e.getMessage() );
			}
		}
	}
	
	/* Fermetures du statement et de la connexion */
	public static void fermeturesStreams( Statement statement, Connection connexion ) {
		fermeturesStream( statement );
		fermeturesStream( connexion );
	}
	
	/* Fermetures du resultset, du statement et de la connexion */
	public static void fermeturesStreams( ResultSet resultSet, Statement statement, Connection connexion ) {
		fermeturesStream( resultSet );
		fermeturesStream( statement );
		fermeturesStream( connexion );
	}
	
	/* Fermetures du resultset, du statement et de la connexion */
	public static void fermeturesStreams( PreparedStatement pstate, Statement statement, Connection connexion ) {
		fermeturesStream( pstate );
		fermeturesStream( statement );
		fermeturesStream( connexion );
	}
	
	/* Fermetures du resultset, du statement et de la connexion */
	public static void fermeturesStreams(  ResultSet resultSet, PreparedStatement pstate, Statement statement, Connection connexion ) {
		fermeturesStream( resultSet );
		fermeturesStream( pstate );
		fermeturesStream( statement );
		fermeturesStream( connexion );
	}
	
}
