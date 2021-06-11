package grails

import grails.gorm.transactions.Transactional

@Transactional
class ProfilService implements IProfileService {

    def serviceMethod() {

    }

    @Override
    Profile get(Serializable id) {
        return Profile.findByIduser(id);
    }

    @Override
    List<Profile> list(Map args) {
        return null
    }

    @Override
    Long count() {
        return null
    }

    @Override
    void delete(Serializable id) {

    }

    @Override
    Profile save(Profile profile) {
        return profile.save()
    }
    void addSolde(String uid,double solde){
        Profile profile1 = get(uid)
        profile1.setSolde(profile1.getSolde()+solde)
        profile1.save();
    }
}
