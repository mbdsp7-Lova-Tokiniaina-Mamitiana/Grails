package grails

class Profile {

    String iduser
    String nom
    String prenom
    double solde
    static constraints = {
        iduser blank: false, unique: true
        solde min: 0d
    }
}
