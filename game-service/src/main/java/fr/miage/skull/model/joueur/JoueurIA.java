package fr.miage.skull.model.joueur;

import fr.miage.skull.CoupDTO;
import fr.miage.skull.CoupType;
import fr.miage.skull.DisqueType;
import fr.miage.skull.Phase;
import fr.miage.skull.model.Disque;

import java.util.List;
import java.util.Map;

public class JoueurIA extends AbstractJoueur {

    public JoueurIA(int id, String username) {
        super(id, username);
    }

    @Override
    public boolean estIA() {
        return true;
    }

    public CoupDTO genererCoup(Phase phase, int enchereCourrante, Map<Integer, Long> otherPlayerNbDisquePileAssoc){
        switch(phase){
            case Phase.PLACEMENT -> {
               if(super.doitParier()){
                   return new CoupDTO(CoupType.ENCHERIR, null, 1,super.getNbDisquePile(), 1);
               }
               //0->99
               if(super.getNbDisquePile() < 1 || super.getRand().nextInt(100) < 80){
                    //80% pour poser une tuile d'un type aléatoire
                   if(super.possedeDisqueType(DisqueType.SKULL)){
                       //50% de chance pour un skull, 50% pour autre
                       boolean skull = super.getRand().nextBoolean();
                       return new CoupDTO(CoupType.PLACER_DISQUE, skull ? DisqueType.SKULL : DisqueType.FLEUR, 1, 1, 1);
                   } else {
                       //Si seulement un disque fleur alors on pose une fleur
                       return new CoupDTO(CoupType.PLACER_DISQUE, DisqueType.FLEUR, 1, 1, 1);
                   }
               } else {
                   //Sinon on enchérit le nombre de disque dans notre pile
                   return new CoupDTO(CoupType.ENCHERIR, null, 1, super.getNbDisquePile(), 1);
               }
            }
            case Phase.ENCHERE -> {
                long maxEnchere = otherPlayerNbDisquePileAssoc.values().stream().mapToLong(Long::longValue).sum();
                if(super.getNbDisquePile() < 1 || enchereCourrante + 1 > maxEnchere){
                    return new CoupDTO(CoupType.ENCHERE_PASSE, null, 1, 1, 1);
                }
                boolean skullDansPile = super.getDisquePile().getDisques().values().stream().anyMatch(x -> x.getType()==DisqueType.SKULL);
                if(skullDansPile){
                    if(super.getRand().nextInt(100) < 70){
                        //On enchérit à +1 de l'enchère courrante
                        //Faire attention, si c'est le dernier voir ce que ça fait, on verra dans les tests
                        //Normalement le challenge commencera avant
                        return new CoupDTO(CoupType.ENCHERIR, null, 1, enchereCourrante+1, 1);
                    } else {
                        //Sinon on passe
                        return new CoupDTO(CoupType.ENCHERE_PASSE, null, 1, 1, 1);
                    }
                } else {
                    if(super.getRand().nextInt(100) < 20){
                        return new CoupDTO(CoupType.ENCHERIR, null, 1, enchereCourrante+1, 1);
                    } else {
                        return new CoupDTO(CoupType.ENCHERE_PASSE, null, 1, 1, 1);
                    }
                }
            }
            case Phase.REVELATION -> {
                boolean resteDesDisquesCaches = super.getDisquePile().getDisques().values().stream()
                        .anyMatch(Disque::estPasRevele);
                if (resteDesDisquesCaches) {
                    // Le joueur doit d'abord révéler tous les disques de sa propre pile
                    return new CoupDTO(CoupType.REVELER, null,1, 1, super.getId());
                }

                //On cherche les joueurs qui ont encore au moins un disque à révéler
                List<Integer> ciblesPossibles = otherPlayerNbDisquePileAssoc.entrySet().stream()
                        .filter(e -> e.getValue() > 0)
                        .map(Map.Entry::getKey)
                        .toList();

                //Parmis les joueurs qui ont encore au moins un disque, on en choisit un aléatoirement
                int cibleId = ciblesPossibles.get(super.getRand().nextInt(ciblesPossibles.size()));
                return new CoupDTO(CoupType.REVELER, null, 1, 1, cibleId);
            }
            case Phase.FIN_CHALLENGE -> {
                return new CoupDTO(CoupType.CHOIX_DISQUE_A_ENLEVER, null, 1, 1, 1);
            }
        }
        return null;
    }
}