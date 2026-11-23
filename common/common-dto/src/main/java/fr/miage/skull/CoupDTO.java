package fr.miage.skull;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CoupDTO(
        @NotNull(message = "coupType est obligatoire")
        CoupType coupType,

        DisqueType disqueType,

        @Positive(message="Le slot doit être comprit entre 1 et n disques à enlever")
        int slotDisque,

        @Positive(message = "encherirNb doit être supérieur à 0")
        int encherirNb,

        @Positive(message = "revelerPlayerId doit être un identifiant valide")
        int revelerPlayerId) {

    @Override
    public CoupType coupType() {
        return coupType;
    }

    @Override
    public DisqueType disqueType() {
        return disqueType;
    }

    @Override
    public int encherirNb() {
        return encherirNb;
    }

    @Override
    public int slotDisque(){return slotDisque;}

    @Override
    public int revelerPlayerId() {
        return revelerPlayerId;
    }
}
