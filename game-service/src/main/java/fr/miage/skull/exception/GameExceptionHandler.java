package fr.miage.skull.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GameExceptionHandler {

    @ExceptionHandler(PartieIntrouvableException.class)
    public ProblemDetail handlePartieIntrouvable(PartieIntrouvableException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "La partie n'existe pas");
    }

    @ExceptionHandler(JoueurNonDansPartieException.class)
    public ProblemDetail handleJoueurNonDansPartie(JoueurNonDansPartieException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Le joueur n'a pas rejoint cette partie");
    }

    @ExceptionHandler(PartieNonCommenceeException.class)
    public ProblemDetail handlePartieNonCommencee(PartieNonCommenceeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "La partie n'a pas encore commencé");
    }

    @ExceptionHandler(PartieTermineeException.class)
    public ProblemDetail handlePartieTerminee(PartieTermineeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "La partie est terminée");
    }

    @ExceptionHandler(PasLeTourDuJoueurException.class)
    public ProblemDetail handlePasLeTourDuJoueur(PasLeTourDuJoueurException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Ce n'est pas votre tour");
    }

    @ExceptionHandler(SignalementDejaExistantException.class)
    public ProblemDetail handleSignalementDejaExistant(SignalementDejaExistantException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Vous avez déjà signalé ce joueur");
    }

    @ExceptionHandler(EnchereInvalideException.class)
    public ProblemDetail handleEnchereInvalide(EnchereInvalideException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, "L'enchère est invalide");
    }

    @ExceptionHandler(CoupNonAutoriseException.class)
    public ProblemDetail handleCoupNonAutorise(CoupNonAutoriseException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, "Ce coup n'est pas autorisé pendant cette phase");
    }

    @ExceptionHandler(DisqueIndisponibleException.class)
    public ProblemDetail handleDisqueIndisponible(DisqueIndisponibleException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, "Le joueur n'a plus de disque de ce type en main");
    }

    @ExceptionHandler(RevelationInvalideException.class)
    public ProblemDetail handleRevelationInvalide(RevelationInvalideException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, "Impossible de révéler un disque sur ce joueur");
    }

    @ExceptionHandler(AutoSignalementException.class)
    public ProblemDetail handleAutoSignalement(AutoSignalementException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, "Vous ne pouvez pas vous signaler vous-même");
    }

    @ExceptionHandler(SignalementIAException.class)
    public ProblemDetail handleSignalementIA(SignalementIAException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, "Une IA ne peut pas être signalée");
    }

    @ExceptionHandler(RegleMetierException.class)
    public ProblemDetail handleRegleMetier(RegleMetierException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, "Ce coup ne respecte pas les règles du jeu");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "La requête contient des champs invalides"
        );
        problem.setTitle("Erreur de Validation");
        // Extraction champ par champ des erreurs Bean Validation
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        problem.setProperty("invalidParams", errors);
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail handleMethodValidation(HandlerMethodValidationException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Paramètre de requête invalide");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Valeur invalide pour " + e.getName());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadable(HttpMessageNotReadableException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Requête mal formé");
    }

    @ExceptionHandler(DebutDeLaPartieImpossibleException.class)
    public ProblemDetail handleDebutPartieImpossible(DebutDeLaPartieImpossibleException e){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Impossible de débuter la partie dans ces conditions");
    }

    @ExceptionHandler(JoueurElimineException.class)
    public ProblemDetail handleJoueurElimineException(JoueurElimineException e){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Vous êtes éliminé, vous ne pouvez plus jouer");
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ProblemDetail handleGameNotFoundException(GameNotFoundException e){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Aucune partie n'existe pour cette id");
    }
}