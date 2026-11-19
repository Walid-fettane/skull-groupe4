package fr.miage.skull.exception;

public class SalonNotFoundException extends RuntimeException {

    public SalonNotFoundException(Long id) {
        super("Salon introuvable avec l'id :"+id);
    }
}
