package grails

import java.sql.Timestamp

class HistoriquePersonnel {

    String idUser
    String idPari
    String idMatch
    double montant
    String nomEquipe1
    String nomEquipe2
    String avatarEquipe1
    String avatarEquipe2
    String textePari
    double localisationx
    double localisationy
    Timestamp dateMatch
    static constraints = {
        idUser blank: false
        idPari blank: false
        idMatch blank: false
        montant blank: false
       

    }
}
